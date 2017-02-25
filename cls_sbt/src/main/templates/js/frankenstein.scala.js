$scope.frankenVars = {
  toggle: false,
  rBox: "false",
  cBox1: false,
  cBox2: false,
  cBox3: false,
  range1: 50
}

$scope.successVals = [
{
  toggle: false,
  rBox: "Do not",
  cBox1: false,
  cBox2: false,
  cBox3: true,
  rangeMin: 0,
  rangeMax: 60
},
{
  toggle: true,
  rBox: "Check Me",
  cBox1: true,
  cBox2: true,
  cBox3: false,
  rangeMin: 60,
  rangeMax: 100
}
][$scope.levelNum%2]

$scope.checkComplete = function () {
  console.log($scope.frankenVars)
  if($scope.frankenVars.toggle == $scope.successVals.toggle &&
    $scope.frankenVars.rBox == $scope.successVals.rBox &&
    $scope.frankenVars.cBox1 == $scope.successVals.cBox1 &&
    $scope.frankenVars.cBox2 == $scope.successVals.cBox2 &&
    $scope.frankenVars.cBox3 == $scope.successVals.cBox3 &&
    $scope.frankenVars.range1 >= $scope.successVals.rangeMin &&
    $scope.frankenVars.range1 <= $scope.successVals.rangeMax) {
      $scope.completeLevel()
  }
}
