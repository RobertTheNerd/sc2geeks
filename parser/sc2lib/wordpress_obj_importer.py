import os
import mysql.connector
import re
import logging
import traceback
import datetime
import requests
import json
import time
import sys

from sc2lib.Config import Config
import sc2lib.mongohelper as mongohelper

__config = Config('config.json')


def import_player(last_update_date):
    query = """
select player.ID as _id, player.post_title as `name`,
    player.post_modified as last_edit_date,
    LOWER(url_db.meta_value) as url,
	toon_db.meta_value as toon_id,
	toon_handle_db.meta_value as toon_handle,
	region_db.meta_value as region,
	subregion_db.meta_value as subregion,
	play_race_db.meta_value as race,
	progamer_db.meta_value as progamer
from sc2_posts as player
left join sc2_postmeta as url_db on player.ID=url_db.post_id and url_db.meta_key='url'
left join sc2_postmeta as toon_db on player.ID=toon_db.post_id and toon_db.meta_key='toon_id'
left join sc2_postmeta as toon_handle_db on player.ID=toon_handle_db.post_id and toon_handle_db.meta_key='toon_handle'
left join sc2_postmeta as region_db on player.ID=region_db.post_id and region_db.meta_key='region'
left join sc2_postmeta as subregion_db on player.ID=subregion_db.post_id and subregion_db.meta_key='subregion'
left join sc2_postmeta as play_race_db on player.ID=play_race_db.post_id and play_race_db.meta_key='play_race'
left join sc2_postmeta as progamer_db on player.ID=progamer_db.post_id and progamer_db.meta_key='progamer'
where post_type='player' and post_status in ('publish', 'draft') and player.post_modified > %s
    """
    import_mysql_obj(query, last_update_date, 'player', convert_mysql_multi_to_single, 'progamer')


def import_progamer(last_update_date):
    query = """
select progamer.ID as _id, progamer.post_title as name,
    progamer.post_modified as last_edit_date,
	link_db.meta_value as link,
	native_name_db.meta_value as native_name,
	romanized_name_db.meta_value as romanized_name,
	country_db.meta_value as country,
	race_db.meta_value as race,
	team_db.meta_value as team,
	birthday_db.meta_value as birthday,
	twitter_url_db.meta_value as twitter_url,
	pan_page_db.meta_value as pan_page,
	other_ids_db.meta_value as other_ids
from sc2_posts as progamer
left join sc2_postmeta as link_db on progamer.ID=link_db.post_id and link_db.meta_key='link'
left join sc2_postmeta as native_name_db on progamer.ID=native_name_db.post_id and native_name_db.meta_key='native_name'
left join sc2_postmeta as romanized_name_db on progamer.ID=romanized_name_db.post_id and romanized_name_db.meta_key='romanized_name'
left join sc2_postmeta as country_db on progamer.ID=country_db.post_id and country_db.meta_key='country'
left join sc2_postmeta as race_db on progamer.ID=race_db.post_id and race_db.meta_key='race'
left join sc2_postmeta as team_db on progamer.ID=team_db.post_id and team_db.meta_key='team'
left join sc2_postmeta as birthday_db on progamer.ID=birthday_db.post_id and birthday_db.meta_key='birthday'
left join sc2_postmeta as twitter_url_db on progamer.ID=twitter_url_db.post_id and twitter_url_db.meta_key='twitter_url'
left join sc2_postmeta as pan_page_db on progamer.ID=pan_page_db.post_id and pan_page_db.meta_key='pan_page'
left join sc2_postmeta as other_ids_db on progamer.ID=other_ids_db.post_id and other_ids_db.meta_key='other_ids'
where post_type='progamer' and post_status in ('publish', 'draft') and progamer.post_modified > %s;
    """
    import_mysql_obj(query, last_update_date, 'progamer')


def import_map(last_update_date):
    query = """
select map.ID as _id, map.post_title as `name`,
    map.post_modified as last_edit_date,
	LOWER(url_db).meta_value as url,
	server_db.meta_value as server,
	hash_db.meta_value as hash
from sc2_posts as map
left join sc2_postmeta as url_db on map.ID=url_db.post_id and url_db.meta_key='url'
left join sc2_postmeta as server_db on map.ID=server_db.post_id and server_db.meta_key='server'
left join sc2_postmeta as hash_db on map.ID=hash_db.post_id and hash_db.meta_key='hash'
where post_type='map' and post_status in ('publish', 'draft') and map.post_modified > %s
    """
    import_mysql_obj(query, last_update_date, 'map')


def import_event(last_update_date):
    query = """
select event.ID as _id, event.post_title as `name`,
    event.post_modified as last_edit_date,
	link_db.meta_value as link,
	start_date_db.meta_value as start_date,
	description_db.meta_value as description,
	parent_db.meta_value as parent
from sc2_posts as event
left join sc2_postmeta as link_db on event.ID=link_db.post_id and link_db.meta_key='link'
left join sc2_postmeta as start_date_db on event.ID=start_date_db.post_id and start_date_db.meta_key='start_date'
left join sc2_postmeta as description_db on event.ID=description_db.post_id and description_db.meta_key='description'
left join sc2_postmeta as parent_db on event.ID=parent_db.post_id and parent_db.meta_key='parent'
where post_type='event' and post_status in ('publish', 'draft') and event.post_modified > %s
    """
    import_mysql_obj(query, last_update_date, 'event', convert_mysql_multi_to_single, 'parent')


