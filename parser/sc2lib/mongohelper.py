from sc2lib.Config import Config
from pymongo import *
import logging
import traceback
import sys
import datetime

__config = Config('config.json')
__client = None


def ensure_mongo_client():
    global __client
    if __client is None:
        try:
            logging.info('Creating mongo client.')
            __client = MongoClient(__config.mongodb_addr, w=0)
        except:
            __client = None
            logging.exception(traceback.format_exc())
    return __client


def save_object(collection, obj):
    try:
        logging.debug('Saving mongo object.')
        logging.debug(obj)
        client = ensure_mongo_client()
        with client.start_request():
            client.sc2[collection].save(obj)
            if collection == 'map':
                update_replay_map(client, obj)
        return True
    except:
        logging.error('Failed to save obj to db: ' + traceback.format_exc())
        return False


def update_replay_map(client, map):
    logging.info('Update map in replays')
    client.sc2['replay'].update({'map.hash': map['hash']}, {'$set': {'map.id': map['_id']}}, multi=True)


def delete_object(collection, id):
    try:
        client = ensure_mongo_client()
        with client.start_request():
            client.sc2[collection].remove({"_id": id})
        return True
    except:
        logging.error('Failed to save obj to db: ' + traceback.format_exc())
        return False


def get_obj_by_id(collection_name, id):
    try:
        logging.info('Getting mongo obj by id:' + str(id))
        client = ensure_mongo_client()
        with client.start_request():
            collection = client.sc2[collection_name]
            return collection.find_one({"_id": id})
    except:
        logging.exception(traceback.format_exc())
        return None


def get_recent_ids(collection_name, last_update_date):
    try:
        logging.info('Start get_recent_ids')
        client = ensure_mongo_client()
        with client.start_request():
            collection = client.sc2[collection_name]
            return [row['_id'] for row in collection.find({'last_edit_date': {'$gt': last_update_date}})]
    except:
        logging.exception(traceback.format_exc())
        return None


def get_replay_without_series():
    try:
        logging.info('Getting one unprocessed replay.')
        client = ensure_mongo_client()
        with client.start_request():
            return client.sc2.replay.find_one({'series_first_replay_id': {'$exists': False}})
    except:
        logging.exception(traceback.format_exc())
        return None


def update_replay_series(replays):
    try:
        logging.info('Persisting to mongodb')
        client = ensure_mongo_client()
        with client.start_request():
            sc2db = client.sc2
            for r in replays:
                sc2db.replay.update({'_id': r['_id']},
                                    {'$set': {
                                        'series_first_replay_id': r['series_first_replay_id'],
                                        'series_count': r['series_count'],
                                        'series_number': r['series_number'],
                                        'series_first_start_time': r['series_first_start_time'],
                                        'series_last_start_time': r['series_last_start_time'],
                                        'last_edit_date': datetime.datetime.now(),
                                    }})
    except:
        logging.exception(traceback.format_exc())


def get_replays_by_daterange(replay, delta):
    mindate = replay['start_time'] - datetime.timedelta(0, delta)
    maxdate = replay['end_time'] + datetime.timedelta(0, delta)
    try:
        client = ensure_mongo_client()
        with client.start_request():
            sc2db = client.sc2
            return sc2db.replay.find({'$or': [
                                                {
                                                    'start_time': {'$lt': replay['start_time']},
                                                    'end_time': {'$gt': mindate}
                                                },
                                                {
                                                    'start_time': {'$gt': replay['start_time'], '$lt': maxdate}
                                                },
                                             ],
                                      '_id': {'$ne': replay['_id']},
                                      'teams.players.toon_id' : {'$all':
                                                                     [replay['teams'][0]['players'][0]['toon_id'],
                                                                      replay['teams'][1]['players'][0]['toon_id']]}
                                      })
    except:
        logging.exception(traceback.format_exc())
        return None


def get_replays_without_details():
    '''
    :return: a list of [(id, file)] of which the detailed in-game events has not been parsed.
    '''
    try:
        client = ensure_mongo_client()
        with client.start_request():
            sc2db = client.sc2
            replays = sc2db.replay.find({'$or': [
                                                    {'detail_parsed': False},
                                                    {'detail_parsed': {'$exists': False}}
                                                ]
                                        },
                                        {'_id': True, 'replayFile': True})
            result = []
            for replay in replays:
                result.append((replay['_id'], replay['replayFile']))
            return result
    except:
        logging.exception(traceback.format_exc())
        return None


