angular.module("sql", ["ionic"]);

function setupSQL() {
  console.log("Creating blank table if nonexistent");
  apidb.execute(db, "CREATE TABLE IF NOT EXISTS levels (number, state VARCHAR(50))");
}

function resetSQL() {
  console.log("Dropping tables...");
  apidb.execute(db, "DROP TABLE IF EXISTS levels");
  setupSQL();
}

// FIXME: Rename
function addLevel(num, state) {
  console.log ("Setting level "+num+" to "+state);
  apidb.execute(db, "INSERT INTO levels (number, state) VALUES (?, ?)", [num, state])
  .then(function(ret) {
    console.log("Set level "+num+" to "+state);
    for (var i in ret) {
      console.log("\t"+i+": ", ret[i]);
    }
  }, function (err) {console.log(err);});
}

function deleteLevel(num) {
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
  }

function getLevelState(num, callback) {
  console.log("Getting state for level: "+num);

  apidb.execute(db, "SELECT * FROM levels WHERE number=?", [num])
  .then(function(ret) {
    if (ret.rows.length == 0) {
      console.log("Could not find level: "+num);
      return;
    }
    callback(ret.rows.item(0));
  }, function(err) {console.log(err);});
}

function setLevelState(num, state) {
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
