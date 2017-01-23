$scope.frankenVars = {
  rBox: "false",
  cBox1: false,
  cBox2: false,
  cBox3: false,
  range1: 50
}

$scope.checkComplete = function () {
  console.log($scope.frankenVars);
  if($scope.frankenVars.rBox == "Check Me" && $scope.frankenVars.cBox1 && $scope.frankenVars.cBox2
    && !$scope.frankenVars.cBox3 && $scope.frankenVars.range1 > 60) {
      $scope.completeLevel();
  }
}
