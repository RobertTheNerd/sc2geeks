from django.http import HttpResponse
from django.views.generic import View
from sc2lib import utils
from sc2lib import mongohelper

from sc2lib.sc2parser import parse_replay_json
from sc2lib.utils import save_temp_file
from sc2lib.ReplayManager import ReplayManager
import traceback
import json
import logging
from datetime import datetime

class ReplaySummaryView(View):
    def post(self, request):
        fileName = save_temp_file("/Users/robert/data/tmp", request.FILES['file'].name, request.FILES['file'])
        json = parse_replay_json(fileName)
        response = HttpResponse(json, content_type='application/json')
        return response


class ReplayDetailView(View):
    def post(self, request):
        temp_folder = "d:\\temp"
        #temp_folder = "/Users/robert/data/tmp"
        fileName = save_temp_file(temp_folder, request.FILES['file'].name, request.FILES['file'])
        json = parse_replay_json(fileName, False)
        response = HttpResponse(json, content_type='application/json')
        return response


class ReplayUploadView(View):
    def post(self, request):
        try:
            res = ReplayManager.process_task(request.POST['taskId'], request.POST['fileName'], request.FILES['file'])
            response = HttpResponse('{"msg": "' + res + '"}', content_type='application/json')
            return response
        except:
            traceback.print_exc()


class AppCheckView(View):
    def get(self, request):
        return HttpResponse('App is running', content_type='text/plain')

def get_request_json_obj(request):
    return json.loads(str(request.body, 'utf-8'))

class MongoObject(View):
    def post(self, request):
        try:
            logging.info('Accepting request to save mongo object')
            post_type = request.POST['post_type']
            object = json.loads(request.POST['object'])
            object['last_edit_date'] = datetime.now()
            mongohelper.save_object(post_type, object)
            return HttpResponse('OK')
        except:
            logging.error(traceback.format_exc())

    def delete(self, request):
        try:
            logging.info('Accepting request to remove mongo object')
            post_type = request.GET['post_type']
            post_id = int(request.GET['id'])
            mongohelper.delete_object(post_type, post_id)
            return HttpResponse('OK')
        except:
            logging.error(traceback.format_exc())
