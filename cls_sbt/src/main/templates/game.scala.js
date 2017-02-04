@(contents:JavaScript)
angular.module("game", ["ionic", "sql"])

.controller("LevelCtrl", function ($scope, $rootScope, $ionicPopup, $state, $stateParams, sqlfactory) {
  // Check for invalid state
  if ($rootScope.levels === undefined) {
    console.log("Level loaded but level list undefined, going to main")
    $state.go("main")
  } else if (!($stateParams.levelNum in $rootScope.levels)) {
    console.log("Went to level " + $stateParams.levelNum + " redirecting to level select.")
    $state.go("level_select")
  } else {
    console.log("Now in level: " + $stateParams.levelNum)
  }

  // Redefined so that it can be used in the HTML.
  $scope.levelNum = $stateParams.levelNum

  @(contents)

  // This runs when the level is entered
  $scope.$on("$ionicView.afterEnter", function(scopes, states){
    console.log("Entered "+states.stateName+" "+$stateParams.levelNum+":", $rootScope.levels[$stateParams.levelNum])
    $rootScope.levels[$stateParams.levelNum].time -= Date.now()

    $scope.initializeLevel()
  });

  // This runs when the level is exited
  $scope.$on("$ionicView.beforeLeave", function(scopes, states){
    console.log("Exited "+states.stateName+" "+$stateParams.levelNum+":", $rootScope.levels[$stateParams.levelNum]);
    $rootScope.levels[$stateParams.levelNum].time += Date.now()
  });

  $scope.completeLevel = function() {
    button = document.getElementById("level_"+$stateParams.levelNum)
    button.setAttribute("class", "button button-dark ng-binding")
    sqlfactory.setLevelState($stateParams.levelNum, "Solved", 0)
    console.log($rootScope.levels)
    var time = $rootScope.levels[$stateParams.levelNum].time + Date.now()

    $ionicPopup.show({
      title: "Level Complete! Total time: " + time / 60 + " seconds",
      buttons: [
      {
        text: "Level Select",
        onTap: function () {
          console.log("Back to level select.")
          $state.go("level_select")
        }
      },
      {
        text: "Next",
        type: "button-positive",
        onTap: function () {
          console.log("On to the next level.")
          $state.go("level", {"levelNum": $stateParams.levelNum+1})
        }
      }]
    })
  }

  $scope.loseLevel = function() {
    $ionicPopup.show({
      title: "You lose!",
      buttons: [
      {
        text: "Level Select",
        onTap: function () {
          console.log("Back to level select.")
          $state.go("level_select")
        }
      },
      {
        text: "Retry",
        type: "button-positive",
        onTap: function() {
          $scope.restart()
        }
      }]
    })
  }

  $scope.restart = function () {
    console.log("Restarting level...")
    $state.reload()
  }
})
