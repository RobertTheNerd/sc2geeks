# SC2Geeks - wp-sc2

This is a Wordpress theme that [SC2Geeks](http://www.sc2geeks.com) uses to maintain replay information like tournaments, progamers, etc.

[SC2Geeks](http://www.sc2geeks.com) is a hobby project of RobertTheNerd. See [Annoucement](http://www.teamliquid.net/forum/starcraft-2/470736-sc2geekscom-yet-another-starcraft-ii-replay-site).

## Components of SC2Geeks
[SC2Geeks](http://www.sc2geeks.com) is under development and includes the following components.
* [Replay parsing utility](https://github.com/RobertTheNerd/sc2-python). This is deployed as REST service and parses a given replay file and persists in MongoDB and Wordpress database (MySQL).
* [Wordpress portal](https://github.com/RobertTheNerd/wp-sc2). This is a portal that allows replay owners to better organize replays into tournaments and to maintain information that are not available in replay files like the actual progamer instead of the player ID used. I'm adopting this approach to fully utilize the backend function of Wordpress and save time implementing UI intensive maintenance tools. This project provides functions and APIs to sync data with MongoDB. Also includes:
  * [sc2-node](https://github.com/RobertTheNerd/sc2-node): a Node.js + AngularJs application that allows mapping the actual progamer to a replay. 
* Solr instance. Provides replay & progamer data for the website. Will be githubed soon.
* Front-end website. Java application (Struts 2). Source code hosted somewhere else and will be githubed soon.