def get_liquid_progamers():
    try:
        client = ensure_mongo_client()
        with client.start_request():
            sc2db = client.sc2
            progamers = sc2db.crawled_progamer.find({}, {'_id': 0})
            result = []
            for progamer in progamers:
                result.append(progamer)
            return result
    except:
        logging.exception(traceback.format_exc())
        return None


def process_replay_event_stats(event_id):
    try:
        logging.info('Processing event stats for %d' % event_id)
        client = ensure_mongo_client()
        with client.start_request():
            db = client.sc2

            # get array for task Ids
            task_ids = []
            tasks = db.upload_task.find({'event_id': event_id}, {'_id': True})
            for task in tasks:
                task_ids.append(task['_id'])

            if len(task_ids) == 0:
                return

            # get total replays
            replay_count = db.replay.find({"taskIds": {"$in": task_ids}}).count()
            db.event_stats.update({'event_id': event_id},
                                  {'event_id': event_id, 'total_replays': replay_count, 'all_progamers_assigned': False},
                                  upsert=True)

            # get all the players
            player_urls = set()
            event_replays = db.replay.find({'taskIds': {'$in': task_ids}}, {'teams.players.url': True, '_id': False})
            for replay in event_replays:
                for team in replay['teams']:
                    for player in team['players']:
                        player_urls.add(player['url'])

            # save all the player for the event
            logging.info('Saving players for event: %d' % event_id)
            for player_url in player_urls:
                player = db.player.find_one({'url': player_url.lower()}, {'name': True, '_id': False})
                if player is None:
                    logging.error("Player not found %s" % player_url)
                    continue
                logging.info('Updating player: ' + player_url)
                db.event_players.update({'event_id': event_id, 'player_url': player_url},
                                {'$set': {'player_name': player['name']}},
                                upsert=True)

            logging.info('Done for event %d.' % event_id)
    except:
        logging.error('Failed to process_replay_event_stats: ' + traceback.format_exc())


def post_process_task(task_id):
    '''
    This function write the follow aggregated info for a task:
        event_stats:
            total_replays
        event_players: all players in the event
    :param task_id:
    :return:
    '''
    try:
        logging.info('Post processing for task %d' % task_id)
        client = ensure_mongo_client()
        with client.start_request():
            db = client.sc2
            count = db.replay.find({"taskIds": task_id}).count()
            event = db.upload_task.find_one({"_id": task_id}, {"event_id":True, "_id": False})
            if event is None or 'event_id' not in event:
                return

            # save total number of replay for a given event
            event_id = event['event_id']
            process_replay_event_stats(event_id)
    except:
        logging.error('Failed to post_process_task: ' + traceback.format_exc())



def db_get_replay(filehash):
    try:
        client = ensure_mongo_client()
        with client.start_request():
            return client.sc2.replay.find_one({"filehash": filehash})
    except:
        logging.error(traceback.format_exc())
        return None


def db_save_replay(replay):
    try:
        client = ensure_mongo_client()
        with client.start_request():
            sc2db = client.sc2
            replay['_id'] = int(sc2db.eval('getNextSequence("replays")'))
            now = datetime.datetime.now()
            replay['in_date'] = now
            replay['last_edit_date'] = now
            replay_collection = sc2db.replay
            replay_collection.insert(replay)

    except:
        logging.error(traceback.format_exc())
        return False

    return True


def db_update_task(replay, task_id):
    if 'taskIds' in replay and task_id in replay['taskIds']:
        return

    try:
        client = ensure_mongo_client()
        with client.start_request():
            sc2db = client.sc2
            replay_collection = sc2db.replay
            replay_collection.update({"_id": replay['_id']}, {'$push': {'taskIds': task_id}, '$set': {'last_edit_date': datetime.datetime.now()}}, True)

    except:
        logging.error(traceback.format_exc())
        return False