def import_task(last_update_date):
    query = """
select task_db.ID as _id, task_db.post_title as name,
    task_db.post_modified as last_edit_date,
	convert(event_db.meta_value, signed) as `event_id`,
	event_detail_db.post_title as event_name
from sc2_posts as task_db
left join sc2_postmeta as event_db on task_db.ID=event_db.post_id and event_db.meta_key='event'
inner join sc2_posts as event_detail_db on event_detail_db.ID = event_db.meta_value
where task_db.post_type='upload_task' and task_db.post_status in ('publish')
  and event_detail_db.post_status in ('publish', 'draft')
  and (task_db.post_modified > %s or event_detail_db.post_modified > %s)
    """
    import_mysql_obj(query, last_update_date, 'upload_task', date_parameter_count=2)


def import_mysql_obj(query, last_update_date, mongo_collection, mysql_post_process=None, special_property_name=None, date_parameter_count=1):
    try:
        logging.info('Start importing mysql object')
        cnx = mysql.connector.connect(host=__config.mysql_host, port=__config.mysql_port, user=__config.mysql_user,
                                      password=__config.mysql_password, database=__config.mysql_db)
        cursor = cnx.cursor()

        cursor.execute(query, ((last_update_date,) * date_parameter_count))
        for row in cursor:
            mysqlObj = dict(zip(cursor.column_names, row))
            if mysql_post_process is not None:
                mysql_post_process(mysqlObj, special_property_name)

            mongoObj = mongohelper.get_obj_by_id(mongo_collection, mysqlObj['_id'])

            image = None
            if mongoObj is not None:
                if 'image' in mongoObj:
                    image = mongoObj['image']
                    del mongoObj['image']

            if not dictionary_equal(mysqlObj, mongoObj):
                mysqlObj['image'] = image
                mongohelper.save_object(mongo_collection, mysqlObj)
        logging.info('updating images.')
        update_images(mongo_collection, last_update_date, __config.image_batch_size)
        logging.info('images updated.')
        logging.info('Imported.')
    except:
        logging.error(traceback.format_exc())


def convert_mysql_multi_to_single(obj, property):
    if property not in obj or obj[property] is None:
        return obj
    obj[property] = get_first_acf_element(obj[property])
    return obj


def get_first_acf_element(progamer_string):
    matches = re.findall('\"(.+?)\"', progamer_string)
    if matches is None or len(matches) == 0:
        return None

    return int(matches[0])


def compare_object_property(obj, property, value):
    if property not in obj:
        return value is None

    return obj[property] == value


def dictionary_equal(dic1, dic2):
    if dic1 is None:
        return dic2 is None

    if dic2 is None:
        return False

    if len(dic1.keys()) != len(dic2.keys()):
        return False

    for key1, val1 in dic1.items():
        if key1 not in dic2:
            return False
        if val1 != dic2[key1]:
            return False

    return True


def get_last_update_date(file):
    try:
        with open (file, "r") as myfile:
            data=myfile.read().replace('\n', '')

        return datetime.datetime.strptime(data, '%Y-%m-%d %H:%M:%S')
    except:
        logging.exception(traceback.format_exc())
        return datetime.datetime(1990, 1, 1)


def update_images(collection_name, last_update_date, batch_size):
    ids = mongohelper.get_recent_ids(collection_name, last_update_date)
    if ids is None:
        return
    logging.debug('Image ids:' + json.dumps(ids))
    max_index = len(ids)
    start_index = 0
    while start_index < max_index:
        next_index = min(start_index + batch_size, max_index)
        update_images_by_id(ids[start_index:next_index], collection_name)
        start_index = next_index


def update_images_by_id(ids, collection_name):
    response = requests.get(__config.url_image, params={'ids': json.dumps(ids)})
    if response.status_code != 200:
        return

    images = response.json()
    if len(images) == 0:
        return

    for id, imageinfo in images.items():
        mongohelper.update_obj_image(collection_name, int(id), imageinfo)


def main():
    os.environ['TZ'] = __config.time_zone
    time.tzset()

    current_time = "{:%Y-%m-%d-%H:%M:%S}".format(datetime.datetime.now())
    logfile = os.path.join(__config.log_folder, 'import-' + current_time + '.log')
    logging.basicConfig(filename=logfile, level=logging.DEBUG, format='[%(asctime)s]-[%(levelname)s]: %(message)s')
    date = None
    if len(sys.argv) <= 1 or sys.argv[1] != 'fullUpdate':
        date = get_last_update_date(__config.timestamp_file)
    else:
        date = datetime.datetime(2000, 1, 1, 0, 0, 0)

    logging.info('Current date :' + str(date))
    logging.info('arguments: ' + str(sys.argv))
    import_player(date)
    import_progamer(date)
    import_map(date)
    import_event(date)
    import_task(date)


if __name__ == '__main__':
    main()