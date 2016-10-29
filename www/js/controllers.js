/**
 * This file holds all the per "page" javascript functions.
 * Each of these should be (mostly) scoped to the page they operate on.
**/

angular.module("controllers", ["ionic", "sql"])

/* Controller for the main page */
.controller("MainCtrl", function ($scope) {
  console.log("Now in the Main Page");
})

/* Controller for the settings page. */
.controller("SettingsCtrl", function ($scope, $rootScope, sqlfactory) {
  console.log("Now in the Settings page");
  // Defining a local function so it can be used by ng-click
  $scope.resetSQL = function () {
    sqlfactory.resetSQL();
    sqlfactory.setupSQL();
    location.reload();
  }
})

/* Controller for the level select, aka list of levels */
.controller("LevelSelectCtrl", function ($scope, $rootScope, $state, sqlfactory) {
  console.log("Now in the Level Select");

  $scope.loadLevel = function (num) {
    console.log("Entering level " + num);
    $state.go("level", {"levelNum": num});
  }

  // Globally defined list of levels.
  $rootScope.levels = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
    11, 12, 13, 14, 15, 16, 17, 18, 19, 20];

  // Creates the database if it doesn't exist.
  sqlfactory.setupSQL();

  for (var i in $rootScope.levels) {
    // Adds a level if it doesn't exist, e.g. the database was just created.
    // Otherwise, this line has no effect, i.e. the level retains its state.
    sqlfactory.addLevel($rootScope.levels[i], "Unsolved");
  }

  // For each level, if it is completed we need to recolor the button.
  // This uses a callback function, since talking to SQL is an async operation.
  for (var i in $rootScope.levels) {
    sqlfactory.getLevelState($rootScope.levels[i], function (level) {
      console.log("Callback from getLevelState: ", level);
      if (level.state == "Solved") {
        button = document.getElementById("level_"+level.number);
        button.setAttribute("class", "button button-dark ng-binding");
      }
    });
  }
});
