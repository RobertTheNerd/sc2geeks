__author__ = 'robert'
import os
from sc2reader.utils import JSONDateEncoder
import json
import uuid
import zipfile
import logging
import traceback
import shutil
import requests


def save_temp_file(folder, originalFileName, ms, prefix=None):
    if prefix is None:
        for i in range(100):
            fileName = os.path.join(folder, str(uuid.uuid4()) + "-" + originalFileName)
            if os.path.isfile(fileName) and os.path.exists(fileName):
                continue
            break
    else:
        fileName = os.path.join(folder, str(prefix) + '-' + originalFileName)

    # create folder if not exist
    ensure_folder(folder)

    with open(fileName, 'wb') as file:
        for chunk in ms.chunks():
            file.write(chunk)
    return fileName


def to_json(obj):
    return json.dumps(obj, cls=JSONDateEncoder)


def ensure_folder(folder):
    if not os.path.exists(folder):
        os.makedirs(folder)


def move_file(source, target_folder, source_prefix=None):
    """
    Move a file to the target folder with the option of keeping folder structure.
    :param source:
    :param target_folder:
    :param source_prefix:
        if source_prefix is None or source is not prefixed with source_prefix, the file itself will be moved to the target folder. File name remains the same.
        Otherwise, the prefix will be strip off from the source file, the remainder of the path will be appended to the
        target folder.
    :return: the target file name it's successfully moved. None otherwise.

    """
    try:
        if source_prefix is not None and source.startswith(source_prefix):
            target = source.replace(source_prefix, '')
            target = os.path.join(target_folder, target.lstrip('/'))
        else:
            (folder, file) = os.path.split(source)
            target = os.path.join(target_folder, file)

        # make sure the target file is deleted as otherwise shutil.move will throw an error.
        if os.path.exists(target):
            os.remove(target)

        # yes, folder needs to be created prior to moving the file
        (folder, file) = os.path.split(target)
        ensure_folder(folder)

        shutil.move(source, target)
        return target

    except:
        logging.error("Failed to move file" + traceback.format_exc())

    return None

def decompress(filename, targetFolder):
    name, ext = os.path.splitext(filename)
    ext = ext.lower()
    if ext == '.zip':
        return unzip(filename, targetFolder)


def unzip(filename, target_folder):
    """

    :param filename:
    :param target_folder:
    :return:
        None - if error occurs
        a list of full paths for the actual files. Folders are excluded.
    """
    try:
        logging.info('Extracting zip file %s to folder %s' % (filename, target_folder))
        zip = zipfile.ZipFile(filename)
        ensure_folder(target_folder)
        zip.extractall(target_folder)
        return get_all_files(target_folder, '.sc2replay')
    except:
        logging.error('Failed to unzip file. ' + traceback.format_exc())
        return None


def get_all_files(folder, suffix):
    file_names = []
    try:
        for root, dirnames, filenames in os.walk(folder):
            for filename in filenames:
                if filename.lower().endswith(suffix.lower()):
                    file_names.append(os.path.join(root, filename))
    except:
        logging.info('Error while getting all files for folder %s ' % folder)
        logging.info(traceback.format_exc())
    return file_names


def save_via_rest(url, data, debug_php=False):
    jdata = to_json(data)
    try:
        response = post_json_request(url, jdata, debug_php)
        if response.status_code == 200 or response.status_code == 201:
            data['id'] = response.json()['id']
            if response.status_code == 200:
                logging.info('Updated the existing one.')
            if response.status_code == 201:
                logging.info('New one was created.')
            return True
    except:
        logging.error("Failed to post to url:" + url);
        logging.error(traceback.format_exc())

    return False


def post_json_request(url, json, debug_php=False):
    try:
        headers = {
            'Content-Type': 'application/json',
        }
        if debug_php:
            headers['Cookie'] = 'XDEBUG_SESSION=PHPSTORM'
        return requests.post(url, data=json, headers=headers)
    except:
        logging.error("Failed to post to url:" + url);
        logging.error(traceback.format_exc())
        return None

def main():
    #move_file('/Users/robert/data/b.json', '/Users/robert/data/replays', '/Users')
    files = unzip('/Users/robert/Documents/sc2geeks/replaypack/WCS/2014 WCS AM S2.zip', '/Users/robert/data/replays/tmp/1385-2014 WCS AM S2')
    for file in files:
        print(file)

    logging.info('done')


if __name__ == '__main__':
    main()