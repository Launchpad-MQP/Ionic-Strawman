import de.tu_dortmund.cs.ls14.cls.interpreter.ReflectedRepository
import de.tu_dortmund.cs.ls14.cls.types.syntax._
import de.tu_dortmund.cs.ls14.cls.types._
import de.tu_dortmund.cs.ls14.cls.interpreter.combinator

import scala.collection.JavaConverters._
import ionicmqp2016._;
import _root_.java.nio.file._

object BlankApp extends App {
  trait BlankAppTrait {

    @combinator object MainPage {
      def apply(): String = {
        return """
<ion-view view-title="Main Page">
  <ion-header-bar class="bar bar-header bar-dark">
    <h1 class="title">Heineman's MQP Strawman App</h1>
  </ion-header-bar>
  <ion-content class="padding has-header has-footer">

    <div class="row">
      <div class="col" style="text-align: center">
        <p>The puzzle app built with combinators</p>
      </div>
    </div>
    <div class="row">
      <div class="col" style="text-align: center">
        <a class="button icon-right ion-chevron-right" href="#/level_select">PLAY &nbsp;</a>
      </div>
    </div>

    <div class="row">
      <div class="col" style="text-align: center">
        <a class="button icon-right ion-gear-b" href="#/settings">SETTINGS &nbsp;</a>
      </div>
    </div>
  </ion-content>

  <ion-footer-bar class="bar bar-footer bar-stable" align-title"left">
    <div class="buttons" ng-click="exitApp()">
      <button class="button button-assertive icon-left ion-close">&nbsp; EXIT</button>
    </div>
  </ion-footer-bar>
</ion-view>
"""
      }
      val semanticType:Type = 'mainFile
    }

    @combinator object LevelSelectSubPage {
      def apply(): String = {
        return """
<ion-view view-title="Level Select">
  <ion-header-bar class="bar bar-header bar-dark">
    <h1 class="title">Level Select</h1>
  </ion-header-bar>
  <ion-content class="has-header" padding="true">
    <div class="container">
      <!-- Here's where a bit of magic happens: Ionic will pupulate this table
        with all the levels in the "$scope.levels" variable, and it fills them
        according to the pattern we specified with {{level}}.
      -->
      <div class="row unlimited-items">
        <div class="col" ng-repeat="level in levels">
          <a class="button button-stable" ng-click="loadLevel({{level}})" id="level_{{level}}">{{level}}</a>
        </div>
      </div>
    </div>
  </div>

  </ion-content>
  <ion-footer-bar class="bar bar-footer bar-stable" align-title="left">
    <a class="button ion-home" href="#/main">&nbsp; BACK</a>
  </ion-footer-bar>
</ion-view>
"""
      }
      val semanticType:Type = 'levelSelectFile
    }

    @combinator object DummyLevelSubPage {
      def apply(): String = {
        return """
<ion-view view-title="Level {{levelNum}}">
  <ion-header-bar class="bar bar-header bar-dark">
    <h1 class="title">Level {{levelNum}}</h1>
  </ion-header-bar>
  <ion-content class="has-header" padding="true">
    <div class="col" style="text-align:center">
      <button class="button button-assertive levelBtn" ng-click="completeLevel()">Click Me!</button>
    </div>

  </ion-content>
  <ion-footer-bar class="bar bar-footer bar-stable" align-title="left">
    <a class="button ion-home" href="#/level_select">&nbsp; BACK</a>
    <button class="button button-calm" ng-click="restart()">&nbsp; RESTART</button>
  </ion-footer-bar>
</ion-view>
"""
      }
      val semanticType:Type = 'dummyLevelFile
    }

