var sqlModule = angular.module("sql", ["ionic"])

.factory("sqlfactory", function() {
	
	//Dictionary of SQL functions to use.
	return {
		setupSQL : function() {
			console.log("Creating blank table if nonexistent");
			apidb.execute(db, "CREATE TABLE IF NOT EXISTS levels (number, state VARCHAR(50))");
		},
		
		resetSQL : function() {
			console.log("Dropping tables...");
			apidb.execute(db, "DROP TABLE IF EXISTS levels");
		},
		
		addLevel : function(num, state) {
		console.log ("Setting level "+num+" to "+state);
		apidb.execute(db, "INSERT INTO levels (number, state) VALUES (?, ?)", [num, state])
		.then(function(ret) {
			console.log("Set level "+num+" to "+state);
			for (var i in ret) {
				console.log("\t"+i+": ", ret[i]);
			}
			}, function (err) {console.log(err);});
		},
		
		deleteLevel : function(num) {
			console.log("Deleting level: "+num);
			apidb.execute(db, "DELETE FROM levels WHERE number=?", [num])
			.then(function(ret) {
				console.log("Deleted level: "+num);
				for (var i in ret) {
					try {
						console.log("\t"+i+": ", ret[i]);
					} catch (exc) {}
				}
			}, function (err) {console.log(err);});
		},
		
		getLevelState : function(num, callback) {
			console.log("Getting state for level: "+num);

			apidb.execute(db, "SELECT * FROM levels WHERE number=?", [num])
			.then(function(ret) {
				if (ret.rows.length == 0) {
					console.log("Could not find level: "+num);
					return;
				}
				callback(ret.rows.item(0));
			}, function(err) {console.log(err);});
		},

		setLevelState : function(num, state) {
			console.log("Setting state for level "+num+" to: "+state);

			apidb.execute(db, "UPDATE levels SET state=? WHERE number=?", [state, num])
			.then(function(ret) {
				console.log("Set state for level "+num+ " to: "+state);
				for (var i in ret) {
					try {
						console.log("\t"+i+": ", ret[i]);
					} catch (exc) {}
				}
			}, function(err) {console.log(err);});
		}
}
});
