import logging

__author__ = 'robert'
import os
from sc2lib.Config import Config

__config = Config('config.json')
os.environ['SC2READER_CACHE_DIR'] = __config.sc2reader_cache_dir
os.environ['SC2READER_CACHE_MAX_SIZE'] = str(__config.sc2reader_cache_max_entries)

# make sure sc2reader is imported after the above setting
import sc2reader
from sc2reader.engine.plugins import APMTracker, SelectionTracker
from sc2reader.factories.plugins.utils import JSONDateEncoder
import json
import datetime
import sys
import traceback

# !!! plugins can be registered only ONE TIME due to the global scope !!!
sc2reader.engine.register_plugin(APMTracker())


def get_player_dict(player):
    return {
        'avg_apm': getattr(player, 'avg_apm', None),
        'color': player.color.__dict__ if hasattr(player, 'color') else None,
        'handicap': getattr(player, 'handicap', None),
        'name': getattr(player, 'name', None),
        'pick_race': getattr(player, 'pick_race', None),
        'pid': getattr(player, 'pid', None),
        'play_race': getattr(player, 'play_race', None),
        'result': getattr(player, 'result', None),
        'type': getattr(player, 'type', None),
        'uid': getattr(player, 'uid', None),
        'url': getattr(player, 'url', None),
        'region': getattr(player, 'region', None),
        'subregion': getattr(player, 'subregion', None),
        'toon_handle': getattr(player, 'toon_handle', None),
        'toon_id': getattr(player, 'toon_id', None),
    }


# obsolete
def clone_obj(obj, props, output=None):
    """
    copy an object by the specified property list. If output is not specified, a new object will be created,
    otherwise the existing object will be merged
    :param obj:
    :param props:
    :param output:
    :return:
    """
    if output is None:
        output = {}

    for prop in props:
        attr = getattr(obj, prop, None)
        output[prop] = attr
    return output


# obsolete
def get_event(e):
    event = clone_obj(e, ['frame', 'name', 'pid', 'second', 'sid'])

    # player id
    player = getattr(e, 'player', None)
    if player is not None:
        event['player_id'] = player.pid

    if e.name == 'AbilityEvent' or e.name == 'LocationAbilityEvent' or e.name == 'TargetAbilityEvent':
        clone_obj(e, ['ability_id', 'ability_name', 'ability_type', 'ability_type_data',
                      'command_index', 'flag', 'flags',
                      'has_ability', 'is_player_action', 'other_unit_id'], event)
        if e.name == 'LocationAbilityEvent':
            clone_obj(e, ['location'], event)
        elif e.name == 'TargetAbilityEvent':
            clone_obj(e, ['location', 'target_unit_id', 'upkeep_player_id'], event)
    elif e.name == 'AddToHotkeyEvent' or e.name == 'GetFromHotkeyEvent' or e.name == 'SetToHotkeyEvent':
        clone_obj(e, ['bank', 'control_group', 'hotkey', 'mask_data', 'mask_type',
                          'update_type'], event)
    elif e.name == 'CameraEvent':
        clone_obj(e, ['distance', 'location', 'pitch', 'x', 'y', 'yaw'], event)
    elif e.name == 'ChatEvent':
        clone_obj(e, ['target', 'text', 'to_all', 'to_allies', 'to_observers'], event)
    elif e.name == 'PacketEvent':
        clone_obj(e, ['info'], event)  # game load progress
    elif e.name == 'PlayerLeaveEvent':
        clone_obj(e, ['data'], event)  # always None?
    elif e.name == 'PlayerStatsEvent':
        clone_obj(e, ['vespene_current', 'resources_used_current', 'food_made', 'vespene_lost_technology',
                          'resources_lost', 'minerals_lost_technology', 'workers_active_count',
                          'minerals_used_active_forces', 'minerals_used_in_progress_technology',
                          'vespene_used_in_progress_technology', 'vespene_killed', 'vespene_collection_rate',
                          'vespene_lost', 'vespene_used_in_progress_economy', 'vespene_lost_economy',
                          'food_used', 'vespene_killed_economy', 'vespene_used_current_technology',
                          'vespene_used_current_army', 'minerals_used_in_progress',
                          'ff_minerals_lost_economy', 'vespene_killed_technology', 'ff_vespene_lost_economy',
                          'ff_vespene_lost_technology', 'vespene_used_current', 'ff_vespene_lost_army',
                          'minerals_used_current_technology', 'minerals_used_current_economy',
                          'vespene_used_in_progress_army', 'minerals_used_in_progress_economy',
                          'vespene_used_active_forces', 'minerals_current', 'ff_minerals_lost_army',
                          'minerals_lost_economy', 'vespene_used_current_economy',
                          'minerals_used_in_progress_army', 'minerals_used_current', 'minerals_used_current_army',
                          'vespene_lost_army', 'minerals_killed_technology', 'vespene_killed_army',
                          'vespene_used_in_progress', 'minerals_lost_army', 'resources_killed',
                          'resources_used_in_progress', 'ff_minerals_lost_technology', 'minerals_killed_economy',
                          'minerals_collection_rate', 'minerals_killed_army', 'minerals_lost', 'minerals_killed'],
                  event)
        stats = getattr(e, 'stats', None)
        if stats is not None:
            event['stats'] = {str(key): val for key, val in stats.items()}

    elif e.name == 'SelectionEvent':
        clone_obj(e, ['bank', 'control_group', 'mask_data', 'mask_type', 'to_observers',
                          'new_unit_ids', 'new_unit_info', 'new_unit_types', 'subgroup_index'], event)
    elif e.name == 'UnitBornEvent':
        clone_obj(e, ['control_pid', 'location', 'unit_id', 'unit_type_name', 'upkeep_pid'], event)
        event['unit'] = get_unit(e.unit)
    elif e.name == 'UnitDiedEvent':
        clone_obj(e, ['killer_pid', 'killing_player_id', 'killing_unit_id', 'unit_type_name', 'upkeep_pid',
                          'unit_id', 'location'], event)
    elif e.name == 'UnitDoneEvent':
        clone_obj(e, ['unit_id'], event)
    elif e.name == 'UnitInitEvent':
        clone_obj(e, ['unit_id', 'control_pid', 'location', 'upkeep_pid'], event)
        event['unit'] = get_unit(e.unit)
    elif e.name == 'UnitPositionEvent':
        clone_obj(e, ['first_unit_index', 'positions'], event)
        event['unit_ids'] = [unit.id for unit, pos in e.units.items()]
    elif e.name == 'UnitTypeChangeEvent':
        clone_obj(e, ['unit_id', 'unit_type_name'], event)
    elif e.name == 'UpgradeCompleteEvent':
        clone_obj(e, ['count'], event)
        event['upgrade_type_name'] = str(e.upgrade_type_name, encoding='UTF-8')
    return event


