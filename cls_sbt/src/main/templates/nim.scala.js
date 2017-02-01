function setSlider(i) {
  var value = $scope.sliders[i];
  var row = document.getElementsByName('row'+i)[0];
  row.cells[0].colSpan = value;
  if(value <= 0){
    var div = row.getElementsByClassName('item')[0].style.backgroundColor="lightgray";
  }
  var slider = document.getElementsByName('slider'+i)[0];
  slider.max = value;
  var icon = row.getElementsByClassName('icon')[1];
  icon.innerHTML = "" + value;
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

// This runs whenever a level is entered
$scope.$on("$ionicView.afterEnter", function(scopes, states){
  $scope.completed = false;
  console.log("Entered "+states.stateName+" "+$stateParams.levelNum+":", $rootScope.levels[$stateParams.levelNum]);
  $scope.initializeLevel();
  $scope.levelStartTime = Date.now();
});

$scope.initializeLevel = function() {
  $scope.sliders = [
    [1, 2],
    [4, 5],
    [1, 2, 4],
    [2, 3, 4],
    [1, 6, 8],
  ][$stateParams.levelNum];
  for (var i=0; i<$scope.sliders.length; i++) {
    setSlider(i);
  }
}

$scope.callback = function(slider) {
  var i = parseInt(slider.slice(-1));
  $scope.sliders[i] = parseInt(document.getElementsByName('slider'+i)[0].value);
  setSlider(i);

  if ($scope.checkComplete()) {
    $scope.completeLevel()
  } else {
    // Artificial delay 0.5s to simulate thinking
    setTimeout(function() {
      $scope.ai(0.2)

      if ($scope.checkComplete()) {
        $scope.loseLevel();
      }
    }, 500)
  }
}

$scope.checkComplete = function() {
  for (var i=0; i<$scope.sliders.length; i++) {
    if ($scope.sliders[i] != 0) {
      return false
    }
  }
  return true
}
