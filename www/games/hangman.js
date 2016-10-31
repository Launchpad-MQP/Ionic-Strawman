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
	$scope.words = [['h', 'e', 'l','l', 'o'], ['w', 'o', 'l', 'f'], ['r', 'a', 't']];
	$rootScope.letters = $scope.words[$scope.levelNum-1];
	
	var card = document.getElementById("hang-letters_" + $scope.levelNum);
	console.log(card);
	if(card!==null) {
		for(var l in $rootScope.letters) {
			console.log("Creating letter div");
			var letterdiv = document.createElement("div");
			console.log(letterdiv);
			letterdiv.className += " " + $rootScope.letters[l];
			var ltr = document.createTextNode($rootScope.letters[l]);
			letterdiv.append(ltr);
			letterdiv.style.color = "rgba(0, 0, 0, 0)";
			letterdiv.style.width = "30px";
			letterdiv.style.border = "thin solid black";
			letterdiv.style.backgroundColor = "powderblue";
			card.append(letterdiv);
		}
		
		var guessed = document.createElement("p");
		guessed.append("Guessed letters: ");
		card.append(guessed);
	}
	
	console.log(card);
	
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
					fields[i].style.color = "rgba(0, 0, 0, 1)";
					fields[i].className += " discovered";
				}
		}
		//var card = document.getElementById("hang-letters_" + $scope.levelNum);
		//card.append(guess + " ");
		$scope.checkComplete();
	}
	
  // Begin: The entirety of our "game". Shows a button, which when clicked
  // beats the level. It also shows "back" and "next" options.
  $scope.completeLevel = function () {
    $rootScope.completeLevel($state, $stateParams.levelNum);
  }
	
	$scope.checkComplete = function () {
		var correct = document.getElementsByClassName("discovered");
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