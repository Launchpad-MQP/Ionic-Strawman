@(contents:JavaScript)
angular.module("game", ["ionic", "sql"])

.controller("LevelCtrl", function ($scope, $rootScope, $ionicPopup, $state, $stateParams, sqlfactory) {
  // Check for invalid state
  if ($rootScope.levelData === undefined) {
    console.log("Level loaded but level list undefined, going to main")
    $state.go("main")
  } else {
    try {
      // Redefined so that they can be used in the HTML
      $scope.levelName = $rootScope.levelData[$stateParams.levelNum]["name"]
      $scope.levelNum = $stateParams.levelNum
      console.log("Now in " + $scope.levelName)
    } catch (err) {
      console.log("Tried to go to " + $stateParams.levelNum + ", redirecting to level select.")
      $state.go("level_select")
    }
  }

  @(contents)

  // This runs when the level is entered
  $scope.$on("$ionicView.afterEnter", function(scopes, states){
    console.log("Entered "+$stateParams.levelName+":", $rootScope.levelData[$stateParams.levelNum])
    $rootScope.levelData[$stateParams.levelNum]["time"] -= Date.now()
    if (typeof $scope.initializeLevel === "function") {
      $scope.initializeLevel()
    }
  })

  // This runs when the level is exited
  $scope.$on("$ionicView.beforeLeave", function(scopes, states){
    console.log("Exited "+states.stateName+" "+$stateParams.levelNum+":", $rootScope.levelData[$stateParams.levelNum])
    $rootScope.levelData[$stateParams.levelNum]["time"] += Date.now()
    if (typeof $scope.beforeLeave === "function") {
      $scope.beforeLeave()
    }
  })

  $scope.completeLevel = function(won) {
    var time = $rootScope.levelData[$stateParams.levelNum]["time"] + Date.now()
    if (won) {
      sqlfactory.setLevelState($stateParams.levelNum, "Solved", 0)
      button = document.getElementById("level_"+$stateParams.levelNum)
      button.setAttribute("class", "button button-dark ng-binding")
      var title = $scope.levelName + " Complete! Total time: " + time / 1000 + " seconds"
      if(typeof $scope.onWin === "function") {
 			  $scope.onWin()
 			}
      var levelOption = {
        text: "Next",
        type: "button-positive",
        onTap: function () {
          console.log("On to the next level.")
          $state.go("level", {"levelNum": $stateParams.levelNum+1})
        }
      }
    } else {
      var title = "You lose!"
      if(typeof $scope.onLose === "function") {
        $scope.onLose()
      }
      var levelOption = {
        text: "Retry",
        type: "button-positive",
        onTap: function() {
          $scope.restart()
        }
      }
    }

     $ionicPopup.show({
       title: title,
       buttons: [
       {
         text: "Level Select",
         onTap: function () {
           console.log("Back to level select.")
           $state.go("level_select")
         }
       }, levelOption]
     })
   }

  $scope.restart = function () {
    console.log("Restarting level...")
    $rootScope.levels[$stateParams.levelNum].time += Date.now()
    if(typeof $scope.restartLevel === "function") {
      $scope.restartLevel()
    }
		$rootScope.states[$stateParams.levelNum-1] = 0
		sqlfactory.setLevelState($stateParams.levelNum, 0)
    $state.reload()
  }
})