var util = require('util');
var sc2 = require('../models/sc2');
var auth = require('../middleware/auth');


exports.getEvent = function(req, res) {
  sc2.getEvent(req.params.event_id, function(err, event) {
    if (err) {
      res.status(400);
      res.json({error: err});
      res.end();
      return;
    }
    res.json(event);
  });
};

exports.getEventStats = function(req, res) {
  sc2.getEventStats('', req.query.page_size, req.query.page_no, req.query.filter_all_assigned, function(err, events, total_count) {
    if (err) {
      res.status(400);
      res.json({err: err});
      res.end();
      return;
    }
    res.json({total_count: total_count, events: events})
    console.log(events, total_count);
  });
};

exports.updateProgamersForEventAllDone = function (req, res) {
  var event_id = parseInt(req.body.event_id, 10);
  sc2.updateProgamersForEventAllDone(event_id, req.body.all_done, function(err, count) {
    if (err) {
      res.status(400);
      res.json({err: err});
      res.end();
      return;
    }
    res.json({updated: count})
  });
};

exports.getEventStatsById = function(req, res) {
  sc2.getEventStatsById(parseInt(req.params.event_id, 10), function(err, eventStats) {
    if (err) {
      res.status(400);
      res.json({error: err});
      res.end();
      return;
    }
    res.json(eventStats)
  });
};

exports.getEventPlayers = function(req, res) {
  sc2.getEventPlayers(req.query.event_id, req.query.page_size, req.query.page_no, req.query.filter_progamers,
    function (err, event_players, total_count) {
      if (err) {
        res.json({err: err});
        return;
      }

      res.json({
        total_count: total_count,
        players: event_players
      });
    });
};

exports.getProgamers = function(req, res) {
  // search
  if (typeof(req.query.search_term) !== 'undefined') {
    var search_term = req.query.search_term ? req.query.search_term : '';
    sc2.searchProgamers(search_term, function(err, progamers) {
      if (err) {
        res.status(400);
        res.json({error: err});
        return;
      }

      res.json({progamers: progamers});
    })
  } else {
    sc2.getAllProgamers(function(err, progamers) {
      if (err) {
        res.status(400);
        res.json({error: err});
        return;
      }
      res.json(progamers);
    });
  }
};

exports.assignProgamerForEventPlayer = function (req, res) {
  sc2.assignProgamerForEventPlayer(req.body.event_id, req.body.player_url, req.body.progamer_id, function(err, result) {
    if (err) {
      res.status(400);
      res.json({error: err});
      res.end();
      return;
    }
    res.json({updated: result});
  });
};

exports.deleteProgamerForEventPlayer = function(req, res) {
  sc2.deleteProgamerForEventPlayer(req.params.event_id, req.params.player_url, function(err, result) {
    if (err) {
      res.status(400);
      res.json({error: err});
      res.end();
      return;
    }
    res.json({updated: result});
  });
};


exports.test = function(req, res) {
  res.send('hello, world');
  res.end();
};