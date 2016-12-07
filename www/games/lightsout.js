angular.module("lightsout", ["ionic", "sql"])

.controller("LevelCtrl", function ($scope, $rootScope, $state, $stateParams, $ionicPopup, sqlfactory) {
  // Check for invalid level number
  if ($rootScope.levels === undefined) {
    console.log("Level loaded but level list undefined, going to main");
    $state.go("main");
  } else if (!$rootScope.levels.includes($stateParams.levelNum)) {
    console.log("Went to level " + $stateParams.levelNum + " redirecting to level select.");
    $state.go("level_select");
  } else {
    console.log("Now in level: " + $stateParams.levelNum);
  }

  // Redefined so that it can be used in the HTML.
  $scope.levelNum = $stateParams.levelNum;

  // Used by the HTML to name the buttons
  $scope.buttons = [
    ["0_0", "0_1", "0_2", "0_3", "0_4", "0_5"],
    ["1_0", "1_1", "1_2", "1_3", "1_4", "1_5"],
    ["2_0", "2_1", "2_2", "2_3", "2_4", "2_5"],
    ["3_0", "3_1", "3_2", "3_3", "3_4", "3_5"],
    ["4_0", "4_1", "4_2", "4_3", "4_4", "4_5"],
    ["5_0", "5_1", "5_2", "5_3", "5_4", "5_5"]
  ];

  function convertTo(longState) {
    arr = [];
    for (var i = 0; i < 6; i++) {
      row = [];
      for (var j = 0; j < 6; j++) {
        row.unshift(longState%2);
        longState = Math.floor(longState/2);
      }
      arr.unshift(row);
    }
    return arr;
  }

  function convertFrom(arr) {
    longState = 0;
    for (var i = 0; i < 6; i++) {
      for (var j = 0; j < 6; j++) {
        longState *= 2;
        longState += arr[i][j];
      }
    }
    return longState;
  }

  // test = 23587584;
  // console.log(test);
  // test = convertTo(test);
  // console.log(""+test);
  // test = convertFrom(test);
  // console.log(test);

  // This runs whenever a level is entered
  $scope.$on("$ionicView.afterEnter", function(scopes, states){
    console.log("Entered "+states.stateName+" "+$stateParams.levelNum);
    $scope.buttonsList = convertTo($rootScope.states[$stateParams.levelNum-1]);
    console.log("State for level "+$stateParams.levelNum + ":"+$scope.buttonsList);
    for (var row=0; row<6; row++) {
      for (var col=0; col<6; col++) {
        var name = $stateParams.levelNum + "_" + row + "_" + col;
        var button = document.getElementById(name);
        if (button === null) {
          console.log("Couldn't find button "+name);
        } else if ($scope.buttonsList[row][col] == 1) {
          button.className = "button button-energized";
        } else {
          button.className = "button button-dark";
        }
      }
    }
  });

  $scope.$on("$ionicView.beforeLeave", function(scope, state){
    console.log("Exiting "+state.stateName+" "+$stateParams.levelNum);
    state = convertFrom($scope.buttonsList);
    $rootScope.states[$stateParams.levelNum-1] = state;
    sqlfactory.setLevelState($stateParams.levelNum, state);
  });

  $scope.toggleButton = function (row, col) {
    var name = $stateParams.levelNum + "_" + row + "_" + col;
    var button = document.getElementById(name);
    if (button === null) {
      return;
    }
    //console.log(button.className);
    if ($scope.buttonsList[row][col] == 1) {
      button.className = "button button-dark";
      $scope.buttonsList[row][col] = 0;
    } else {
      $scope.buttonsList[row][col] = 1;
      button.className = "button button-energized";
    }
  }

  // Logic for toggling lights. This can be simplified if toggling uses the number, not the array: completion is just compare to 0.
  $scope.toggle = function (button_name) {
    // All of our buttons are named "#_#", so if the table was 10 rows or 10
    // columns, this logic would need to change.
    var row = parseInt(button_name.charAt(0));
    var col = parseInt(button_name.charAt(2));

    $scope.toggleButton(row, col);
    $scope.toggleButton(row, col+1);
    $scope.toggleButton(row, col-1);
    $scope.toggleButton(row+1, col);
    $scope.toggleButton(row-1, col);

    // Check for game completion
    for (var row=0; row < $scope.buttonsList.length; row++) {
      for (var col=0; col < $scope.buttonsList[0].length; col++) {
        if ($scope.buttonsList[row][col] != 0) {
          // Found a button which wasn't off, level not complete
          return;
        }
      }
    }
    console.log("All lights out, completing level.")
    $rootScope.completeLevel($state, $stateParams.levelNum);
  }

  $scope.restart = function () {
    console.log("Restarting level...");
    $rootScope.states[$stateParams.levelNum-1] = $rootScope.defaultStates[$stateParams.levelNum-1];
    $state.reload();
  }
});
