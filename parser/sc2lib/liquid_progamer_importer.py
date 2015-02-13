import datetime
import requests
import time

from sc2lib.Config import Config
from sc2lib.utils import *
import sc2lib.mongohelper as mongohelper


__config = Config('config.json')


def post_json_request(url, object):
    json = to_json(object)
    try:
        headers = {
            'Content-Type': 'application/json',
        }
        return requests.post(url, data=json, headers=headers)
    except:
        logging.error(traceback.format_exc())
        return None


def save_progamer(progamer):
    logging.info('Saving progamer %s' % progamer['link'])
    response = post_json_request(__config.url_progamer, progamer)
    logging.info('Response code: %d, body: %s ' % (response.status_code, response.text,))
    pass


def main():
    os.environ['TZ'] = __config.time_zone
    time.tzset()

    current_time = "{:%Y-%m-%d-%H:%M:%S}".format(datetime.datetime.now())
    logfile = os.path.join(__config.log_folder, 'progamer-' + current_time + '.log')
    logging.basicConfig(filename=logfile, level=logging.DEBUG, format='[%(asctime)s]-[%(levelname)s]: %(message)s')
    logging.info("Starting saving progamer. Getting list.")
    progamers = mongohelper.get_liquid_progamers()
    if progamers is not None:
        logging.info("Total progamers found: %d" % len(progamers))
        for progamer in progamers:
            save_progamer(progamer)

    logging.info("Done.")


if __name__ == '__main__':
    main()