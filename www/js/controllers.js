angular.module("controllers", ["ionic", "sql"])

.controller("MainCtrl", function($scope) {
  console.log("Now in the Main Page");
})

.controller("LevelCtrl", function($scope, $rootScope, $state, $stateParams, $ionicPopup) {
  console.log("Now in level: " + $stateParams.levelNum);
  if(!$rootScope.levels.includes($stateParams.levelNum)) {
    console.log("Went to level " + $stateParams.levelNum + " redirecting to level select.");
    $state.go("level_select");
  }

  // Begin: The entirety of our "game". Shows a button, which when clicked
  // beats the level. It also shows "back" and "next" options.
  $scope.completeLevel = function() {
    completeLevel($stateParams.levelNum);
    var levelOverPopUp = $ionicPopup.show({
      title: "Level Complete!",
      scope: $scope,
      buttons: [
      {
        text: "Level Select",
        onTap: function(e) {
          console.log("Back to level select.");
          $state.go("level_select");
        }
      },
      {
        text: "Next",
        type: "button-positive",
        onTap: function(e) {
          console.log("On to the next level.");
    			$state.go("level", {"levelNum": $stateParams.levelNum+1});
        }
      }]
    }); // Don't lose track of the semicolons, this ends .show();
  }

  $scope.restart = function() {
    console.log("Restarting level...");
    $state.reload();
  }
})

.controller("LevelSelectCtrl", function($scope, $rootScope, $state) {
  console.log("Now in the Level Select");

  $scope.loadLevel = function(num) {
    console.log("Entering level " + num);
    $state.go("level", {"levelNum": num});
  }

  $scope.myTitle = "Template";
  $rootScope.levels = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20];

  setupSQL();

  for (var i in $rootScope.levels) {
    // Adds a level if it doesn't exist, e.g. the database
    // wasn't yet initialized.
    addLevel($rootScope.levels[i], "Unsolved");
  }

  for (var i in $rootScope.levels) {
    getLevelState($rootScope.levels[i], function(level) {
      console.log("Callback from getLevelState: ", level);
      if (level.state == "Solved") {
        button = document.getElementById("level_"+level.number);
        button.setAttribute("class", "button button-dark ng-binding");
      }
    });
  }
});

function completeLevel(number) {
  button = document.getElementById("level_"+number);
  button.setAttribute("class", "button button-dark ng-binding");

  setLevelState(number, "Solved");
}
