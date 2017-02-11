$scope.myLetters = [
  'hello', 'wolf', 'rat', 'xylophone', 'attention',
  'school', 'ruling', 'poison', 'tree', 'prison',
  'abacus', 'toothache', 'short', 'bacon', 'crossroads',
  'darkness', 'candle', 'quadruple', 'extraordinary', 'declaration'
][$scope.levelNum].split("")
$scope.guessesLeft = 7
$scope.miss = true

$scope.makeGuess = function () {
	var guessBox = document.getElementById("letterguess_" + $scope.levelNum)
  var guess = guessBox.value
  var card = document.getElementById("guessed_" + $scope.levelNum)

  console.log("Guess", guess)
  // Fields are the elements which match the letter guessed.
  var fields = document.getElementsByClassName(guess)
  console.log("Fields", fields)
  for(var i = 0; i<fields.length; i++) {
      if(!fields[i].className.includes("discovered")) {
        fields[i].className = "discovered " + $scope.levelNum + " " + guess
      }
      $scope.miss = false
  }
  if($scope.miss) {
    if(!card.innerHTML.includes(guess))
      $scope.guessesLeft--
    if($scope.guessesLeft==0)
      $scope.completeLevel(false)
  }

  $scope.miss = true
  if(!card.innerHTML.includes(guess))
    card.append(guess + " ")
  $scope.checkComplete()
	guessBox.value = ""
}

$scope.checkComplete = function () {
  var correct = document.getElementsByClassName("discovered " + $scope.levelNum)
  console.log(correct.length)
  if(correct.length === $scope.myLetters.length)
    $scope.completeLevel(true)
}

$scope.beforeLeave = function() {
	var card = document.getElementById("guessed_" + $scope.levelNum)
	var guessed = card.innerHTML.split("")
  sqlfactory.setLevelState($stateParams.levelNum, guessed)
}

$scope.initializeLevel = function() {
	$scope.guessesLeft = 7
	$scope.miss = true
	var card = document.getElementById("guessed_" + $scope.levelNum)
	var guessBox = document.getElementById("letterguess_" + $scope.levelNum)
	card.innerHTML = ""
	guessBox.value = ""
	var fields = document.getElementsByClassName($scope.levelNum)
	for(var i = 0; i<fields.length; i++) {
		var letter = fields[i].className.slice(-1)
		fields[i].className = "guessable " + $scope.levelNum + " " + letter
	}

  if ($rootScope.levelData[$stateParams.levelNum]["state"] != "Unsolved") {
	  var letters = $rootScope.levelData[$stateParams.levelNum]["state"]
	  for (var j = 0; j < letters.length; j++) {
	    guessBox.value = letters[j]
	    $scope.makeGuess()
	  }
	}
}
