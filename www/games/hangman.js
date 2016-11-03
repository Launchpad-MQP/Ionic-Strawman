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
	console.log($scope.levelNum);
	$scope.myLetters = ['hello', 'wolf', 'rat', 'xylophone', 'attention',
																'school', 'ruling', 'poison', 'tree', 'prison',
																'abacus', 'toothache', 'short', 'bacon', 'crossroads',
																'darkness', 'candle', 'quadruple', 'extraordinary', 'declaration'][$scope.levelNum-1].split("");
	$scope.guessesLeft = 7;
	$scope.miss = true;
	
	$scope.makeGuess = function () {
		var guess = document.getElementById("letterguess_" + $scope.levelNum).value;
		var card = document.getElementById("guessed_" + $scope.levelNum);
		
		console.log("Guess", guess);
		// Fields are the elements which match the letter guessed.
		var fields = document.getElementsByClassName(guess);
		console.log("Fields", fields);
		for(var i = 0; i<fields.length; i++) {
				if(!fields[i].className.includes("discovered")) {
					fields[i].className = "discovered " + $scope.levelNum + " " + guess;
				}
				$scope.miss = false;
		}
		if($scope.miss) {
			if(!card.innerHTML.includes(guess))
				$scope.guessesLeft--;
			if($scope.guessesLeft===0)
				$scope.loseLevel();
		}
		
		$scope.miss = true;
		if(!card.innerHTML.includes(guess))
			card.append(guess + " ");
		$scope.checkComplete();
	}
	
	$scope.loseLevel = function() {
		$ionicPopup.show({
      title: "You lose!",
      buttons: [
      {
        text: "Level Select",
        onTap: function () {
          console.log("Back to level select.");
          $state.go("level_select");
        }
      },
      {
        text: "Retry",
        type: "button-positive",
        onTap: function () {
          console.log("Reloading...");
          $scope.restart();
        }
      }]
    });
	}
	
  $scope.completeLevel = function () {
    $rootScope.completeLevel($state, $stateParams.levelNum);
  }
	
	$scope.checkComplete = function () {
		var correct = document.getElementsByClassName("discovered " + $scope.levelNum);
		console.log(correct.length);
		if(correct.length === $scope.myLetters.length)
			$scope.completeLevel();
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