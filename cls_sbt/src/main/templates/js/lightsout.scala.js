
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

$scope.beforeLeave = function () {
  var s = convertFrom($scope.buttonsList);
  //console.log("State before leave: " + $scope.buttonsList);
  $rootScope.states[$stateParams.levelNum-1] = s;
  sqlfactory.setLevelState($stateParams.levelNum, s);
}

$scope.initializeLevel = function () {
    if(typeof $rootScope.states === "undefined"){
    $rootScope.states = [
      0, 0, 0, 0, 0,
      0, 0, 0, 0, 0,
      0, 0, 0, 0, 0,
      0, 0, 0, 0, 0
    ];
    $rootScope.defaultStates = [
      18123587584, 18119623680, 480887296, 8738341376, 315360000,
       2684356643,      524288,  34873344,  807600128, 137561088,
                0,           0,         0,          0,         0,
                0,           0,         0,          0,         0
    ];
  }
  if($rootScope.states[$stateParams.levelNum-1] != 0){
    $scope.buttonsList = convertTo($rootScope.states[$stateParams.levelNum-1]);
  }
  else{
    $scope.buttonsList = convertTo($rootScope.defaultStates[$stateParams.levelNum-1]);
  }
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
}

$scope.restartLevel = function () {
}

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
  } else if ($scope.buttonsList[row][col] == 0) {
    button.className = "button button-energized";
      $scope.buttonsList[row][col] = 1;
  }
}

// Logic for toggling lights.
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
      var name = $stateParams.levelNum + "_" + row + "_" + col;
      var button = document.getElementById(name);
      if (button.className.includes("button-energized")) {
        // Found a button which wasn't off, level not complete
        return;
      }
    }
  }
  console.log("All lights out, completing level.");
  $scope.completed = true;
  $scope.completeLevel($state, $stateParams.levelNum, $scope.levelStartTime);
}
