var app = angular.module("controllers", ["ionic"])

app.controller("MainCtrl", function($scope) {
  console.log("Now in the Main Page");
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
  console.log("Now in the Level Select");

	$scope.loadLevel = function(levelNum) {
		console.log("Entering level " + levelNum);
		$state.go("level", {'levelNum': levelNum});
	}

    $scope.myTitle = 'Template';
    $scope.levels = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20];
});
