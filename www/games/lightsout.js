angular.module("lightsout", ["ionic", "sql"])

.controller("LevelCtrl", function ($scope, $rootScope, $state, $stateParams, $ionicPopup, sqlfactory) {
  // Check for invalid level number
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

  // Define buttons for use in HTML
  $scope.buttons = [
    [ 0,  1,  2,  3,  4,  5],
    [ 6,  7,  8,  9, 10, 11],
    [12, 13, 14, 15, 16, 17],
    [18, 19, 20, 21, 22, 23],
    [24, 25, 26, 27, 28, 29],
    [30, 31, 32, 33, 34, 35],
  ];

  $scope.toggleButton = function (buttonNum) {
    var button = document.getElementById(buttonNum.toString());
    console.log(button.className);
    if (button.className === "button button-energized lit activated"){
      button.className = "button button-dark";
    } else {
      button.className = "button button-energized lit";
    }
  }

  // Logic for toggling lights.
  $scope.toggle = function (buttonNum) {
    var buttonAbove = buttonNum-$scope.buttons[0].length;
    var buttonBelow = buttonNum-$scope.buttons[0].length;

    $scope.toggleButton(buttonNum);
    $scope.checkCompleteLevel();
  }

  // Begin: The entirety of our "game".
  // When all buttons (lights) are out the game ends.
  $scope.checkCompleteLevel = function () {
    var buttonList = document.getElementsByClassName("lit");

    if (buttonList.length == 0){
      console.log("All lights out.");
    }
  }

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

  $scope.restart = function () {
    console.log("Restarting level...");
    $state.reload();
  }
});
