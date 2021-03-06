
angular.module("nim", ["ionic", "sql", "dictionary"])

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

  function setSlider(i) {
  var value = $scope.sliders[i]
  var slider = document.getElementsByName('slider_'+$scope.levelNum+'_'+i)[0]
  slider.max = value
  slider.value = value
  var div = slider.parentElement
  div.style.width = 100 * value / $scope.max + "%"
  var rightIcon = div.getElementsByTagName('i')[1]
  rightIcon.innerHTML = value
}

// The zero-sum state is when the bitwise xor is 0, so we make the move which
// reaches this state. Returns true if a move was made, false otherwise.
function doBestMove() {
  var bitwiseXor = 0
  for (var i=0; i<$scope.sliders.length; i++) {
    bitwiseXor ^= $scope.sliders[i]
  }
  for (var i=0; i<$scope.sliders.length; i++) {
    // If changing this slider is a valid move, take it.
    if (($scope.sliders[i] ^ bitwiseXor) < $scope.sliders[i]) {
      $scope.sliders[i] ^= bitwiseXor
      setSlider(i)
      return true
    }
  }
  return false
}

// Picks an equally weighted random move, so it's more likely to pick
// from a longer column.
function doRandomMove() {
  var numMoves = 0
  for (var i=0; i<$scope.sliders.length; i++) {
    numMoves += $scope.sliders[i]
    console.log($scope.sliders[i], numMoves)
  }
  var chosenMove = Math.floor(Math.random()*numMoves)
  console.log(chosenMove)
  for (var i=0; i<$scope.sliders.length; i++) {
    console.log(chosenMove, $scope.sliders[i])
    if (chosenMove >= $scope.sliders[i]) {
      chosenMove -= $scope.sliders[i]
      console.log(chosenMove)
      continue
    } else {
      $scope.sliders[i] = chosenMove
      setSlider(i)
      break
    }
  }
}

// Asks the AI to make a move, with a given percentage chance of a mistake.
$scope.ai = function(mistake) {
  if (Math.random() <= mistake) {
    doRandomMove()
    console.log("AI makes a mistake")
  } else {
    var madeMove = doBestMove()
    if (madeMove) {
      console.log("AI made best move")
    } else {
      doRandomMove()
      console.log("AI is in a losing state, made a random move")
    }
  }
}

$scope.finished = false

$scope.initializeLevel = function() {
  $scope.sliders = [
    [1, 2, 0, 0, 0],
    [4, 5, 0, 0, 0],
    [1, 2, 4, 0, 0],
    [2, 3, 4, 0, 0],
    [1, 6, 8, 0, 0],
  ][$scope.levelNum]
  $scope.max = Math.max.apply(null, $scope.sliders)
  var levelState = $rootScope.levelData[$scope.levelNum]["state"]
  if(levelState != "Solved" && levelState != "Unsolved" &&
      levelState != "0 0 0 0 0" && levelState != "") {
        var slides = $rootScope.levelData[$scope.levelNum]["state"]
        console.log("Slide info")
        console.log(slides)
        $scope.sliders = slides.split(" ")
        console.log($scope.sliders)
      }
  for (var i=0; i<$scope.sliders.length; i++) {
    setSlider(i)
  }
}

$scope.callback = function(slider) {
  var i = parseInt(slider.split('_')[2])
  var value = parseInt(document.getElementsByName('slider_'+$scope.levelNum+'_'+i)[0].value)
  if ($scope.sliders[i] == value) {
    return // User didn't change the slider
  } else {
    $scope.sliders[i] = value
  }
  setSlider(i)

  if ($scope.checkComplete()) {
    $scope.completeLevel(true)
  } else {
    // Artificial delay 0.5s to simulate thinking
    setTimeout(function() {
      $scope.ai(0.2)

      if ($scope.checkComplete()) {
        $scope.completeLevel(false)
      }
    }, 500)
  }
}

$scope.beforeLeave = function() {
  var slidersState = "";
  for(var i = 0; i < $scope.sliders.length; i++) {
    slidersState += $scope.sliders[i]
    if(i != $scope.sliders.length - 1)
      slidersState += " "
  }
  if(!$scope.finished)
    sqlfactory.setLevelState($scope.levelNum, slidersState)
}

$scope.onWin = function() {
  console.log("On Win")
  $scope.finished = true
  sqlfactory.setLevelState($scope.levelNum, "Solved", 0)
}

$scope.onLose = function() {
  console.log("losing")
  sqlfactory.setLevelState($scope.levelNum, "Lost")
}

$scope.checkComplete = function() {
  for (var i=0; i<$scope.sliders.length; i++) {
    if ($scope.sliders[i] != 0) {
      return false
    }
  }
  return true
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
