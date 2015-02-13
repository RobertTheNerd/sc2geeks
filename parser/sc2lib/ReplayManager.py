import traceback
import time

__author__ = 'robert'

from threading import Thread, Lock
import sc2lib.mongohelper as mongohelper
import os
import shutil
import logging
import sc2lib.utils as utils
from sc2lib.Config import Config
import sc2lib.sc2parser as sc2parser
import sys
import multiprocessing
import datetime


class ReplayManager:
    __config = Config('config.json')
    __exclude_abilities = set(__config.replay_event['ability']['exclude'])

    __detail_processing_lock = Lock()

    def __init__(self):
        pass

    @staticmethod
    def thread_process_file(task_id, filename, full_path, ext):
        logging.info('Start processing upload task: taskid=%s, filename=%s' % (task_id, filename))

        res = 'Failed'
        try:
            # get file type
            if ext == '.SC2REPLAY':
                res = ReplayManager.process_single_replay(task_id, full_path, filename)
            elif ext == '.ZIP':
                res = ReplayManager.process_replay_pack(task_id, filename, full_path)
        except:
            logging.error(traceback.format_exc())
        finally:
            logging.info('Process result %s ' % res)
            ReplayManager.end_task(task_id, False if res == 'Failed' else True, res)

        # process replay series
        try:
            mongohelper.post_process_task(task_id)

            logging.info('Processing series & replay details.')
            ReplayManager.process_series()
            ReplayManager.process_replay_events_parallel()
        except:
            logging.error(traceback.format_exc())


    @staticmethod
    def process_single_replay(taskId, full_path, filename=None, current_file=1, total_file=1):
        """

        :param taskId:
        :param full_path:
        :param filename: the part of full path that does not include working folder.
        :param current_file:
        :param total_file:
        :return:
        """

        working_folder = ReplayManager.__config.replay_working_folder
        if filename is None:
            filename = full_path

        # parse it
        ReplayManager.save_task_progress(taskId, 'Parse replay', 0, filename, current_file * 100 / total_file)

        # check file size for bypass
        size = os.path.getsize(full_path)
        if size <= ReplayManager.__config.size_to_bypass:
            logging.info("Bypass due to file size %d" % size)
            utils.move_file(full_path, ReplayManager.__config.replay_bypass_folder, working_folder)
            return "Bypass."

        replay = sc2parser.parse_replay_dict(full_path, True, False)
        real_file = full_path

        if replay is None:
            result = 'Failed'
        else:
            # check if replay exists
            filehash = replay['filehash']
            r = mongohelper.db_get_replay(filehash)

            if r is not None:
                result = 'Existed'
                mongohelper.db_update_task(r, taskId)
            else:
                result = 'Successful'

                # move replay file to repo dir. If the replay file is already in the repo folder,
                # the file will not be moved.
                targetFolder = ReplayManager.__config.replay_repo_folder
                if targetFolder not in full_path:
                    real_file = utils.move_file(full_path, targetFolder, working_folder)
                    if real_file is None:
                        # in this case, we need to move to the right folder
                        real_file = full_path
                        result = 'Failed'

                if result == 'Successful':
                    # save to db
                    ReplayManager.save_task_progress(taskId, 'Save replay', 80, filename, int(((current_file - 1) * 100 + 80)/total_file))
                    result = ReplayManager.save_replay_summary(replay, taskId, real_file, False)
                    if result != 'Successful':
                        # in this case, we need to move to the right folder
                        working_folder = targetFolder

        if result == 'Successful':
            ReplayManager.save_task_progress(taskId, 'Successful', 100, filename, int(((current_file - 1) * 100 + 80)/total_file))
            return result

        # hanlding the replay file for 'Failed' and 'Existed'
        try:
            logging.info('Move/remove replays files based on result %s' % result)
            if result == 'Existed':
                os.remove(real_file)
            elif result == 'Failed':
                utils.move_file(real_file, ReplayManager.__config.replay_failed_folder, working_folder)
            logging.info('Done.')
        except:
            logging.error("Failed. " + traceback.format_exc())
            result = 'Failed'

        ReplayManager.save_task_progress(taskId, result, 100, filename, int(((current_file - 1) * 100 + 80)/total_file))
        return result

    @staticmethod
    def process_replay_pack(task_id, filename, full_path):
        """
        :param task_id:
        :param filename:
        :param full_path:
        :return:
            'Failed' - if all replay failed
            'Successful' - if any replay succeeds or exists
        """
        # unzip to working folder
        (zipname, zipext) = os.path.splitext(filename)
        working_folder = os.path.join(ReplayManager.__config.replay_working_folder, str(task_id) + '-' + zipname)
        unzip_result = utils.unzip(full_path, working_folder)
        if unzip_result is None:
            logging.error('Failed to unzip file.')
            return "Failed"

        # get the list of actual files, removing files to bypass
        replayFiles = []
        for name in unzip_result:
            if name.lower().endswith('.sc2replay'):
                size = os.path.getsize(name)
                if size <= ReplayManager.__config.size_to_bypass:
                    logging.info('File %s bypassed for size: %d' % (name, size))
                else:
                    replayFiles.append(name)

        # process all the replays
        current = 0
        total = len(replayFiles)

        summary = {
            'Total': total,
            'Existed': 0,
            'Failed': 0,
            'Successful': 0,
        }

        for replayFile in replayFiles:
            current += 1
            partial_name = replayFile.replace(working_folder, '')
            # process replay without copying to repo folder
            logging.info("Parsing replay file: %s " % replayFile)
            res = ReplayManager.process_single_replay(task_id, replayFile, partial_name, current, total)
            if res not in summary:
                summary[res] = 0
            summary[res] += 1
            msg = utils.to_json(summary)
            if res == 'Failed':
                try:
                    logging.error("Failed to parse replay: " + traceback.format_exc())
                    msg += '\n Last Exception' + traceback.format_exc()
                except:
                    pass
            ReplayManager.save_task_progress(task_id, 'Replay parsed.', 100, partial_name, (current * 100 / total), msg)

        # remove working folder
        shutil.rmtree(working_folder)

        if summary['Failed'] == total:
            return 'Failed'
        return 'Successful'

    @staticmethod
    def process_task(task_id, filename, uploaded_obj):
        # parse replay
        # persistence replay
        # save processing result
        task_id = int(task_id)
        try:
            # get file type
            name, ext = os.path.splitext(filename)
            ext = ext.upper()

            if ext == '.SC2REPLAY':
                save_folder = ReplayManager.__config.replay_repo_folder
                full_path = utils.save_temp_file(save_folder, filename, uploaded_obj)
            elif ext == '.ZIP':
                save_folder = ReplayManager.__config.replay_zip_folder
                full_path = utils.save_temp_file(save_folder, filename, uploaded_obj, task_id)
            else:
                return 'Failed. File type is not supported. Only .SC2Replay and .zip files are currently allowed.'
            thread = Thread(target=ReplayManager.thread_process_file, args=(task_id, filename, full_path, ext,))
            thread.start()
        except:
            return traceback.format_exc()

        return "successful"

    @staticmethod
    def process_detail_replay(replay_id, replay_file):
        logging.info('Start getting replay for %d, file name: %s' % (replay_id, replay_file))
        replay = sc2parser.parse_replay_dict(replay_file, is_summary=False, load_map= False)
        mongohelper.save_replay_details(replay_id, replay)

    @staticmethod
    def process_series():
        logging.info('Process_Series started.')
        replay = mongohelper.get_replay_without_series()
        while replay is not None:
            ReplayManager.process_replay_series(replay)
            replay = mongohelper.get_replay_without_series()
        logging.info('Process_Series finished.')

    @staticmethod
    def process_replay_series(replay):
        # getting all neighboring replays
        logging.info('Processing replay for %s', replay['replayFile'])
        replay_dict = ReplayManager.get_neighboring_replays(replay)

        replay_series = [(r['start_time'], r) for r in replay_dict.values()]
        sorted_replays = sorted(replay_series, key=lambda tup: tup[0])
        first_replay = sorted_replays[0][1]
        last_replay = sorted_replays[len(sorted_replays) - 1][1]
        for idx, t in enumerate(sorted_replays):
            t[1]['series_first_replay_id'] = first_replay['_id']
            t[1]['series_count'] = len(sorted_replays)
            t[1]['series_number'] = idx + 1
            t[1]['series_first_start_time'] = first_replay['start_time']
            t[1]['series_last_start_time'] = last_replay['start_time']

        logging.info('Total replays found in the series: %d', len(sorted_replays))
        mongohelper.update_replay_series(replay_dict.values())

    @staticmethod
    def get_neighboring_replays(replay):
        replay_dict = {replay['_id']: replay}

        should_try_dict = True
        while should_try_dict:
            should_try_dict = False
            for rep in replay_dict.values():
                if 'checked' not in rep:
                    rep['checked'] = True
                    replay_neighbors = mongohelper.get_replays_by_daterange(rep, ReplayManager.__config.seconds_delta)
                    if replay_neighbors is None:
                        continue
                    for r in replay_neighbors:
                        if r['_id'] not in replay_dict:
                            should_try_dict = True
                            replay_dict[r['_id']] = r

                    if should_try_dict:
                        break
        return replay_dict

    #### process detailed replay info ####
    @staticmethod
    def process_replay_details():
        # get list of replays for details
        logging.info('Getting replays without detail.')
        replay_list = mongohelper.get_replays_without_details()
        if replay_list is None:
            logging.info("No replays to process.")
        else:
            logging.info('Find %d replays.' % len(replay_list))
            for (id, replayFile) in replay_list:
                ReplayManager.process_detail_replay(id, replayFile)
        logging.info('Done for getting replay details')

    @staticmethod
    def save_map(map):
        return utils.save_via_rest(ReplayManager.__config.url_map, map, ReplayManager.__config.debug_php)

    @staticmethod
    def save_player(player):
        return utils.save_via_rest(ReplayManager.__config.url_player, player, ReplayManager.__config.debug_php)

    @staticmethod
    def save_replay_summary(replay, task_id=None, replay_file=None, check_existence=True):
        """

        :param replay:
        :param task_id:
        :param replay_file:
        :return: "Existed", "Failed", "Successful"
        """

        # find existing replay using filehash
        filehash = replay['filehash']
        if check_existence:
            r = mongohelper.db_get_replay(filehash)

            if r is not None:
                mongohelper.db_update_task(r, task_id)
                logging.info('Replay already exists.')
                return 'Existed'

        # save map first
        if not ReplayManager.save_map(replay['map']):
            logging.error('Failed to save map.')
            return 'Failed'

        # save all players
        for team in replay['teams']:
            for player in team['players']:
                logging.info("Saving players")
                logging.info(player)
                if not ReplayManager.save_player(player):
                    logging.error("Failed to save player:")
                    logging.debug(player)
                    return 'Failed'

        # save filehash as mongodb _id
        # replay['_id'] = filehash
        # del replay['filehash']

        # taskid & replayFIle
        replay['taskIds'] = [task_id]

        # replace original file name as the actual replay file might be moved to the replay repo dir
        if replay_file is not None:
            del replay['filename']
            replay['replayFile'] = replay_file

        # delete possible events element which will be too huge for a summary to hold
        events = None
        if 'events' in replay:
            events = replay['events']
            del replay['events']

        if mongohelper.db_save_replay(replay):
            return 'Successful'

        if events is not None:
            replay['events'] = events
        return 'Failed'

    @staticmethod
    def save_task_progress(task_id, current_action=None, current_progress=None, current_file=None,
                           total_progress=None, message=None):
        msg = {
            'taskId': int(task_id),
        }
        if current_action is not None:
            msg['current_action'] = current_action
        if current_progress is not None:
            msg['current_progress'] = current_progress
        if current_file is not None:
            msg['current_file'] = current_file
        if total_progress is not None:
            msg['total_progress'] = total_progress
        if message is not None:
            msg['message'] = message

        utils.post_json_request(ReplayManager.__config.url_upload_task, utils.to_json(msg),
                                ReplayManager.__config.debug_php)

    @staticmethod
    def end_task(task_id, success=True, append_message=None):
        msg = {
            'taskId': int(task_id),
            'status': 'publish' if success else 'fail',
            'total_progress': 100,
            'current_progress': 100,
        }

        if append_message is not None:
            if 'message' not in msg:
                msg['message'] = ''
            msg['message'] = append_message + msg['message']

        utils.post_json_request(ReplayManager.__config.url_upload_task, utils.to_json(msg),
                                ReplayManager.__config.debug_php)

    @staticmethod
    def get_player(replay, player_url):
        for team in replay['teams']:
            for player in team['players']:
                if player['url'] == player_url:
                    return player
        return None

    @staticmethod
    def process_replay_events_parallel():
        mongohelper.delete_outdated_detail_processing_marks(ReplayManager.__config.replay_detail_mark_expiration)
        if mongohelper.get_detail_work_count() > 0:
            logging.info('Other worker are in progress. Quitting now.')
            return

        worker_count = multiprocessing.cpu_count() - ReplayManager.__config.replay_detail_reserved_cores_for_system
        if worker_count <= 0:
            worker_count = 1
        logging.info('Initial worker counts %d' % worker_count)

        replay_ids = []
        for i in range(worker_count):
            id, replay_file = mongohelper.fetch_update_replay_without_detail(ReplayManager.__detail_processing_lock)
            if id > 0:
                replay_ids.append((id, replay_file))
            else:
                break

        logging.info("Kicking off %d threads to process replay details" % len(replay_ids))
        for i in range(len(replay_ids)):
            thread = Thread(target=ReplayManager.thread_proc_process_replay_events, args=(i, replay_ids[i][0], replay_ids[i][1],))
            thread.start()


    @staticmethod
    def thread_proc_process_replay_events(thread_id, replay_id, replay_file):
        while replay_id > 0:
            if mongohelper.update_replay_worker_id(thread_id, replay_id):
                logging.info('Process detail for %d file: %s' % (replay_id, replay_file))
                replay = sc2parser.parse_replay_dict(replay_file, is_summary=False, load_map=False)

                if replay is None:
                    mongohelper.save_replay_details_parsed_mark(replay_id, replay)
                    replay_id, replay_file = mongohelper.fetch_update_replay_without_detail(ReplayManager.__detail_processing_lock)
                    continue

                # process events
                ability_events = []
                upgrade_complete_events = []
                unit_init_events = []
                unit_done_events = []
                unit_born_events = []

                for event in replay['events']:
                    try:
                        if hasattr(event, 'ability_name'):
                            if event.ability_name not in ReplayManager.__exclude_abilities:
                                ability_events.append({'frame': event.frame,
                                                       'pid': event.player.pid,
                                                       'ability': event.ability_name})
                        elif event.name == 'UnitBornEvent':
                            if event.upkeep_pid != 0 and not event.unit_type_name.startswith('Beacon'):
                                unit_born_events.append({'frame': event.frame,
                                                         'pid': event.upkeep_pid,
                                                         'unit_id': event.unit_id,
                                                         'unit_name': event.unit_type_name})
                        elif event.name == 'UnitInitEvent':
                            unit_init_events.append({'frame': event.frame,
                                                     'pid': event.upkeep_pid,
                                                     'unit_id': event.unit_id,
                                                     'unit_name': event.unit_type_name})
                        elif event.name == 'UnitDoneEvent':
                            if hasattr(event, 'unit') and hasattr(event.unit, 'owner'):
                                unit_done_events.append({'frame': event.frame,
                                                         'pid': event.unit.owner.pid,
                                                         'unit_id': event.unit.id,
                                                         'unit_name': event.unit.name.replace('SupplyDepotLowered', 'SupplyDepot')})
                        elif event.name == 'UpgradeCompleteEvent':
                            upgrade_complete_events.append({'frame': event.frame,
                                                            'pid': event.pid,
                                                            'upgrade': event.upgrade_type_name.decode("utf-8")})
                    except:
                        logging.info('Error occurred while processing event:' + traceback.format_exc())

                mongohelper.save_replay_ability_events(replay_id, ability_events)
                mongohelper.save_replay_unit_born_events(replay_id, unit_born_events)
                mongohelper.save_replay_unit_init_events(replay_id, unit_init_events)
                mongohelper.save_replay_unit_done_events(replay_id, unit_done_events)
                mongohelper.save_replay_upgrade_complete_events(replay_id, upgrade_complete_events)
                mongohelper.save_replay_details_parsed_mark(replay_id, replay)

            replay_id, replay_file = mongohelper.fetch_update_replay_without_detail(ReplayManager.__detail_processing_lock)
        logging.info('All tasks done for thread %d' % thread_id)
        return