    @combinator object SettingsSubPage {
      def apply(): String = {
        return """
<ion-view view-title="Options">
  <ion-header-bar class="bar bar-header bar-dark">
    <h1 class="title">Options</h1>
  </ion-header-bar>
  <ion-content class="padding has-header">
    <div class="item range">
      MUSIC &nbsp;
      <i class="icon ion-volume-low"></i>
      <input type="range" name="music" min="0" max="100" value="100">
      <i class="icon ion-volume-high"></i>
    </div>

    <div class="item range">
      SFX &nbsp;
      <i class="icon ion-volume-low"></i>
      <input type="range" name="sfx" min="0" max="100" value="100">
      <i class="icon ion-volume-high"></i>
    </div>

    <div class="row">
      <button class="button button-assertive" ng-click="resetSQL()">Reset Progress</button>
    </div>
  </ion-content>

  <ion-footer-bar class="bar bar-footer bar-stable" align-title="left">
    <a class="button ion-home" href="#/main">&nbsp; BACK</a>
  </ion-footer-bar>
</ion-view>
"""
      }
      val semanticType:Type = 'settingsFile
    }

    @combinator object DummyController {
      def apply(): String = {
        return """
angular.module("dummy", ["ionic", "sql"])

.controller("LevelCtrl", function ($scope, $rootScope, $state, $stateParams, sqlfactory) {
  // Check for invalid state
  if ($rootScope.levels === undefined) {
    console.log("Level loaded but level list undefined, going to main")
    $state.go("main");
  } else if (!$rootScope.levels.includes($stateParams.levelNum)) {
    console.log("Went to level " + $stateParams.levelNum + " redirecting to level select.");
    $state.go("level_select");
  } else {
    console.log("Now in level: " + $stateParams.levelNum);
  }

  // Redefined so that it can be used in the HTML.
  $scope.levelNum = $stateParams.levelNum;

  // Begin: The entirety of our "game". Shows a button, which when clicked
  // beats the level. It also shows "back" and "next" options.
  $scope.completeLevel = function () {
    $rootScope.completeLevel($state, $stateParams.levelNum);
  }
  // End: The entirety of our "game".

  $scope.restart = function () {
    console.log("Restarting level...");
    $state.reload();
  }
});
"""
      }
      val semanticType:Type = 'dummyControllerFile
    }

    @combinator object DummyIndex {
      def apply(): String = {
        return """
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, width=device-width">
    <title></title>

    <link rel="manifest" href="manifest.json">

    <link href="lib/ionic/css/ionic.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">

    <!-- ionic/angularjs js -->
    <script src="lib/ionic/js/ionic.bundle.js"></script>
    <script src="lib/ngCordova/dist/ng-cordova.min.js"></script>

    <!-- cordova script (this will be a 404 during development) -->
    <script src="cordova.js"></script>

    <!-- your app's js -->
		<script src="js/sql.js"></script> <!-- Must load before controllers, they use sql. -->
    <script src="js/states.js"></script>
    <script src="js/controllers.js"></script>
    <script src="games/dummy.js"></script>
    <script src="js/app.js"></script>
  </head>
  <body ng-app="starter">
    <div ng-controller="MainCtrl">
      <ion-nav-view animation="slide-left-right"></ion-nav-view>
    </div>
  </body>
</html>
"""
      }
      val semanticType:Type = 'dummyIndexFile
    }

    @combinator object DummyApp {
      def apply(): String = {
        return """
/**
 * The main javascript file. Contains global variable definitions, and loads
 * other javascript templates.
**/

// Initialize SQL database and database access so that they
// are available at global scope
var db = undefined;
var apidb = undefined;

// A list of other javascript files to include
angular.module("starter", [
  "ionic", /* Base include for ionic */
  "states", /* State transitions between pages */
  "controllers", /* Individual page js */
  "ngCordova", /* Used for Cordova-SQLite */
  "dummy" /* Our game */
])

// Runs when the app is fully loaded.
.run(function ($ionicPlatform, $cordovaSQLite) {
  $ionicPlatform.ready( function () {
    // When running on mobile, hide the accessory bar by default.
    if (window.cordova && window.cordova.plugins && window.cordova.plugins.Keyboard) {
      cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
      cordova.plugins.Keyboard.disableScroll(true);
    }
    if (window.StatusBar) {
      // org.apache.cordova.statusbar required
      StatusBar.styleDefault();
    }

    if (window.cordova) { // If true, running in an emulator.
      db = $cordovaSQLite.openDB("levels.db");
    } else { // If false, running in a browser.
      db = window.openDatabase("levels.db", "1", "levels", 10000000);
    }

    apidb = $cordovaSQLite;

  });
});
"""
      }
      val semanticType:Type = 'dummyAppFile
    }

