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
  apidb.execute(db, "CREATE TABLE IF NOT EXISTS levels (number, state VARCHAR(50))");
}

function reset() {
  console.log("Dropping tables...");
  apidb.execute(db, "DROP TABLE IF EXISTS levels");
  setup();
}

function addLevel(num) {
  setLevelState2(num, "13", "Unsolved");
}

function setLevelState2(num, _, state) {
  console.log ("Setting level "+num+" to "+state);
  apidb.execute(db, "INSERT INTO levels (number, state) VALUES (?, ?)", [num, state])
  .then(function(ret) {
    console.log("Set level "+num+" to "+state);
    for (var i in ret) {
      console.log("\t"+i+": ", ret[i]);
    }
  }, function (err) {console.log(err);});
}

function setLevelState(num, state) {
  deleteLevel(num);
  setLevelState2(num, "14", state);
}

function deleteLevel(num) {
  console.log("Deleting level: "+num);

  apidb.execute(db, "DELETE FROM levels WHERE number=?", [num])
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

  apidb.execute(db, "SELECT * FROM levels WHERE number=?", [num])
  .then(function(ret) {
    if (ret.rows.length == 0) {
      console.log("Could not find level: "+num);
      return;
    }
    callback(ret.rows.item(0));
  }, function(err) {console.log(err);});
}
