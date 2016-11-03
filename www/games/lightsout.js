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
  $scope.buttonsList = [
    [
      [0, 1, 0, 0, 0, 0],
      [1, 1, 1, 0, 0, 0],
      [0, 1, 0, 0, 0, 0],
      [0, 0, 0, 0, 0, 0],
      [0, 0, 0, 0, 0, 0],
      [0, 0, 0, 0, 0, 0],
    ],
    [
      [0, 1, 0, 0, 0, 0],
      [1, 1, 1, 0, 0, 0],
      [0, 0, 0, 0, 0, 0],
      [1, 1, 1, 0, 0, 0],
      [0, 1, 0, 0, 0, 0],
      [0, 0, 0, 0, 0, 0],
    ],
    [
      [0, 0, 0, 0, 0, 0],
      [0, 1, 1, 1, 0, 0],
      [1, 0, 1, 0, 1, 0],
      [0, 1, 1, 1, 0, 0],
      [0, 0, 1, 0, 0, 0],
      [0, 0, 0, 0, 0, 0],
    ],
    [
      [0, 0, 1, 0, 0, 0],
      [0, 0, 1, 0, 0, 0],
      [1, 1, 0, 1, 1, 0],
      [0, 0, 1, 0, 0, 0],
      [0, 0, 1, 0, 0, 0],
      [0, 0, 0, 0, 0, 0],
    ],
    [
      [0, 0, 0, 0, 0, 0],
      [0, 1, 0, 0, 1, 0],
      [1, 1, 0, 0, 1, 1],
      [0, 0, 0, 0, 0, 0],
      [0, 0, 1, 1, 0, 0],
      [0, 0, 0, 0, 0, 0],
    ],
    [
      [0, 0, 0, 0, 1, 0],
      [1, 0, 0, 0, 0, 0],
      [0, 0, 0, 0, 0, 0],
      [0, 0, 0, 0, 0, 0],
      [1, 0, 0, 0, 0, 0],
      [1, 0, 0, 0, 1, 1],
    ],
    [
      [0, 0, 0, 0, 0, 0],
      [0, 0, 0, 0, 0, 0],
      [0, 0, 0, 0, 1, 0],
      [0, 0, 0, 0, 0, 0],
      [0, 0, 0, 0, 0, 0],
      [0, 0, 0, 0, 0, 0],
    ],
    [
      [0, 0, 0, 0, 0, 0],
      [0, 0, 0, 0, 1, 0],
      [0, 0, 0, 1, 0, 1],
      [0, 0, 0, 0, 1, 0],
      [0, 0, 0, 0, 0, 0],
      [0, 0, 0, 0, 0, 0],
    ],
    [
      [0, 0, 0, 0, 0, 0],
      [1, 1, 0, 0, 0, 0],
      [0, 0, 1, 0, 0, 0],
      [1, 1, 0, 0, 0, 0],
      [0, 0, 0, 0, 0, 0],
      [0, 0, 0, 0, 0, 0],
    ],
    [
      [0, 0, 0, 0, 0, 0],
      [0, 0, 1, 0, 0, 0],
      [0, 0, 1, 1, 0, 0],
      [1, 1, 0, 0, 0, 0],
      [0, 1, 0, 0, 0, 0],
      [0, 0, 0, 0, 0, 0],
    ]
  ];
  $scope.buttons = [ // $scope.buttonsList[$stateParams.levelNum-1];
    ["0_0", "0_1", "0_2", "0_3", "0_4", "0_5"],
    ["1_0", "1_1", "1_2", "1_3", "1_4", "1_5"],
    ["2_0", "2_1", "2_2", "2_3", "2_4", "2_5"],
    ["3_0", "3_1", "3_2", "3_3", "3_4", "3_5"],
    ["4_0", "4_1", "4_2", "4_3", "4_4", "4_5"],
    ["5_0", "5_1", "5_2", "5_3", "5_4", "5_5"],
  ];

  //console.log($stateParams.levelNum);
  //console.log(document.getElementsByClassName("lit"));
  $scope.Math = window.Math;
  document.addEventListener('DOMContentLoaded', function(){
    console.log("Document loaded");
  }, false);

  // This runs whenever a level is entered
  $scope.$on("$ionicView.afterEnter", function(scopes, states){
    console.log(states.stateName);
    console.log($stateParams.levelNum);
    $scope.initializeLevel();
  });

  $scope.initializeLevel = function() {
    for(var r=0; r < 6; r++){
      for(var c=0; c < 6; c++){
        var button = document.getElementById(($stateParams.levelNum-1)+"_"+r+"_"+c);
        if(button===null){
          console.log("Button "+($stateParams.levelNum-1)+"_"+r+"_"+c+" does not exist!");
        }
        else{
          if($scope.buttonsList[$stateParams.levelNum-1][r][c]===1){
            button.className = "button button-energized";
          }
          else{
            button.className = "button button-dark";
          }
        }
      }
    }
  }

  $scope.toggleButton = function (buttonNum) {
    var button = document.getElementById(($stateParams.levelNum-1) + "_" + buttonNum);
    if(button===null){
      return;
    }
    //console.log(button.className);
    if (button.className.includes("button-energized")){
      button.className = "button button-dark";
    } else if(button.className.includes("button-dark")){
      button.className = "button button-energized";
    }
  }

  // Logic for toggling lights.
  $scope.toggle = function (levelNumber, buttonRowCol) {
    var row = parseInt(buttonRowCol.charAt(0));
    var col = parseInt(buttonRowCol.charAt(2));

    $scope.toggleButton(row+"_"+col);
    $scope.toggleButton(row+"_"+(col+1));
    $scope.toggleButton(row+"_"+(col-1));
    $scope.toggleButton((row+1)+"_"+col);
    $scope.toggleButton((row-1)+"_"+col);
    $scope.checkCompleteLevel();
  }

  // Begin: The entirety of our "game".
  // When all buttons (lights) are out the game ends.
  $scope.checkCompleteLevel = function () {
    var allDark = true;
    for(var r=0; r<6; r++){
      for(var c=0; c<6; c++){
        var bttns = document.getElementById(($stateParams.levelNum-1)+"_"+r+"_"+c);
        if(bttns.className.includes("button-energized")){
          allDark = false;
          return;
        }
      }
    }

    if (allDark){
      //console.log("All lights out.");
      $rootScope.completeLevel($state, $stateParams.levelNum);
    }
  }

  $scope.restart = function () {
    console.log("Restarting level...");
    $state.reload();
  }
});
