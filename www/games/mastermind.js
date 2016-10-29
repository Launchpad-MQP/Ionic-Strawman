angular.module("mastermind", ["ionic", "sql"])

.controller("LevelCtrl", function ($scope, $rootScope, $state, $stateParams, $ionicPopup, sqlfactory, $http) {
  // Check for invalid level number
  if(!$rootScope.levels.includes($stateParams.levelNum)) {
    console.log("Went to level " + $stateParams.levelNum + " redirecting to level select.");
    $state.go("level_select");
  } else {
    console.log("Now in level: " + $stateParams.levelNum);
  }

  $scope.word = ["rest", "cats", "hurt", "wolf", "milk", "yaks"][$stateParams.levelNum-1];
  console.log("Word for this level:", $scope.word);

  // Redefined so that it can be used in the HTML.
  $scope.levelNum = $stateParams.levelNum;

  $scope.result = "";
  $scope.submit = function() {
    guess = document.getElementById("guess").value;
    if (guess.length != $scope.word.length) {
      console.log(guess.length, "didn't match", $scope.word.length);
      $scope.result = "Please guess a word of length "+$scope.word.length;
      return;
    }
    $http.get("https://owlbot.info/api/v1/dictionary/"+guess+"?format=json")
    .success(function(data, status, headers, config) {
      console.log("Got response:", data);
      if (data.length == 0) {
        console.log("No dictionary value, not a word");
        $scope.result = "Not a valid word!";
        return;
      }
      console.log("Valid word");
      if (guess == $scope.word) {
        $scope.result = "";
        $scope.completeLevel();
        return;
      }

      matches = 0;
      for (var letter in $scope.word) {
        if (guess.includes($scope.word[letter])) {
          matches++;
        }
      }
      console.log(matches+" letters match between "+$scope.word+" and "+guess);
      $scope.result = matches+" matching letter"+(matches==1?"":"s");
    }, function (err) {console.log(err);});
  }
  $scope.restart = function () {
    console.log("Restarting level...");
    $state.reload();
  }
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
});

// When a level is completed, find the appropriate button and make it gray.
function completeLevel (number, sqlfactory) {
  button = document.getElementById("level_"+number);
  button.setAttribute("class", "button button-dark ng-binding");

  sqlfactory.setLevelState(number, "Solved");
}