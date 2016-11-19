$scope.myLetters = [
  'hello', 'wolf', 'rat', 'xylophone', 'attention',
  'school', 'ruling', 'poison', 'tree', 'prison',
  'abacus', 'toothache', 'short', 'bacon', 'crossroads',
  'darkness', 'candle', 'quadruple', 'extraordinary', 'declaration'
][$scope.levelNum-1].split("");
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

$scope.checkComplete = function () {
  var correct = document.getElementsByClassName("discovered " + $scope.levelNum);
  console.log(correct.length);
  if(correct.length === $scope.myLetters.length)
    $scope.completeLevel();
}
