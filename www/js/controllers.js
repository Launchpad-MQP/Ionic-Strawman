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
.controller("LevelSelectCtrl", function ($scope, $rootScope, $state, $ionicPopup, sqlfactory) {
  console.log("Now in the Level Select");

  $scope.loadLevel = function (num) {
    console.log("Entering level " + num);
    $state.go("level", {"levelNum": num});
  }

  // Globally defined list of levels.
  $rootScope.levels = [
     1,  2,  3,  4, 5,
     6,  7,  8,  9, 10,
    11, 12, 13, 14, 15,
    16, 17, 18, 19, 20
  ];

  // Global list of default states. Used for initialization and resets.
  $rootScope.defaultStates = [
    18123587584, 18119623680, 480887296, 8738341376, 315360000,
     2684356643,      524288,  34873344,  807600128, 137561088,
              0,           0,         0,          0,         0,
              0,           0,         0,          0,         0
  ];

  // Global list of current states. Used for the current session to reduce SQL
  $rootScope.states = [
    0, 0, 0, 0, 0,
    0, 0, 0, 0, 0,
    0, 0, 0, 0, 0,
    0, 0, 0, 0, 0
  ];

  // Creates the database if it doesn't exist.
  sqlfactory.setupSQL(
    init = function() {
      console.log("Initialize callback called");
      for (var i in $rootScope.levels) {
        // Adds a level if it doesn't exist, e.g. the database was just created.
        // Otherwise, this line has no effect, i.e. the level retains its state.
        sqlfactory.addLevel($rootScope.levels[i], $rootScope.defaultStates[i]);
        $rootScope.states[i] = $rootScope.defaultStates[i];
      }
    },
    load = function() {
      console.log("Load callback called");
      // For each level, if it is completed we need to recolor the button.
      // This uses a callback function, since talking to SQL is asynchronous
      for (var i in $rootScope.levels) {
        sqlfactory.getLevelState($rootScope.levels[i], function (level) {
          // Saved as a float for some reason
          level.state = Math.floor(level.state);
          console.log("Got state for level " + level.number + ":", level.state);
          $rootScope.states[level.number-1] = level.state;
          if (level.state == 0) {
            button = document.getElementById("level_"+level.number);
            button.setAttribute("class", "button button-dark ng-binding");
          }
        });
      }
    });

  $rootScope.completeLevel = function($state, levelNum) {
    button = document.getElementById("level_"+levelNum);
    button.setAttribute("class", "button button-dark ng-binding");
    // sqlfactory.setLevelState(levelNum, "Solved");

    $ionicPopup.show({
      title: "Level Complete!",
      buttons: [
      {
        text: "Level Select",
        onTap: function () {
          console.log("Back to level select.");
          $state.go("level_select");
        }
      },
      {
        text: "Next",
        type: "button-positive",
        onTap: function () {
          console.log("On to the next level.");
          $state.go("level", {"levelNum": levelNum+1});
        }
      }]
    });
  }
});
