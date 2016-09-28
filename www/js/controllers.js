var app = angular.module("controllers", ["ionic", "sql"])

app.controller("MainCtrl", function($scope) {
  console.log("Now in the Main Page");
})

.controller("LevelCtrl", function($scope, $rootScope, $state, $stateParams, $ionicPopup, sqlfactory) {
  console.log($stateParams.levelNum);

  //Creates a popup when the level is complete.
  $scope.completeLevel = function() {
    completeLevel($stateParams.levelNum, sqlfactory);

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
					if($rootScope.levels.includes($stateParams.levelNum+1)) {
						$state.go("level", {"levelNum": $stateParams.levelNum+1});
					} else {
						$state.go("level_select");
					}
        }
        }]
    });
  }

  //Restarts the level.
  $scope.restart = function() {
    console.log("Restarts level.");
    $state.reload();
  }
})

.controller("SettingsCtrl", function($scope, $rootScope, sqlfactory) {
	console.log("Settings controller");
	$scope.resetSQL = function() {
		sqlfactory.resetSQL();
		sqlfactory.setupSQL();
	}
	console.log($scope);
})

.controller("LevelSelectCtrl", function($scope, $rootScope, $state, sqlfactory) {
  console.log("Now in the Level Select");

  $scope.loadLevel = function(levelNum) {
    console.log("Entering level " + levelNum);
    $state.go("level", {"levelNum": levelNum});
  }

  $scope.myTitle = "Template";
  $rootScope.levels = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20];

  sqlfactory.setupSQL();

  for (var i in $rootScope.levels) {
    // Adds a level if it doesn't exist, e.g. the database
    // wasn't yet initialized.
    sqlfactory.addLevel($rootScope.levels[i], "Unsolved");
  }

  for (var i in $rootScope.levels) {
    sqlfactory.getLevelState($rootScope.levels[i], function(level) {
      console.log("Callback from getLevelState: ", level);
      if (level.state == "Solved") {
        button = document.getElementById("level_"+level.number);
        button.setAttribute("class", "button button-dark ng-binding");
      }
    });
  }
});

function completeLevel(number, sqlfactory) {
  button = document.getElementById("level_"+number);
  button.setAttribute("class", "button button-dark ng-binding");

  sqlfactory.setLevelState(number, "Solved");
}
