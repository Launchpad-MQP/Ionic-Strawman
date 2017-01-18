$scope.winStates = [
  'hello', 'wolf', 'rat', 'xylophone', 'attention',
  'school', 'ruling', 'poison', 'tree', 'prison',
  'abacus', 'toothache', 'short', 'bacon', 'crossroads',
  'darkness', 'candle', 'quadruple', 'extraordinary', 'declaration'
][$scope.levelNum-1].split("");
$scope.guessesLeft = 7;
$scope.miss = true;

$scope.frankenVars = {
  cBox1: false,
  cBox2: false,
  cBox3: false
}

$scope.makeGuess = function () {
}

$scope.loseLevel = function() {
}

$scope.checkComplete = function () {
  console.log($scope.frankenVars);
  $scope.completeLevel();
}
