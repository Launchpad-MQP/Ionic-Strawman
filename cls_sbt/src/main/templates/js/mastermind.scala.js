// Word to guess for each level
$scope.initializeLevel = function() {
  $scope.word = [
     "rest",  "cats",  "fate",  "hurt",  "note",
     "wolf",  "milk",  "yaks",  "know",  "sync",
    "spade", "exist", "group", "topic", "fruit",
    "movie", "style", "vital", "lynch", "rhythm"
  ][$scope.levelNum]
  console.log("Word for this level:", $scope.word)
  $scope.result = ""
}

$scope.makeGuess = function() {
  guess = document.getElementById("guess_"+$scope.levelNum).value
  console.log("Guess: "+guess)
  if (guess.length != $scope.word.length) {
    console.log(guess.length, "didn't match", $scope.word.length)
    $scope.result = "Please guess a word of length "+$scope.word.length+"."
  } else if (!dictionary.isWord(guess)) {
    console.log($scope.word, "invalid")
    $scope.result = $scope.word+" is not a valid word!"
  } else {
    console.log($scope.word, "valid")
    if (guess == $scope.word) {
      $scope.result = ""
      $scope.completeLevel(true)
    } else {
      matches = 0
      for (var letter in $scope.word) {
        if (guess.includes($scope.word[letter])) {
          matches++
        }
      }
      console.log(matches+" letters match between "+$scope.word+" and "+guess)
      $scope.result = matches+" matching letter"+(matches==1?"":"s")
    }
<<<<<<< Updated upstream
  }
=======
    console.log(matches+" letters match between "+$scope.word+" and "+guess)
    $scope.result = matches+" matching letter"+(matches==1?"":"s")
  }, function (err) {console.log(err)})
>>>>>>> Stashed changes
}