    @combinator object DummySql {
      def apply(): String = {
        return """
/**
 * This file holds all of the SQL interaction. In order to share functions
 * across other files, we use an Ionic factory.
 * This returns a dictionary of {name:function} for each function we want to
 * define.
**/

angular.module("sql", ["ionic"])

.factory("sqlfactory", function () {
  return {
    // Setup function. Should be called early, once the window loads.
    setupSQL: function () {
      console.log("Creating blank table if nonexistent");
      apidb.execute(db, "CREATE TABLE IF NOT EXISTS levels (number, state VARCHAR(50))");
    },

    // Reset function. Warning: You need to call setup again!
    resetSQL: function () {
      console.log("Dropping tables...");
      apidb.execute(db, "DROP TABLE IF EXISTS levels");
    },

    // Adds a level to the database. Won't overwrite if the level
    // already exists, use setLevelState for that.
    addLevel: function (num, state) {
    console.log ("Setting level "+num+" to "+state);
    apidb.execute(db, "INSERT INTO levels (number, state) VALUES (?, ?)", [num, state])
    .then(function (ret) {
      console.log("Set level "+num+" to "+state);
      for (var i in ret) {
        console.log("\t"+i+": ", ret[i]);
      }
      }, function (err) {console.log(err);});
    },

    // Removes a level from the database. Currently unused.
    deleteLevel: function (num) {
      console.log("Deleting level: "+num);
      apidb.execute(db, "DELETE FROM levels WHERE number=?", [num])
      .then(function (ret) {
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
    getLevelState: function (num, callback) {
      console.log("Getting state for level: "+num);
      apidb.execute(db, "SELECT * FROM levels WHERE number=?", [num])
      .then(function (ret) {
        if (ret.rows.length == 0) {
          console.log("Could not find level: "+num);
          return;
        }
        callback(ret.rows.item(0));
      }, function (err) {console.log(err);});
    },

    // Sets a level state. Again, the return from this is asynchronous, but at
    // present we don't need to do anything with it.
    setLevelState: function (num, state) {
      console.log("Setting state for level "+num+" to: "+state);
      apidb.execute(db, "UPDATE levels SET state=? WHERE number=?", [state, num])
      .then(function (ret) {
        console.log("Set state for level "+num+ " to: "+state);
        for (var i in ret) {
          try {
            console.log("\t"+i+": ", ret[i]);
          } catch (exc) {}
        }
      }, function (err) {console.log(err);});
    }
  }
});
"""
      }
      val semanticType:Type = 'dummySqlFile
    }

    @combinator object DummyGenericStates {
      def apply(): String = {
        return """
/**
 * A list of all the levels and states. This is what allows us to switch
 * between the different "pages". Each html page should have an associated
 * javascript controller, even if that controller is unused.
**/

angular.module("states", ["ionic"])

.config(function ($stateProvider, $urlRouterProvider){
  $stateProvider

  .state("main", {
    url: "/main",
    templateUrl: "templates/main.html",
    controller: "MainCtrl"
  })

  .state("level_select", {
    url: "/level_select",
    templateUrl: "templates/level_select.html",
    controller: "LevelSelectCtrl"
  })

  .state("level", {
    url: "/level/{levelNum:int}",
    templateUrl: "games/dummy.html",
    controller: "LevelCtrl"
  })

  .state("settings", {
    url: "/settings",
    templateUrl: "templates/settings.html",
    controller: "SettingsCtrl"
  });

  $urlRouterProvider.otherwise("/main");
});
"""
      }
      val semanticType:Type = 'dummyGenericStatesFile
    }

