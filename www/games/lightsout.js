
angular.module("lightsout", ["ionic", "sql", "dictionary"])

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

  // Used by the HTML to name the buttons
$scope.buttons = []
for (var i=0; i<6; i++) {
  var row = []
  for (var j=0; j<6; j++) {
    row[j] = i+"_"+j
  }
  $scope.buttons[i] = row
}
// $scope.buttons = [
//   ["0_0", "0_1", "0_2", "0_3", "0_4", "0_5"],
//   ["1_0", "1_1", "1_2", "1_3", "1_4", "1_5"],
//   ["2_0", "2_1", "2_2", "2_3", "2_4", "2_5"],
//   ["3_0", "3_1", "3_2", "3_3", "3_4", "3_5"],
//   ["4_0", "4_1", "4_2", "4_3", "4_4", "4_5"],
//   ["5_0", "5_1", "5_2", "5_3", "5_4", "5_5"]
// ]

function convertTo(longState) {
  arr = []
  for (var i = 0; i < 6; i++) {
    row = []
    for (var j = 0; j < 6; j++) {
      row.unshift(longState%2)
      longState = Math.floor(longState/2)
    }
    arr.unshift(row)
  }
  return arr
}

function convertFrom(arr) {
  longState = 0
  for (var i = 0; i < 6; i++) {
    for (var j = 0; j < 6; j++) {
      longState *= 2
      longState += arr[i][j]
    }
  }
  return longState
}

$scope.beforeLeave = function () {
  var state = convertFrom($scope.buttonsList)
  //console.log("State before leave: " + $scope.buttonsList)
  sqlfactory.setLevelState($scope.levelNum, state)
}

$scope.initializeLevel = function () {
  if ($rootScope.levelData[$scope.levelNum]["state"] == "Unsolved" ||
      $rootScope.levelData[$scope.levelNum]["state"] == 0) {
    var defaultState = [
      18123587584,   18119623680,   349703572,   8738341376,   315360000,
      20186262387,   46182173418,   484093340,  32774690910, 26754334360,
      20087122770,   35738666145, 13103002380,  13099807500, 67956511935,
      20186189010,   67645734912, 32980810590,  68518150143, 68719476735
    ][$scope.levelNum]
    sqlfactory.setLevelState($scope.levelNum, defaultState)
  }
  $scope.buttonsList = convertTo($rootScope.levelData[$scope.levelNum]["state"])
  console.log("State for "+$scope.levelName+": "+$scope.buttonsList)
  for (var row=0; row<6; row++) {
    for (var col=0; col<6; col++) {
      var name = $scope.levelNum + "_" + row + "_" + col
      var button = document.getElementById(name)
      if (button === null) {
        console.log("Couldn't find button "+name)
      } else if ($scope.buttonsList[row][col] == 1) {
        button.className = "button button-energized"
      } else {
        button.className = "button button-dark"
      }
    }
  }
}

$scope.toggleButton = function (row, col) {
  var name = $scope.levelNum + "_" + row + "_" + col
  var button = document.getElementById(name)
  if (button === null) {
    return
  }
  //console.log(button.className)
  if ($scope.buttonsList[row][col] == 1) {
    button.className = "button button-dark"
    $scope.buttonsList[row][col] = 0
  } else if ($scope.buttonsList[row][col] == 0) {
    button.className = "button button-energized"
      $scope.buttonsList[row][col] = 1
  }
}

// Logic for toggling lights.
$scope.toggle = function (button_name) {
  // All of our buttons are named "#_#", so if the table was 10 rows or 10
  // columns, this logic would need to change.
  var row = parseInt(button_name.charAt(0))
  var col = parseInt(button_name.charAt(2))

  $scope.toggleButton(row, col)
  $scope.toggleButton(row, col+1)
  $scope.toggleButton(row, col-1)
  $scope.toggleButton(row+1, col)
  $scope.toggleButton(row-1, col)

  // Check for game completion
  for (var row=0; row < $scope.buttonsList.length; row++) {
    for (var col=0; col < $scope.buttonsList[0].length; col++) {
      var name = $scope.levelNum + "_" + row + "_" + col
      var button = document.getElementById(name)
      if (button.className.includes("button-energized")) {
        // Found a button which wasn't off, level not complete
        return
      }
    }
  }
  console.log("All lights out, completing level.")
  $scope.completeLevel(true)
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
