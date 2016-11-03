angular.module("hangman", ["ionic", "sql"])

.controller("LevelCtrl", function ($scope, $rootScope, $state, $stateParams, $ionicPopup, sqlfactory) {
  // Check for invalid state
  if ($rootScope.levels === undefined) {
    console.log("Level loaded but level list undefined, going to main")
    $state.go("main");
  } else if (!$rootScope.levels.includes($stateParams.levelNum)) {
    console.log("Went to level " + $stateParams.levelNum + " redirecting to level select.");
    $state.go("level_select");
  } else {
    console.log("Now in level: " + $stateParams.levelNum);
  }

  // Redefined so that it can be used in the HTML.
  $scope.levelNum = $stateParams.levelNum;

  $scope.letters = ['hello', 'wolf', 'rat', 'xylophone'][$scope.levelNum-1].split('');

	$scope.makeGuess = function () {
		var guess = document.getElementById("letterguess_" + $scope.levelNum).value;
		console.log("Guess", guess);
		// Fields are the elements which match the letter guessed.
		var fields = document.getElementsByClassName(guess);
		console.log("Fields", fields);
		for(var i = 0; i<fields.length; i++) {
			if (!fields[i].className.includes("discovered")) {
				fields[i].className = "discovered " + $scope.levelNum + " " + guess;
				fields[i].innerHTML = guess;
			}
		}
		var card = document.getElementById("guessed_" + $scope.levelNum);
		// After the "Letters Guessed:", if our letter isn't included, add it.
		if(!card.innerHTML.slice(16).includes(guess)) {
			card.append(" " + guess);
		}

    // Find all elements which have been discovered on this level.
    var correct = document.getElementsByClassName("discovered " + $scope.levelNum);
    // If we have all of them, complete the level.
    if (correct.length == $scope.letters.length) {
		  $rootScope.completeLevel($state, $stateParams.levelNum);
		}
	}

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