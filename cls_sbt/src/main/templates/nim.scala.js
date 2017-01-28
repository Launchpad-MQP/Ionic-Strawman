$scope.sliders = [4, 3, 2];
$scope.max = 4;

function setSlider(i) {
  var value = $scope.sliders[i];
  var row = document.getElementsByName('row'+i)[0];
  row.cells[0].width = 100*i/$scope.max+'%';
  row.cells[1].innerHTML = value;
  var slider = document.getElementsByName('slider'+i)[0];
  slider.max = value;
  slider.value = value;
}

for (var i=0; i<$scope.sliders.length; i++) {
  setSlider(i);
}

$scope.callback = function(slider) {
  var i = parseInt(slider.slice(-1));
  $scope.sliders[i] = document.getElementsByName('slider'+i)[0].value;
  setSlider(i);

  for (var i=0; i<$scope.sliders.length; i++) {
    if ($scope.sliders[i] != 0) {
      return;
    }
  }
  $scope.completeLevel();
}