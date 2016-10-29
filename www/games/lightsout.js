angular.module("lightsout", ["ionic", "sql"])

.controller("LevelCtrl", function ($scope, $rootScope, $state, $stateParams, $ionicPopup, sqlfactory) {
  // Check for invalid level number
  if(!$rootScope.levels.includes($stateParams.levelNum)) {
    console.log("Went to level " + $stateParams.levelNum + " redirecting to level select.");
    $state.go("level_select");
  } else {
    console.log("Now in level: " + $stateParams.levelNum);
  }

  // Redefined so that it can be used in the HTML.
  $scope.levelNum = $stateParams.levelNum;

  // Define buttons for use in HTML
  $scope.buttons = [
  [0, 1, 2, 3, 4, 5],
  [6, 7, 8, 9, 10, 11],
  [12, 13, 14, 15, 16, 17],
  [18, 19, 20, 21, 22, 23],
  [24, 25, 26, 27, 28, 29],
  [30, 31, 32, 33, 34, 35],
  ];

  $scope.toggleButton = function(buttonNum){
    var button = document.getElementById(buttonNum.toString());
    console.log(button.className);
    if(button.className === "button button-energized lit activated"){
      button.className = "button button-dark";
    }
    else{
      button.className = "button button-energized lit";
    }
  }

  // Logic for toggling lights.
  $scope.toggle = function(buttonNum) {
    var buttonAbove = buttonNum-$scope.buttons[0].length;
    var buttonBelow = buttonNum-$scope.buttons[0].length;

    $scope.toggleButton(buttonNum);
    $scope.checkCompleteLevel();
  }

  // Begin: The entirety of our "game".
  // When all buttons (lights) are out the game ends.
  $scope.checkCompleteLevel = function () {
    var buttonList = document.getElementsByClassName("lit");

    if(buttonList.length == 0){
      console.log("All lights out.");
    }
  }
  // End: The entirety of our "game".

  // Begin: The entirety of our "game". Shows a button, which when clicked
  // beats the level. It also shows "back" and "next" options.
  $scope.completeLevel = function () {
    completeLevel($stateParams.levelNum, sqlfactory);

    var levelOverPopUp = $ionicPopup.show({
      title: "Level Complete!",
      scope: $scope,
      buttons: [
      {
        text: "Level Select",
        onTap: function (e) {
          console.log("Back to level select.");
          $state.go("level_select");
        }
      },
      {
        text: "Next",
        type: "button-positive",
        onTap: function (e) {
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

// When a level is completed, find the appropriate button and make it gray.
function completeLevel (number, sqlfactory) {
  button = document.getElementById("level_"+number);
  button.setAttribute("class", "button button-dark ng-binding");

  sqlfactory.setLevelState(number, "Solved");
}