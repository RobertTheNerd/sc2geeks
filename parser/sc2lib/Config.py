__author__ = 'robert'

import os
import json

#TODO: make singleton
class Config:
    def __init__(self, filename=None):
        if filename is None:
            url_root = 'http://d.sc2geeks.com/wp-json'
            self.url_obj = url_root + '/sc2obj'
            self.url_map = url_root + '/map'
            self.url_player = url_root + '/player'
            self.url_upload_task = url_root + '/upload_task'
            self.mongodb_addr = 'localhost'
            self.debug_php = False
            self.logfolder = "/Users/robert/data/replays/log"
            self.replay_repo_folder = '/Users/robert/data/replays/repo'
            self.replay_working_folder = '/Users/robert/data/replays/tmp'
            self.replay_zip_folder = '/Users/robert/data/replays/zip'
            self.replay_failed_folder = '/Users/robert/data/replays/failed'
            self.sc2reader_cache_dir = '/Users/robert/data/cache'
            self.sc2reader_cache_max_entries = 5000
        else:
            module_file = os.path.abspath(__file__)
            folder = os.path.dirname(module_file)
            config_file = os.path.join(folder, filename)
            with open(config_file) as cfg_file:
                # data = cfg_file.read()
                self.__dict__ = json.load(cfg_file)