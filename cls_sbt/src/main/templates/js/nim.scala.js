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

$scope.callback = function(slider) {
  var i = parseInt(slider.split('_')[2])
  var slider = document.getElementsByName('slider_'+$scope.levelNum+'_'+i)[0]
  var value = parseInt(slider.value)
  if ($scope.sliders[i] == value) {
    return // User didn't change the slider
  } else {
    $scope.sliders[i] = value
  }
  setSlider(i)

  if ($scope.checkComplete()) {
    $scope.completeLevel(true)
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