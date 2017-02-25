
angular.module("game", ["ionic", "sql", "dictionary"])

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

  $scope.myLetters = [
  'hello', 'wolf', 'rat', 'xylophone', 'attention',
  'school', 'ruling', 'poison', 'tree', 'prison',
  'abacus', 'toothache', 'short', 'bacon', 'crossroads',
  'darkness', 'candle', 'quadruple', 'extraordinary', 'declaration'
  ][$scope.levelNum].split("")
  $scope.guessesLeft = 7
  $scope.miss = true
  $scope.finished = false

  $scope.makeGuess = function () {
  var guessBox = document.getElementById("letterguess_" + $scope.levelNum)
  var guess = guessBox.value
  var card = document.getElementById("guessed_" + $scope.levelNum)

  console.log("Guess", guess)
  // Fields are the elements which match the letter guessed.
  var fields = document.getElementsByClassName(guess)
  console.log("Fields", fields)
  for(var i = 0; i<fields.length; i++) {
     if(!fields[i].className.includes("discovered")) {
       fields[i].className = "discovered " + $scope.levelNum + " " + guess
     }
     $scope.miss = false
  }
  if($scope.miss) {
   if(!card.innerHTML.includes(guess)) {
     $scope.guessesLeft--
     card.append(guess + " ")
   }
   if($scope.guessesLeft==0)
     $scope.completeLevel(false)
  } else {
   if(!card.innerHTML.includes(guess))
     card.append(guess + " ")
   $scope.checkComplete()
  }
  guessBox.value = ""
  $scope.miss = true
  }

  $scope.checkComplete = function () {
  var correct = document.getElementsByClassName("discovered " + $scope.levelNum)
  console.log(correct.length)
  if(correct.length === $scope.myLetters.length)
   $scope.completeLevel(true)
  }

  $scope.beforeLeave = function() {
  var card = document.getElementById("guessed_" + $scope.levelNum)
  var guessed = card.innerHTML.split(", ")
  console.log("before leaving")
  console.log(guessed)
  if(!$scope.finished)
    sqlfactory.setLevelState($scope.levelNum, guessed[0])
  }

  $scope.onWin = function() {
  console.log("On Win")
  $scope.finished = true
  sqlfactory.setLevelState($scope.levelNum, "Solved", 0)
  }

  $scope.onLose = function() {
  console.log("losing")
  sqlfactory.setLevelState($scope.levelNum, "Lost")
  document.getElementById("guessed_" + $scope.levelNum).innerHTML = ""
  }

  $scope.initializeLevel = function() {
  $scope.guessesLeft = 7
  $scope.miss = true
  var card = document.getElementById("guessed_" + $scope.levelNum)
  var guessBox = document.getElementById("letterguess_" + $scope.levelNum)
  card.innerHTML = ""
  guessBox.value = ""
  var fields = document.getElementsByClassName($scope.levelNum)
  for(var i = 0; i<fields.length; i++) {
   var letter = fields[i].className.slice(-1)
   fields[i].className = "guessable " + $scope.levelNum + " " + letter
  }

  if ($rootScope.levelData[$scope.levelNum]["state"] != "Unsolved" &&
        $rootScope.levelData[$scope.levelNum]["state"] != "Solved") {
    console.log($rootScope.levelData[$scope.levelNum]["state"])
    var letters = $rootScope.levelData[$scope.levelNum]["state"].split("")

    for (var j = 0; j < letters.length; j++) {
      guessBox.value = letters[j]
      $scope.makeGuess()
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
