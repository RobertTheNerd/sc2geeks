/**
 * Created by robert on 10/3/14.
 */
// Retrieve
var MongoClient = require('mongodb').MongoClient;

// Connect to the db
MongoClient.connect("mongodb://localhost:27017/sc2", function(err, db) {
  if(err) {
    console.log("Error");
    return;
  }
  var collection = db.collection('map');
  collection.findOne({_id:145}, function(err, item) {
    console.log(item);
  });
});

