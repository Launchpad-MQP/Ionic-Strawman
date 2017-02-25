@(levelList:Array[String])
/**
* This file holds all the per "page" javascript functions.
* Each of these should be (mostly) scoped to the page they operate on.
**/

angular.module("controllers", ["ionic", "sql", "dictionary"])

/* Controller for the main page */
.controller("MainCtrl", function ($scope) {
  console.log("Now in the Main Page")
})

/* Controller for the settings page. */
.controller("SettingsCtrl", function ($scope, $rootScope, sqlfactory) {
  console.log("Now in the Settings page")
  // Defining a local function so it can be used by ng-click
  $scope.resetSQL = function () {
    sqlfactory.resetSQL()
    sqlfactory.setupSQL()
    location.reload()
  }
})

/* Controller for the level select, aka list of levels */
.controller("LevelSelectCtrl", function ($scope, $rootScope, $state, $ionicPopup, sqlfactory, dictionary) {
  console.log("Now in the Level Select")
  $scope.loadLevel = function (name) {
    console.log("Entering " + name)
    for (var i=0; i<$rootScope.levelData.length; i++) {
      if ($rootScope.levelData[i]["name"] == name) {
        $state.go("level", {"levelNum": i})
        break
      }
    }
  }
  // Creates the database if it doesn't exist.
  sqlfactory.setupSQL()
  // Init the dictionary into memory
  dictionary.setup()
  // Redefined so it can be acessed in HTML
  $scope.levelData = $rootScope.levelData

  var levelList = [@for(level <- levelList) {"@level", }]

  for (var i=0; i<levelList.length; i++) {
    // Adds a level if it doesn't exist, e.g. the database was just created.
    // Otherwise, this line has no effect, i.e. the level retains its state.
    sqlfactory.addLevel(i, levelList[i], "Unsolved", 0)
  }

  // For each level, if it is completed we need to recolor the button.
  // This uses a callback function, since talking to SQL is an async operation.
  for (var i=0; i<levelList.length; i++) {
    sqlfactory.getLevelState(i, function (level, num) {
      console.log("Callback from getLevelState: ", level, num)
      if (level.state == "Solved") {
        button = document.getElementById("level_" + num)
        button.setAttribute("class", "button button-dark ng-binding")
      }
      $rootScope.levelData[level.number]["time"] = level.time
      $rootScope.levelData[level.number]["state"] = level.state
    })
  }
})
