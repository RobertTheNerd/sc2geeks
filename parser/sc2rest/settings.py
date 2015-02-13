"""
Django settings for sc2rest project.

For more information on this file, see
https://docs.djangoproject.com/en/1.6/topics/settings/

For the full list of settings and their values, see
https://docs.djangoproject.com/en/1.6/ref/settings/
"""

# Build paths inside the project like this: os.path.join(BASE_DIR, ...)
import os
import time
BASE_DIR = os.path.dirname(os.path.dirname(__file__))


# Quick-start development settings - unsuitable for production
# See https://docs.djangoproject.com/en/1.6/howto/deployment/checklist/

# SECURITY WARNING: keep the secret key used in production secret!
SECRET_KEY = '=7@f$6$5y#nwtn1i=9xck6+o!w&tk7-f_bdc51l^gip74f!c(s'

# SECURITY WARNING: don't run with debug turned on in production!
DEBUG = True

TEMPLATE_DEBUG = True

ALLOWED_HOSTS = []


# Application definition

INSTALLED_APPS = (
    'django.contrib.admin',
    'django.contrib.auth',
    'django.contrib.contenttypes',
    'django.contrib.sessions',
    'django.contrib.messages',
    'django.contrib.staticfiles',
)

MIDDLEWARE_CLASSES = (
    'django.contrib.sessions.middleware.SessionMiddleware',
    'django.middleware.common.CommonMiddleware',
    'django.middleware.csrf.CsrfViewMiddleware',
    'django.contrib.auth.middleware.AuthenticationMiddleware',
    'django.contrib.messages.middleware.MessageMiddleware',
    'django.middleware.clickjacking.XFrameOptionsMiddleware',
    'sc2rest.disable.DisableCSRF',

)

ROOT_URLCONF = 'sc2rest.urls'

WSGI_APPLICATION = 'sc2rest.wsgi.application'


# Database
# https://docs.djangoproject.com/en/1.6/ref/settings/#databases

DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.sqlite3',
        'NAME': os.path.join(BASE_DIR, 'db.sqlite3'),
    }
}

# Internationalization
# https://docs.djangoproject.com/en/1.6/topics/i18n/

LANGUAGE_CODE = 'en-us'

#TIME_ZONE = 'PST'
os.environ['TZ']="America/Los_Angeles"
time.tzset()

USE_I18N = True

USE_L10N = True

USE_TZ = True


# Static files (CSS, JavaScript, Images)
# https://docs.djangoproject.com/en/1.6/howto/static-files/

STATIC_URL = '/static/'

TEMPLATE_DIRS = (
    os.path.join(BASE_DIR, 'templates'),
)

# Setup logging
from .Config import Config
import logging
import datetime

__config = Config("config.json")
os.environ['TZ'] = __config.time_zone
time.tzset()
current_time = "{:%Y-%m-%d-%H:%M:%S}".format(datetime.datetime.now())
logfile = os.path.join(__config.log_folder, 'parser.sc2geeks.com-' + current_time + '.log')
logging.basicConfig(filename=logfile, level=logging.DEBUG,
                    format='[%(asctime)s]-[%(module)s]-[%(levelname)s]: %(message)s',
                    datefmt="%H:%M:%S")
logging.info("Logger initialized.")
