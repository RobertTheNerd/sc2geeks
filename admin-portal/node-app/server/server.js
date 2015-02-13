
/**
 * Module dependencies.
 */

var express = require('express')
  , route = require('./routes')
  , api = require('./routes/api')
  , bodyParser = require('body-parser')
  , auth = require('./middleware/auth')
  ;

var app = module.exports = express();

app.use(auth.apiAuth);
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
app.set('views', __dirname + '/views');
app.set('view engine', 'jade');
app.use(express.static(__dirname + '/../content'));
app.use(express.static(__dirname + '/../'));

var router = express.Router();

// Routes
router.get('/api/test', api.test);

router.get('/api/event/:event_id', api.getEvent);
router.get('/api/eventStats/:event_id', api.getEventStatsById);
router.get('/api/eventStats', api.getEventStats);
router.post('/api/eventStats', api.updateProgamersForEventAllDone);
router.get('/api/eventPlayers', api.getEventPlayers);
router.get('/api/progamer', api.getProgamers);
router.post('/api/progamer/eventPlayer', api.assignProgamerForEventPlayer);
router.delete('/api/progamer/event/:event_id/player/:player_url', api.deleteProgamerForEventPlayer);
router.get('/', route.index);
app.use('/', router);

app.listen(8600, function () {
  console.log("API server running on port %d in %s mode", this.address().port, app.settings.env);
});