def process_replay_ability_events():
    ids = mongohelper.get_replay_ids()
    if ids is not None:
        for idrow in ids:
            mongohelper.save_replay_ability_events(idrow['_id'])
        ids.close()

def main():
    if len(sys.argv) < 2:
        # print usage
        print("""
Usage: python ReplayManager.py command
    where command:
        ProcessSeries   - process replay series
        ProcessDetails  - process replay detailed events
        ProcessAll      - process both replay series and detailed events
        ClearDetailMarks - clear all detail marks. Make sure no worker is running!!
        ProcessEventStats - Calculate stats and players for a given event id
        """)
        return

    config = Config('config.json')
    os.environ['TZ'] = config.time_zone
    time.tzset()

    current_time = "{:%Y-%m-%d-%H:%M:%S}".format(datetime.datetime.now())
    logfile = os.path.join(config.log_folder, 'replay-manager-' + current_time + '.log')
    logging.basicConfig(filename=logfile, level=logging.DEBUG, format='[%(asctime)s]-[%(levelname)s]: %(message)s')

    command = sys.argv[1]
    if command == 'ProcessSeries':
        ReplayManager.process_series()
    elif command == 'ProcessDetails':
        ReplayManager.process_replay_events_parallel()
    elif command == 'ProcessAll':
        ReplayManager.process_series()
        ReplayManager.process_replay_events_parallel()
    elif command == 'ClearDetailMarks':
        mongohelper.delete_outdated_detail_processing_marks(0)
    elif command == 'ProcessEventStats':
        if len(sys.argv) < 3:
            print('Please specify event id.')
            return
        event_id = int(sys.argv[2])
        mongohelper.process_replay_event_stats(event_id)
    elif command == 'FixUpgradeName':
        mongohelper.fix_upgrade_name()


if __name__ == '__main__':
    main()