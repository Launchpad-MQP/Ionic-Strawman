// Ionic Starter App

var db = undefined;
var apidb = undefined;

// angular.module is a global place for creating, registering and retrieving Angular modules
// "starter" is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of "requires"
angular.module("starter", [
  "ionic", /* Base include for ionic */
  "states", /* State transitions between pages */
  "sql", /* Persistent level storage */
  "controllers", /* Individual page js */
  "ngCordova" /* Used for Cordova-SQLite */
])

.run(function($ionicPlatform, $cordovaSQLite) {
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
      db = $cordovaSQLite.openDB("levels.db");
    } else {
      db = window.openDatabase("levels.db", "1", "levels", 1024*1024*5);
    }

    apidb = $cordovaSQLite;

  });
})
