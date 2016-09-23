// Ionic Starter App

var db = undefined;
var apidb = undefined;

// angular.module is a global place for creating, registering and retrieving Angular modules
// "starter" is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of "requires"
angular.module("starter", [
  "ionic", /* Base include for ionic */
  "states", /* State transitions between pages */
  "controllers", /* Individual page js */
  "ngCordova" /* Used for Cordova-SQLite */
])

.run(function($ionicPlatform, $cordovaSQLite, $timeout) {
  $ionicPlatform.ready(function() {
    // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
    // for form inputs)
    if (window.cordova && window.cordova.plugins && window.cordova.plugins.Keyboard) {
      cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
      cordova.plugins.Keyboard.disableScroll(true);
    }
    if (window.StatusBar) {
      // org.apache.cordova.statusbar required
      StatusBar.styleDefault();
    }

    if (window.cordova) {
      db = $cordovaSQLite.openDB("test.db");
    } else {
      db = window.openDatabase("mytest.db", "1", "mytest", 1024*1024*5);
    }

    // set up the apidb for future.
    apidb = $cordovaSQLite;
    setup();

  });
})

function setup() {
  console.log("Creating blank table if nonexistent");
  apidb.execute(db, "CREATE TABLE IF NOT EXISTS games (level, number INTEGER, description VARCHAR(50))");
}

function reset() {
  console.log("Dropping tables...");
  apidb.execute(db, "DROP TABLE IF EXISTS games");
  setup();
}

function addLevel(num) {
  recordMove(num, "13", "Unsolved");
}

function recordMove(gameid, moveid, moveinfo) {
  console.log ("Inserting " + gameid + "," + moveid + "," + moveinfo);
  apidb.execute(db, "INSERT INTO games(level,number,description) VALUES(?,?,?)", [gameid, moveid, moveinfo])
  .then(function(ret) {
    console.log("Inserted");
    for (var i in ret) {
      console.log("\t"+i+": ", ret[i]);
    }
  }, function (err) {console.log(err);});
}

function setLevelState(num) {
  deleteLevel(num);
  recordMove(num, "14", "Solved");
}

function deleteLevel(num) {
  console.log("Deleting level: "+num);

  apidb.execute(db, "DELETE FROM games WHERE level=? AND number=13", [num])
  .then(function(ret) {
    console.log("Deleted level: "+num);
    for (var i in ret) {
      try {
        console.log("\t"+i+": ", ret[i]);
      } catch (exc) {}
    }
  }, function (err) {console.log(err);});
  }

function getLevelState(num, callback) {
  console.log("Getting state for level: "+num);

  apidb.execute(db, "SELECT * FROM games WHERE level=?", [num])
  .then(function(ret) {
    if (ret.rows.length == 0) {
      console.log("Couldn"t find level: "+num);
      return;
    }
    callback(ret.rows.item(0));
  }, function(err) {console.log(err);});
}
