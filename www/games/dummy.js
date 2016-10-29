angular.module("dummy", ["ionic", "sql"])

.controller("LevelCtrl", function ($scope, $rootScope, $state, $stateParams, $ionicPopup, sqlfactory) {
  // Check for invalid state
  if ($rootScope.levels === undefined) {
    console.log("Level loaded but level list undefined, going to main")
    $state.go("main");
  } else if (!$rootScope.levels.includes($stateParams.levelNum)) {
    console.log("Went to level " + $stateParams.levelNum + " redirecting to level select.");
    $state.go("level_select");
  } else {
    console.log("Now in level: " + $stateParams.levelNum);
  }

  // Redefined so that it can be used in the HTML.
  $scope.levelNum = $stateParams.levelNum;

  // Begin: The entirety of our "game". Shows a button, which when clicked
  // beats the level. It also shows "back" and "next" options.
  $scope.completeLevel = function () {
    button = document.getElementById("level_"+$stateParams.levelNum);
    button.setAttribute("class", "button button-dark ng-binding");

    sqlfactory.setLevelState($stateParams.levelNum, "Solved");

    $ionicPopup.show({
      title: "Level Complete!",
      scope: $scope,
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
          $state.go("level", {"levelNum": $stateParams.levelNum+1});
        }
      }]
    });
  }
  // End: The entirety of our "game".

  $scope.restart = function () {
    console.log("Restarting level...");
    $state.reload();
  }
});
