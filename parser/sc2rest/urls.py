from django.conf.urls import patterns, include, url

from django.contrib import admin
from .views import *

admin.autodiscover()

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'sc2rest.views.home', name='home'),
    # url(r'^blog/', include('blog.urls')),
    url(r'^replay/summary/', ReplaySummaryView.as_view(), ),
    url(r'^replay/detail/', ReplayDetailView.as_view(), ),
    url(r'^replay/upload/', ReplayUploadView.as_view(), ),
    url(r'^api/mongoobj', MongoObject.as_view(), ),
    url(r'^test/', AppCheckView.as_view(), ),

    url(r'^admin/', include(admin.site.urls)),
)
