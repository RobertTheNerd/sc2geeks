
var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var s = String;
var n = Number;

var mapSchema = new Schema({
  _id         : n,
  hash        : s,
  url         : s,
  server      : s,
  name        : s
});

var Map = moogose.model('map', mapSchema);