    @combinator object DummyGenericControllers {
      def apply(): String = {
        return """
/**
 * This file holds all the per "page" javascript functions.
 * Each of these should be (mostly) scoped to the page they operate on.
**/

angular.module("controllers", ["ionic", "sql"])

/* Controller for the main page */
.controller("MainCtrl", function ($scope) {
  console.log("Now in the Main Page");
})

/* Controller for the settings page. */
.controller("SettingsCtrl", function ($scope, $rootScope, sqlfactory) {
  console.log("Now in the Settings page");
  // Defining a local function so it can be used by ng-click
  $scope.resetSQL = function () {
    sqlfactory.resetSQL();
    sqlfactory.setupSQL();
    location.reload();
  }
})

/* Controller for the level select, aka list of levels */
.controller("LevelSelectCtrl", function ($scope, $rootScope, $state, $ionicPopup, sqlfactory) {
  console.log("Now in the Level Select");

  $scope.loadLevel = function (num) {
    console.log("Entering level " + num);
    $state.go("level", {"levelNum": num});
  }

  // Globally defined list of levels.
  $rootScope.levels = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
    11, 12, 13, 14, 15, 16, 17, 18, 19, 20];

  // Creates the database if it doesn't exist.
  sqlfactory.setupSQL();

  for (var i in $rootScope.levels) {
    // Adds a level if it doesn't exist, e.g. the database was just created.
    // Otherwise, this line has no effect, i.e. the level retains its state.
    sqlfactory.addLevel($rootScope.levels[i], "Unsolved");
  }

  // For each level, if it is completed we need to recolor the button.
  // This uses a callback function, since talking to SQL is an async operation.
  for (var i in $rootScope.levels) {
    sqlfactory.getLevelState($rootScope.levels[i], function (level) {
      console.log("Callback from getLevelState: ", level);
      if (level.state == "Solved") {
        button = document.getElementById("level_"+level.number);
        button.setAttribute("class", "button button-dark ng-binding");
      }
    });
  }

  $rootScope.completeLevel = function($state, levelNum) {
    button = document.getElementById("level_"+levelNum);
    button.setAttribute("class", "button button-dark ng-binding");
    sqlfactory.setLevelState(levelNum, "Solved");

    $ionicPopup.show({
      title: "Level Complete!",
      buttons: [
      {
        text: "Level Select",
        onTap: function () {
          console.log("Back to level select.");
          $state.go("level_select");
        }
      },
      {
        text: "Next",
        type: "button-positive",
        onTap: function () {
          console.log("On to the next level.");
          $state.go("level", {"levelNum": levelNum+1});
        }
      }]
    });
  }
});
"""
      }
      val semanticType:Type = 'controllers
    }

    class Bind(sym:Symbol, filePath:String) {
      def apply(expr:String) : Tuple = {
			  return new Tuple(expr, filePath)
      }
      val semanticType:Type = sym =>: 'BoundFile :&: sym
    }

    @combinator object BindDummyLevelPage extends Bind('dummyLevelFile, "www/games/dummy.html")
    @combinator object BindLevelSelectPage extends Bind('levelSelectFile, "www/templates/level_select.html")
    @combinator object BindMainPage extends Bind('mainFile, "www/templates/main.html")
    @combinator object BindDummyControllerPage extends Bind('dummyControllerFile, "www/games/dummy.js")
    @combinator object BindSettingsPage extends Bind('settingsFile, "www/templates/settings.html")
    @combinator object BindDummyCtrlsPage extends Bind('dummyGenericControllersFile, "www/js/controllers.js")
    @combinator object BindDummySettingsPage extends Bind('dummyGenericStatesFile, "www/js/states.js")
    @combinator object BindDummyAppPage extends Bind('dummyAppFile, "www/js/app.js")
    @combinator object BindDummySqlPage extends Bind('dummySqlFile, "www/js/sql.js")
    @combinator object BindIndexPage extends Bind('dummyIndexFile, "www/index.html")
  }

  // Initializes the CLS system
  val repository = new BlankAppTrait {}
  val reflectedRepository = ReflectedRepository (repository)

  // Get the interpreted response from CLS
  val reply = reflectedRepository.inhabit[Tuple] ('BoundFile)

  // Pass the response into our defined output, currently just a printer
  val it = reply.interpretedTerms.values.flatMap(_._2).iterator
  DirectoryMaker.parseResults(it.asJava)

}
