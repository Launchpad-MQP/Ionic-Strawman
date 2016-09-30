/**
 * This file holds all of the SQL interaction. In order to share functions
 * across other files, we use an Ionic factory.
 * This returns a dictionary of {name:function} for each function we want to
 * define.
**/

var sqlModule = angular.module("sql", ["ionic"])

.factory("sqlfactory", function() {
  return {
    // Setup function. Should be called early, once the window loads.
    setupSQL : function() {
      console.log("Creating blank table if nonexistent");
      apidb.execute(db, "CREATE TABLE IF NOT EXISTS levels (number, state VARCHAR(50))");
    },

    // Reset function. Warning: You need to call setup again!
    resetSQL : function() {
      console.log("Dropping tables...");
      apidb.execute(db, "DROP TABLE IF EXISTS levels");
    },

    // Adds a level to the database. Won't overwrite if the level
    // already exists, use setLevelState for that.
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

    // Removes a level from the database. Currently unused.
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

    // Gets a level state. Warning: To get a return from this function, you'll
    // need to use a callback, which means passing in an anonymous function
    // that will be called when SQL returns.
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

    // Sets a level state. Again, the return from this is asynchronous, but at
    // present we don't need to do anything with it.
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
