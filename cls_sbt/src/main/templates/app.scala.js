/**
 * The main javascript file. Contains global variable definitions, and loads
 * other javascript templates.
**/

// Initialize SQL database and database access so that they
// are available at global scope
var db = undefined;
var apidb = undefined;

// A list of other javascript files to include
angular.module("starter", [
  "ionic", /* Base include for ionic */
  "states", /* State transitions between pages */
  "controllers", /* Individual page js */
  "ngCordova", /* Used for Cordova-SQLite */
  "game" /* Our game */
])

// Runs when the app is fully loaded.
.run(function ($ionicPlatform, $cordovaSQLite) {
  $ionicPlatform.ready( function () {
    // When running on mobile, hide the accessory bar by default.
    if (window.cordova && window.cordova.plugins && window.cordova.plugins.Keyboard) {
      cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
      cordova.plugins.Keyboard.disableScroll(true);
    }
    if (window.StatusBar) {
      // org.apache.cordova.statusbar required
      StatusBar.styleDefault();
    }

    if (window.cordova) { // If true, running in an emulator.
      db = $cordovaSQLite.openDB("levels.db");
    } else { // If false, running in a browser.
      db = window.openDatabase("levels.db", "1", "levels", 10000000);
    }

    apidb = $cordovaSQLite;

  });
});