def save_replay_details(id, replay):
    logging.info("Saving events for replay: %d" % id)
    try:
        client = ensure_mongo_client()
        with client.start_request():
            sc2db = client.sc2
            replay_collection = sc2db.replay
            detail_parsed = False

            dbreplay = sc2db.replay.find_one({"_id": id}, {"teams": True})
            if dbreplay is None:
                logging.info("Replay does not exist.")
                return

            # update apm
            for team in replay['teams']:
                for player in team['players']:
                    p = get_player(dbreplay, player['url'])
                    if p is not None:
                        p['avg_apm'] = 0 if player['avg_apm'] is None else round(player['avg_apm'])

            # save events
            if 'events' not in replay or replay['events'] is None or len(replay['events']) == 0:
                # simply log error, will not change detail_parsed
                logging.error('Replay does not contain any events %d. ' % id)
            else:
                sc2db.replay_event.remove({"replay_id": id})
                for event in replay['events']:
                    event['replay_id'] = id
                    sc2db.replay_event.save(event)
                detail_parsed = True

            if detail_parsed:
                # increase number of parsed times regardless
                replay_collection.update({"_id": id},
                                         {'$inc': {'detail_parsing_times': 1},
                                          "$set": {"detail_parsed": detail_parsed,
                                                   "last_edit_date": datetime.datetime.now(),
                                                   "teams": dbreplay['teams']},
                                          "$unset": {'detail_in_progress': 0,
                                                     'detail_worker_id': 0,
                                                     'detail_start_time': 0}
                                          })
            else:
                replay_collection.update({"_id": id},
                                         {'$inc': {'detail_parsing_times': 1},
                                          "$set": {"last_edit_date": datetime.datetime.now(),
                                                   "teams": dbreplay['teams']},
                                          "$unset": {'detail_in_progress': 0,
                                                     'detail_worker_id': 0,
                                                     'detail_start_time': 0,
                                                     'detail_parsed': 0}
                                          })
            logging.info("Saved.")

    except:
        logging.exception(traceback.format_exc())


def save_replay_ability_events(replay_id, events):
    return save_object('replay_ability_events', {'_id': replay_id, 'events': events})


def save_replay_unit_born_events(replay_id, events):
    return save_object('replay_unit_born_events', {'_id': replay_id, 'events': events})


def save_replay_unit_init_events(replay_id, events):
    return save_object('replay_unit_init_events', {'_id': replay_id, 'events': events})


def save_replay_unit_done_events(replay_id, events):
    return save_object('replay_unit_done_events', {'_id': replay_id, 'events': events})


def save_replay_upgrade_complete_events(replay_id, events):
    return save_object('replay_upgrade_complete_events', {'_id': replay_id, 'events': events})


def save_replay_details_parsed_mark(replay_id, replay):
    logging.info("Saving details_parsed mark for replay: %d" % replay_id)
    try:
        client = ensure_mongo_client()
        with client.start_request():
            sc2db = client.sc2
            replay_collection = sc2db.replay

            if replay is not None:
                # getting replay for apm
                dbreplay = sc2db.replay.find_one({"_id": replay_id}, {"teams": True})
                if dbreplay is None:
                    logging.info("Replay does not exist.")
                    return

                # update apm
                for team in replay['teams']:
                    for player in team['players']:
                        p = get_player(dbreplay, player['url'])
                        if p is not None:
                            p['avg_apm'] = 0 if player['avg_apm'] is None else round(player['avg_apm'])

                # increase number of parsed times regardless
                replay_collection.update({"_id": replay_id},
                                         {'$inc': {'detail_parsing_times': 1},
                                          "$set": {"detail_parsed": True,
                                                   "last_edit_date": datetime.datetime.now(),
                                                   "teams": dbreplay['teams']},
                                          "$unset": {'detail_in_progress': 0,
                                                     'detail_worker_id': 0,
                                                     'detail_start_time': 0}
                                              })
            else:
                # increase number of parsed times regardless
                replay_collection.update({"_id": replay_id},
                                         {'$inc': {'detail_parsing_times': 1},
                                          "$set": {"detail_parsed": True,
                                                   "last_edit_date": datetime.datetime.now()},
                                          "$unset": {'detail_in_progress': 0,
                                                     'detail_worker_id': 0,
                                                     'detail_start_time': 0}
                                              })
            logging.info("Saved.")

    except:
        logging.exception(traceback.format_exc())


