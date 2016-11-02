angular.module("hangman", ["ionic", "sql"])

.controller("LevelCtrl", function ($scope, $rootScope, $state, $stateParams, $ionicPopup, sqlfactory) {
  // Check for invalid level number
  if(!$rootScope.levels.includes($stateParams.levelNum)) {
    console.log("Went to level " + $stateParams.levelNum + " redirecting to level select.");
    $state.go("level_select");
  } else {
    console.log("Now in level: " + $stateParams.levelNum);
  }

  // Redefined so that it can be used in the HTML.
  $scope.levelNum = $stateParams.levelNum;
	console.log($scope.levelNum);
	$scope.stringList = ['hello', 'wolf', 'rat', 'xylophone'];
	$scope.words = [];
	for(var i = 0; i < $scope.stringList.length; i++) {
		$scope.words[i] = $scope.stringList[i].split("");
	}
	console.log($scope.words);
	$scope.myLetters = $scope.words[$scope.levelNum-1];
	$rootScope.letters = $scope.words[$scope.levelNum-1];
		
		var guessed = document.createElement("p");
		guessed.append("Guessed letters: ");
		//card.append(guessed);
	
	$scope.makeGuess = function () {
		console.log($rootScope.letters);
		var a = "letterguess_" + $scope.levelNum;
		var guess = document.getElementById("letterguess_" + $scope.levelNum).value;
		console.log(guess);
		var fields = document.getElementsByClassName(guess);
		console.log(fields);
		for(var i = 0; i<fields.length; i++) {
				//fields[i].style.visibility = "visible";
				if(!fields[i].className.includes("discovered")) {
					fields[i].className = "discovered " + $scope.levelNum + " " + guess;
				}
		}
		var card = document.getElementById("guessed_" + $scope.levelNum);
		if(!card.innerHTML.includes(guess))
			card.append(guess + " ");
		$scope.checkComplete();
	}
	
  // Begin: The entirety of our "game". Shows a button, which when clicked
  // beats the level. It also shows "back" and "next" options.
  $scope.completeLevel = function () {
    $rootScope.completeLevel($state, $stateParams.levelNum);
  }
	
	$scope.checkComplete = function () {
		var correct = document.getElementsByClassName("discovered " + $scope.levelNum);
		console.log(correct.length);
		console.log($rootScope.letters.length)
		if(correct.length === $rootScope.letters.length)
			$scope.completeLevel();
	}
	
  // End: The entirety of our "game".

  $scope.restart = function () {
    console.log("Restarting level...");
    $state.go($state.current, {}, {reload: true});
  }
});

// When a level is completed, find the appropriate button and make it gray.
function completeLevel (number, sqlfactory) {
  button = document.getElementById("level_"+number);
  button.setAttribute("class", "button button-dark ng-binding");

  sqlfactory.setLevelState(number, "Solved");
}