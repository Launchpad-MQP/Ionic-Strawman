@(contents:String)

angular.module("game", ["ionic", "sql"])

.controller("LevelCtrl", function ($scope, $rootScope, $state, $stateParams, sqlfactory) {
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

  @contents

  $scope.completeLevel = function () {
    $rootScope.completeLevel($state, $stateParams.levelNum);
  }

  $scope.restart = function () {
    console.log("Restarting level...");
    $state.reload();
  }
});