def get_player(replay, player_url):
    for team in replay['teams']:
        for player in team['players']:
            if player['url'] == player_url:
                return player
    return None


def update_obj_image(collection_name, id, image):
    try:
        logging.info('Start update_obj_image')
        client = ensure_mongo_client()
        with client.start_request():
            collection = client.sc2[collection_name]
            collection.update({'_id': id}, {'$set': {'image': image}})
    except:
        logging.exception(traceback.format_exc())
        return None


def delete_outdated_detail_processing_marks(hours):
    try:
        logging.info('delete outdated processing marks')
        client = ensure_mongo_client()
        with client.start_request():
            collection = client.sc2['replay']
            query = {'detail_start_time': {'$lt': datetime.datetime.now() - datetime.timedelta(hours=hours)}} \
                if hours > 0 else {'detail_in_progress': {'$exists': True}}
            res = collection.update(query,
                                    {'$unset': {'detail_start_time': 0, 'detail_in_progress': 0, 'detail_worker_id': 0}},
                                    multi=True)
            logging.info(res)
    except:
        logging.exception(traceback.format_exc())
        return None


def get_detail_work_count():
    try:
        logging.info('get_detail_work_count')
        client = ensure_mongo_client()
        with client.start_request():
            collection = client.sc2['replay']
            cursor = collection.find({'detail_worker_id': {'$exists': True}})
            count = cursor.count()
            cursor.close()
            return count
    except:
        logging.exception(traceback.format_exc())
        return 0


def fetch_update_replay_without_detail(lock=None):
    '''

    :return: 0 indicates an error.
    '''
    try:
        if lock is not None:
            lock.acquire()
        logging.info('get_detail_work_count')
        client = ensure_mongo_client()
        with client.start_request():
            collection = client.sc2['replay']
            replay = collection.find_one({'detail_parsed': {'$exists': False},
                                         'detail_in_progress': {'$exists': False}},
                                        {'_id': True, 'replayFile': True})
            if replay is not None:
                id = replay['_id']
                collection.update({'_id': id}, {'$set': {'detail_in_progress': True, 'detail_start_time': datetime.datetime.now()}})
                return (id, replay['replayFile'])
    except:
        logging.exception(traceback.format_exc())
    finally:
        if lock is not None:
            lock.release()
    return (0, None)


def update_replay_worker_id(worker_id, replay_id):
    try:
        logging.info('update_replay_worker_id')
        client = ensure_mongo_client()
        with client.start_request():
            collection = client.sc2['replay']
            replay = collection.find_one({'_id': replay_id}, {'detail_worker_id': True})
            if 'detail_worker_id' in replay and replay['detail_worker_id'] is not None:
                return False
            collection.update({'_id': replay_id}, {'$set': {'detail_worker_id': worker_id}})
            return True
    except:
        logging.exception(traceback.format_exc())
        return 0


def get_collection_ids(collection_name):
    try:
        client = ensure_mongo_client()
        with client.start_request():
            collection = client.sc2[collection_name]
            return collection.find({}, {"_id": True})
    except:
        logging.exception(traceback.format_exc())
        return None


def get_replay_ids():
    return get_collection_ids('replay')


def fix_upgrade_name():
    try:
        client = ensure_mongo_client()
        with client.start_request():
            collection = client.sc2['replay_upgrade_complete_events']
            # get replay id list
            replay_ids = []
            replays = collection.find({}, {"_id": True})
            for replay in replays:
                replay_ids.append(replay['_id'])
            replays.close()

            # do update events
            for replay_id in replay_ids:
                replay = get_obj_by_id('replay_upgrade_complete_events', replay_id)
                if 'events' not in replay:
                    continue
                for event in replay['events']:
                    if type(event['upgrade']) is not str:
                        event['upgrade'] = event['upgrade'].decode('utf-8')

                collection.save(replay)

            return collection.find({}, {"_id": True})
    except:
        logging.exception(traceback.format_exc())


def main():
    if len(sys.argv) == 1:
        print('Please specify the task Id. ')
        return
    task_id = int(sys.argv[1])
    post_process_task(task_id)


if __name__ == '__main__':
    main()
    # fix_upgrade_name()

