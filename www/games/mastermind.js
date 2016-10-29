angular.module("mastermind", ["ionic", "sql"])

.controller("LevelCtrl", function ($scope, $rootScope, $state, $stateParams, $ionicPopup, sqlfactory, $http) {
  // Check for invalid level number
  if(!$rootScope.levels.includes($stateParams.levelNum)) {
    console.log("Went to level " + $stateParams.levelNum + " redirecting to level select.");
    $state.go("level_select");
  } else {
    console.log("Now in level: " + $stateParams.levelNum);
  }

  $scope.word = ["rest", "cats", "hurt", "wolf", "kilt", "yaks"][$stateParams.levelNum-1];
  console.log("Word for this level:", $scope.word);

  // Redefined so that it can be used in the HTML.
  $scope.levelNum = $stateParams.levelNum;

  $scope.result = "";
  $scope.submit = function() {
    console.log("<20>");
    guess = document.getElementById("submit").value;
    if (guess.length != word.length) {
      console.log(guess.length, "didn't match", word.length);
      $scope.result = "Please guess a word of length "+word.length;
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
      matches = 0;
      for (var letter in $scope.word) {
        if (guess.includes(letter)) {
          matches++;
        }
      }
      console.log(matches, "letters match between", $scope.word, "and", guess);
      $scope.result = matches + " matching letters";
    }, function (err) {console.log(err);});
  }
});

// When a level is completed, find the appropriate button and make it gray.
function completeLevel (number, sqlfactory) {
  button = document.getElementById("level_"+number);
  button.setAttribute("class", "button button-dark ng-binding");

  sqlfactory.setLevelState(number, "Solved");
}