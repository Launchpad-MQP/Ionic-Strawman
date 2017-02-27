
angular.module("mastermind", ["ionic", "sql", "dictionary"])

.controller("LevelCtrl", function ($scope, $rootScope, $ionicPopup, $state, $stateParams, sqlfactory, dictionary) {
  // Check for invalid state
  if ($rootScope.levelData === undefined) {
    console.log("Level loaded but level list undefined, going to main")
    $state.go("main")
  } else {
    try {
      // Redefined so that they can be used in the HTML
      $scope.levelNum = $stateParams.levelNum
      $scope.levelName = $rootScope.levelData[$scope.levelNum]["name"]
      console.log("Now in " + $scope.levelName)
    } catch (err) {
      console.log("Tried to go to " + $scope.levelNum + ", redirecting to level select.")
      $state.go("level_select")
    }
  }

  // Word to guess for each level
$scope.initializeLevel = function() {
  $scope.word = [
     "rest",  "cats",  "fate",  "hurt",  "note",
     "wolf",  "milk",  "yaks",  "know",  "sync",
    "spade", "exist", "group", "topic", "fruit",
    "movie", "style", "vital", "lynch", "rhythm"
  ][$scope.levelNum]
  console.log("Word for this level:", $scope.word)
  $scope.result = ""
}

$scope.makeGuess = function() {
  guess = document.getElementById("guess_"+$scope.levelNum).value
  console.log("Guess: "+guess)
  if (guess.length != $scope.word.length) {
    console.log(guess.length, "didn't match", $scope.word.length)
    $scope.result = "Please guess a word of length "+$scope.word.length+"."
  } else if (!dictionary.isWord(guess)) {
    console.log($scope.word, "invalid")
    $scope.result = $scope.word+" is not a valid word!"
  } else {
    console.log($scope.word, "valid")
    if (guess == $scope.word) {
      $scope.result = ""
      $scope.completeLevel(true)
    } else {
      matches = 0
      for (var letter in $scope.word) {
        if (guess.includes($scope.word[letter])) {
          matches++
        }
      }
      console.log(matches+" letters match between "+$scope.word+" and "+guess)
      $scope.result = matches+" matching letter"+(matches==1?"":"s")
    }
  }
}


  // This runs when the level is entered
  $scope.$on("$ionicView.afterEnter", function(scopes, states){
    console.log("Entered "+$scope.levelName+":", $rootScope.levelData[$scope.levelNum])
    
      $rootScope.levelData[$scope.levelNum]["time"] -= Date.now()
    
    if (typeof $scope.initializeLevel === "function") {
      $scope.initializeLevel()
    }
  })

  // This runs when the level is exited
  $scope.$on("$ionicView.beforeLeave", function(scopes, states){
    console.log("Exited "+states.stateName+" "+$scope.levelNum+":", $rootScope.levelData[$scope.levelNum])
    $rootScope.levelData[$scope.levelNum]["time"] += Date.now()
    if (typeof $scope.beforeLeave === "function") {
      $scope.beforeLeave()
    }
  })

  $scope.completeLevel = function(won) {
    
      var time = $rootScope.levelData[$scope.levelNum]["time"] + Date.now()
    
    if (won) {
      sqlfactory.setLevelState($scope.levelNum, "Solved", 0)
      button = document.getElementById("level_"+$scope.levelNum)
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
          $state.go("level", {"levelNum": $scope.levelNum+1})
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
    sqlfactory.setLevelState($scope.levelNum, "")
    
      $rootScope.levelData[$scope.levelNum]["time"] += Date.now()
    
    if(typeof $scope.restartLevel === "function") {
      $scope.restartLevel()
    }
    $state.reload()
  }
})
