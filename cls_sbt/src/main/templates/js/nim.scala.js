function setSlider(i) {
  var value = $scope.sliders[i]
  var slider = document.getElementsByName('slider_'+$scope.levelNum+'_'+i)[0]
  slider.max = value
  slider.value = value
}

$scope.initializeLevel = function() {
  $scope.sliders = [1, 2, 3, 4, 5]

  for (var i=0; i<$scope.sliders.length; i++) {
    setSlider(i)
  }
}