def get_unit(u):
    unit = clone_obj(u, ['id', 'is_army', 'is_building', 'is_worker', 'finished_at',
                         'died_at', 'flags', 'hallucinated', 'location', 'name',
                         'started_at', 'type'])
    if u.type_history is not None:
        type_history = {}
        for time, type in u.type_history.items():
            type_history[str(time)] = type.id
        unit['type_history'] = type_history

    # owner
    owner = getattr(u, 'owner', None)
    if owner is not None:
        unit['owner_id'] = owner.pid

    return unit


def get_team_dict(team):
    t = {
        'number': team.number,
        'lineup': team.lineup,
        'result': team.result,
        'players': list(),
    }
    for player in team.players:
        t['players'].append(get_player_dict(player))
    return t


def prepare_replay(replay):
    game_events = replay.total_game_events = dict()
    for event in replay.events:
        if event.name not in game_events:
            game_events[event.name] = list()

        game_events[event.name].append(event)

    return replay


def get_map_dict(replay):
    if replay.map_file is None:
        return None
    map = replay.map_file
    obj = clone_obj(map, ['hash', 'server', 'type', 'url', 'url_template'])
    obj['name'] = replay.map_name
    return obj


def get_replay_dict(replay, summary=True):
    # Build observers into dictionary
    observers = [get_player_dict(player) for player in replay.observers]

    # build teams
    teams = [get_team_dict(team) for team in replay.teams]

    # Consolidate replay metadata into dictionary
    replay_dict = {
        'region': getattr(replay, 'region', None),
        'map': get_map_dict(replay),
        'file_time': getattr(replay, 'file_time', None),
        'filehash': getattr(replay, 'filehash', None),
        'unix_timestamp': getattr(replay, 'unix_timestamp', None),
        'start_time': getattr(replay, 'date', None),
        'utc_date': getattr(replay, 'utc_date', None),
        'speed': getattr(replay, 'speed', None),
        'category': getattr(replay, 'category', None),
        'type': getattr(replay, 'type', None),
        'is_ladder': getattr(replay, 'is_ladder', False),
        'is_private': getattr(replay, 'is_private', False),
        'filename': getattr(replay, 'filename', None),
        'file_time': getattr(replay, 'file_time', None),
        'frames': getattr(replay, 'frames', None),
        'build': getattr(replay, 'build', None),
        'release': getattr(replay, 'release_string', None),
        'game_fps': getattr(replay, 'game_fps', None),
        'game_length': getattr(getattr(replay, 'game_length', None), 'seconds', None),
        'teams': teams,
        'observers': observers,
        'real_length': getattr(getattr(replay, 'real_length', None), 'seconds', None),
        'real_type': getattr(replay, 'real_type', None),
        'time_zone': getattr(replay, 'time_zone', None),
        'versions': getattr(replay, 'versions', None),
    }
    start_time = replay_dict['start_time']
    if start_time is not None:
        replay_dict['end_time'] = start_time + datetime.timedelta(0, replay_dict['real_length'])

    if not summary:
        # events
        replay_dict['events'] = replay.events
        # replay_dict['units'] = [get_unit(u) for u in replay.active_units]

    return replay_dict


def parse_replay_json(replayFile, is_summary=True, load_map=False):
    replay_dict = parse_replay_dict(replayFile, is_summary, load_map)
    if replay_dict is None:
        return None
    return json.dumps(replay_dict, cls=JSONDateEncoder)


def parse_replay_dict(replayFile, is_summary=True, load_map=False):
    try:
        # sc2reader.engine.register_plugin(SelectionTracker())
        replay = sc2reader.load_replay(replayFile, load_level=2 if is_summary else 4, load_map=load_map)

        return get_replay_dict(replay, is_summary)
    except:
        logging.error(traceback.format_exc())
        return None


def test_all_events():
    file = sys.argv[1]
    replay = parse_replay_dict(file, is_summary=False)
    events = {}
    for event in replay['events']:
        if event.name not in events:
            events[event.name] = []
        events[event.name].append(event)

    replay['all_events'] = events
    print(replay)


def test_save_jason():
    import codecs

    file = sys.argv[1]
    msg = parse_replay_json(file, False, True)
    target_file = sys.argv[2]
    with codecs.open(target_file, 'wb', 'utf-8-sig') as f:
        f.write(msg)
    pass


def main():
    test_save_jason()

if __name__ == "__main__":
    test_all_events()
