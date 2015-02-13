(function(){
  var MongoClient = require('mongodb').MongoClient;
  var dbUrl = "mongodb://localhost:27017/sc2";
  var utils = require('../commons/utils');
  var async = require('async');

  exports.getEventStats = function(event_name, page_size, page_no, filter_all_assigned, callback) {
    MongoClient.connect(dbUrl, function(err, db){
      if (err) {
        callback(err, null);
        return;
      }

      var collection = db.collection('event_stats');
      // TODO: add event name handling logic
      var query = filter_all_assigned === 'true' ? {all_progamers_assigned: false} : {};

      // get count first
      collection.find(query).count(function(err, count){
        if (err) {
          callback(err, null, 0);
          return;
        }
        collection.find(query, {skip: utils.get_start_index(count, page_size, page_no), limit: page_size, sort: {event_id: 1}})
          .toArray(function(err, events){
            if (err) {
              callback(err, null, 0);
            }
            async.each(events, function(event, each_callback){
              var event_collection = db.collection('event');
              event_collection.findOne({_id: event.event_id}, function(err, res){
                if (err) {
                  each_callback(err);
                }
                if (res) {
                  event.event_name = res.name;
                }

                each_callback();
              });
            }, function(){
              callback(null, events, count);
            });
          });
      });
    });
  };

  exports.getEventStatsById = function(event_id, callback) {
    get_one_mongo_obj('event_stats', {event_id: event_id}, callback);
  };

  exports.updateProgamersForEventAllDone = function(event_id, all_done, callback) {
    all_done = String(all_done) == 'true';
    MongoClient.connect(dbUrl, function(err, db){
      if (err) {
        callback(err, null);
        return;
      }

      // search the progamers
      var collection = db.collection('event_stats');
      collection.update({event_id: event_id},
        {$set: {all_progamers_assigned: all_done}},
        callback);
    });
  };

  exports.getEventPlayers = function(event_id, page_size, page_no, filter_progamers, callback) {
    event_id = parseInt(event_id, 10);
    MongoClient.connect(dbUrl, function(err, db){
      if (err) {
        callback(err, null, 0);
        return;
      }

      // get count first
      var player_collection = db.collection('event_players');
      var query = {event_id: event_id};
      if (filter_progamers === 'true') {
        query.progamer_id = {$exists: false};
      }
      player_collection.find(query).count(function(err, count){
        if (err) {
          callback(err, null, 0);
          return;
        }
        // then get all the players
        player_collection.find(query, {skip: utils.get_start_index(count, page_size, page_no), limit: page_size, sort: {player_name: 1}})
          .toArray(function(err, players){
            if (err) {
              callback(err, null, 0);
            }
            // populate progamer names
            async.each(players, function(player, each_callback){
              // only do when progamer is assigned
              if (typeof(player.progamer_id) === 'undefined') {
                each_callback();
                return;
              }
              get_mongo_obj_by_id('progamer', player.progamer_id, function(err, progamer) {
                if (err) {
                  each_callback(err);
                  return;
                }
                player.progamer_name = progamer.name;
                each_callback();
              });
            }, function(err){
                callback(err, players, count);
            });
          });
      });

    });
  };

  exports.searchProgamers = function(search_term, callback){
    MongoClient.connect(dbUrl, function(err, db){
      if (err) {
        callback(err, null);
        return;
      }

      // search the progamers
      var player_collection = db.collection('player');
      player_collection.find({name: {$regex: '.*' + search_term + '.*', $options: 'i'}}).
        toArray(function(err, players){
          if (err) {
            callback(err, null);
            return;
          }

          callback(null, players);
        });
    });
  };

  exports.getAllProgamers = function(callback) {
    MongoClient.connect(dbUrl, function(err, db){
      if (err) {
        callback(err, null);
        return;
      }

      // search the progamers
      var progamer_collection = db.collection('progamer');
      progamer_collection.find({}, {sort: {name: 1}, fields: {name:1}}).
        toArray(function(err, progamers){
          if (err) {
            callback(err, null);
            return;
          }

          callback(null, progamers);
        });
    });
  };

  exports.getEvent = function(event_id, callback){
    get_mongo_obj_by_id('event', event_id, callback);
  };

  exports.assignProgamerForEventPlayer = function(event_id, player_url, progamer_id, callback) {
    event_id = parseInt(event_id, 10);
    progamer_id = parseInt(progamer_id, 10);
    MongoClient.connect(dbUrl, function(err, db){
      if (err) {
        callback(err, null);
        return;
      }

      // search the progamers
      var collection = db.collection('event_players');
      collection.update({event_id: event_id, player_url: player_url},
        {$set: {progamer_id: progamer_id}},
        callback);
    });
  };

  exports.deleteProgamerForEventPlayer = function(event_id, player_url, callback) {
    event_id = parseInt(event_id, 10);
    MongoClient.connect(dbUrl, function(err, db){
      if (err) {
        callback(err, null);
        return;
      }

      // search the progamers
      var collection = db.collection('event_players');
      collection.update({event_id: event_id, player_url: player_url},
        {$unset: {progamer_id: ''}},
        callback);
    });
  };

  function get_mongo_obj_by_id(collection_name, id, callback) {
    id = parseInt(id, 10);
    get_one_mongo_obj(collection_name, {_id: id}, callback);
  }

  function get_one_mongo_obj(collection_name, query, callback) {
    MongoClient.connect(dbUrl, function(err, db){
      if (err) {
        callback(err, null);
        return;
      }

      // get object by _id
      var collection = db.collection(collection_name);
      collection.findOne(query, function(err, obj){
        if (err) {
          callback(err, null);
          return;
        }
        callback(null, obj);
      });
    });
  }

}());

