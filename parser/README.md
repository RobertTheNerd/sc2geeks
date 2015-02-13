# SC2Geeks - sc2-python

This is a python library/utility to parse Starcraft II replays and save the result in MongoDB and wordpress (via API) that are used by [SC2Geeks](http://www.sc2geeks.com). A lot of thanks go to [GraylinKim's great work](https://github.com/GraylinKim/sc2reader).

[SC2Geeks](http://www.sc2geeks.com) is a hobby project of RobertTheNerd. See [Annoucement](http://www.teamliquid.net/forum/starcraft-2/470736-sc2geekscom-yet-another-starcraft-ii-replay-site).

## Components of SC2Geeks
[SC2Geeks](http://www.sc2geeks.com) is under development and includes the following components.
* [Replay parsing utility](https://github.com/RobertTheNerd/sc2-python). This is deployed as REST service and parses a given replay file and persists in MongoDB and Wordpress database (MySQL).
* [Wordpress portal](https://github.com/RobertTheNerd/wp-sc2). This is a portal that allows replay owners to better organize replays into tournaments and to maintain information that are not available in replay files like the actual progamer instead of the player ID used. I'm adopting this approach to fully utilize the backend function of Wordpress and save time implementing UI intensive maintenance tools. This project provides functions and APIs to sync data with MongoDB.
* Solr instance. Provides replay & progamer data for the website. Will be githubed soon.
* Front-end website. Java application (Struts 2). Source code hosted somewhere else and will be githubed soon.
