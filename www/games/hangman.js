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
	$rootScope.letters = ['h', 'e', 'l','l', 'o'];
	var card = document.getElementById("hang-letters");
	for(var l in $rootScope.letters) {
		var thingy = document.createElement("div");
		var thingy2 = document.createTextNode($rootScope.letters[l]);
		thingy.append(thingy2);
		thingy.className += " " + $rootScope.letters[l];
		thingy.style.visibility = "hidden";
		card.appendChild(thingy);
	}
	$scope.guess = 3;
	
	$scope.removeOne = function () {
		console.log($rootScope.letters);
		var guess = document.getElementById("letterguess").value;
		console.log(guess);
		var fields = document.getElementsByClassName(guess);
		console.log(fields);
		for(var i = 0; i<fields.length; i++) {
				fields[i].style.visibility = "visible";
		}
	}
	
  // Begin: The entirety of our "game". Shows a button, which when clicked
  // beats the level. It also shows "back" and "next" options.
  $scope.completeLevel = function () {
    completeLevel($stateParams.levelNum, sqlfactory);

    var levelOverPopUp = $ionicPopup.show({
      title: "Level Complete!",
      scope: $scope,
      buttons: [
      {
        text: "Level Select",
        onTap: function (e) {
          console.log("Back to level select.");
          $state.go("level_select");
        }
      },
      {
        text: "Next",
        type: "button-positive",
        onTap: function (e) {
          console.log("On to the next level.");
          $state.go("level", {"levelNum": $stateParams.levelNum+1});
        }
      }]
    });
  }
  // End: The entirety of our "game".

  $scope.restart = function () {
    console.log("Restarting level...");
    $state.reload();
  }
});

// When a level is completed, find the appropriate button and make it gray.
function completeLevel (number, sqlfactory) {
  button = document.getElementById("level_"+number);
  button.setAttribute("class", "button button-dark ng-binding");

  sqlfactory.setLevelState(number, "Solved");
}