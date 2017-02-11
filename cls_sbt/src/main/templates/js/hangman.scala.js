$scope.myLetters = [
 'hello', 'wolf', 'rat', 'xylophone', 'attention',
 'school', 'ruling', 'poison', 'tree', 'prison',
 'abacus', 'toothache', 'short', 'bacon', 'crossroads',
 'darkness', 'candle', 'quadruple', 'extraordinary', 'declaration'
][$scope.levelNum-1].split("")
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
   if(!card.innerHTML.includes(guess)) {
     $scope.guessesLeft--
     card.append(guess + " ")
   }
   if($scope.guessesLeft==0)
     $scope.completeLevel(false)
 } else {
   if(!card.innerHTML.includes(guess))
     card.append(guess + " ")
   $scope.checkComplete()
 }
 guessBox.value = ""
 $scope.miss = true
}

$scope.checkComplete = function () {
 var correct = document.getElementsByClassName("discovered " + $scope.levelNum)
 console.log(correct.length)
 if(correct.length === $scope.myLetters.length)
   $scope.completeLevel(true)
}

$scope.beforeLeave = function() {
 var card = document.getElementById("guessed_" + $scope.levelNum)
 var guessed = card.innerHTML.split(", ")
 if($rootScope.states != undefined) {
   $rootScope.states[$stateParams.levelNum-1] = guessed
   sqlfactory.setLevelState($stateParams.levelNum, guessed)
 }
}

$scope.whenLose = function() {
 console.log("losing")
 document.getElementById("guessed_" + $scope.levelNum).innerHTML = "";
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

 if($rootScope.states == undefined) {
   $rootScope.states = []
   for(var i = 0; i < 20; i++)
     $rootScope.states.push(0);
 } else {
   sqlfactory.getLevelState($stateParams.levelNum, function(ret){
     console.dir(ret)
     if(ret != undefined && ret.state != 0 && ret.state != "Unsolved") {
       var lets = ret.state.split("")
       for(var j = 0; j < lets.length; j++) {
         guessBox.value = lets[j]
         console.log(lets[j])
         $scope.makeGuess()
       }
     }
   })
    // if($rootScope.states[$stateParams.levelNum-1] != 0){
      // var letters = $rootScope.states[$stateParams.levelNum-1]
     // for(var j = 0; j < letters.length; j++) {
       // guessBox.value = letters[j]
       // $scope.makeGuess()
     // }
   // }
 }
}
