__author__ = 'robert'

import os
import json

#TODO: make singleton
class Config:
    def __init__(self, filename=None):
        if filename is None:
            self.log_folder = "/Users/robert/data/replays/log"
            self.time_zone = "America/Los_Angeles"
        else:
            module_file = os.path.abspath(__file__)
            folder = os.path.dirname(module_file)
            config_file = os.path.join(folder, filename)
            with open(config_file) as cfg_file:
                # data = cfg_file.read()
                self.__dict__ = json.load(cfg_file)