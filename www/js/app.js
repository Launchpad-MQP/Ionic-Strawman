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
      db = window.openDatabase("mytest.db", '1', 'mytest', 1024*1024*5);
    }

    // set up the apidb for future.
    apidb = $cordovaSQLite;
    timeout = $timeout;
    // deleteTable();
    createTable();
    if (getLevelState(1) == null) { // not working because async and my head hurts
      addLevel(1, "Unsolved");
    } else {
      setLevelState(1, "In Progress");
    }
    var out = getLevelState(1);
    console.log(out);
    setLevelState(1, "Solved");
  });
})

function deleteTable() {
  console.log("Hard reset: Dropping tables");
  apidb.execute(db, "DROP TABLE IF EXISTS levels");
}

function createTable() {
  console.log("Creating tables")
  apidb.execute(db, "CREATE TABLE IF NOT EXISTS levels (num INTEGER, state VARCHAR(20))")
  .then(timeout, (function(ret) {
    for (var i in ret) {
      console.log(i+": ", ret);
    }
  }, function (err) {console.log(error);}), 2000);
}

function addLevel(num, state) {
  console.log("Added level "+num+" at state "+state);
  apidb.execute(db, "INSERT INTO levels (num, state) VALUES (?, ?)", [num, state])
}

function deleteLevel(num) {
  console.log("Deleting level "+num);
  apidb.execute(db, "DELETE FROM levels WHERE num=?", [num])
}

function getLevelState(num) {
  console.log("Getting state for level "+num);
  apidb.execute(db, "SELECT * FROM levels WHERE num=?", [num])
  .then(function(ret) {
    if (ret.rows.length == 0) {
      console.log("Got no data for level "+num);
      return null;
    }
    var level = ret.rows.item(0);
    console.log("Data for level "+num+": ", level);
    // return level.state;
  }, function (err) {console.log(error);});
}

function setLevelState(num, state) {
  console.log("Setting level "+num+" to state "+state);
  apidb.execute(db, "UPDATE levels SET state=? WHERE num=?", [state, num]);
}
