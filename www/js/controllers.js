var app = angular.module("controllers", ["ionic"])

app.controller("MainCtrl", function($scope) {
  console.log("Main Controller says: Hello World");
})

.controller("LevelCtrl", function($scope, $state, $stateParams, $ionicPopup) {
	console.log($stateParams.levelNum);
	
	//Creates a popup when the level is complete.
	$scope.completeLevel = function() {
		$scope.data = {};
		
		var levelOverPopUp = $ionicPopup.show({
			title: "Level Complete!",
			scope: $scope,
			buttons: [
			{
				text: 'Level Select',
				onTap: function(e) {
					console.log("Back to level select.");
					$state.go('level_select');
				}
				},
			{
				text: 'Next',
				type: 'button-positive',
				onTap: function(e) {
					console.log("On to the next level.");
					$state.go('level', {'levelNum': $stateParams.levelNum+1});
				}
				}]
		});
	}
	
	//Restarts the level.
	$scope.restart = function() {
		console.log("Restarts level.");
		$state.reload();
	}
})

.controller("LevelSelectCtrl", function($scope, $state) {
	
	$scope.loadLevel = function(levelNum) {
		console.log("Entering level " + levelNum);
		$state.go("level", {'levelNum': levelNum});
	}
	
  var table = document.getElementsByClassName("levelTable")[0];
  var height = 5;
  var width = 4;
  for (var i=0; i<height; i+=1) {
    var row = table.insertRow(i);
    for (var j=0; j<width; j+=1) {
      var levelNo = i*width+j;
      var cell = row.insertCell(j);
      cell.setAttribute("name", j+"_"+i);
      var btn = document.createElement("button");
      btn.setAttribute("class", "button button-outline button-calm");
      btn.innerHTML = levelNo;
      // btn.setAttribute("onmousedown", "loadLevel("+levelNo+")");
      cell.appendChild(btn);
    }
  }
});