#SC2Geeks
[SC2Geeks.com](http://www.sc2geeks.com) is a website that parses Starcraft II replays (publicly available) and provides powerful interface to show game stats and to find replays of specific criteria.

## Components/Projects of SC2Geeks
* [Front-end website](http://www.sc2geeks.com). `/web-api`. Java application. Website and API service are implemented using Struts 2. Also includes some other utilities like a pro-gamer crawlers and a Solr importer.
* **Replay parsing lib and service**. `/parser`. Python application powered by [sc2reader](https://github.com/GraylinKim/sc2reader). This is deployed as a REST service and invoked by the admin site. It parses replay files and persists the result in MongoDB and Wordpress database (MySQL) which is then indexed into Solr.
* [Back-end portal](http://admin.sc2geeks.com). `/admin-portal`. Consists of a Wordpress theme in PHP and a Node.js application. Admin portal is a wordpress site that maintains the taxonomy of sc2geeks.com (maps, tournaments, pro-gamers, etc). 
* **Solr instance**. `/solr`. There are two instances, namely replays and pro-gamers, that power the no-sql data-service for the front-end and api service.
* Template project. `/template`. Grunt project for authoring the template for the website.

