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

.controller("LevelSelectCtrl", function($scope, $state, $cordovaSQLite) {
  console.log("Now in the Level Select");

	$scope.loadLevel = function(levelNum) {
    console.log("Entering level " + levelNum);
    $state.go("level", {'levelNum': levelNum});
  }

  if (window.cordova) {
    db = $cordovaSQLite.openDB("test.db");
  } else {
    db = window.openDatabase("mytest.db", '1', 'mytest', 1024*1024*5);
  }
  $scope.levels = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20];
  var query = "SELECT * FROM levels";
  $cordovaSQLite.execute(db, query, []).then(function(res) {
    console.log("done");
    if(res.rows.length > 0) {
      console.log("SELECTED -> " + res.rows.item(0).num + " " + res.rows.item(0).state);
      for(i = 0; i < res.rows.length; i++){
        console.log(res.rows.item(i));
        $scope.levels.push(res.rows.item(i).num);
      }
    } else {
      console.log("No results found");
    }
  }, function (err) {
      console.error(err);
  });
  console.log("abc");

    $scope.myTitle = 'Template';
});
