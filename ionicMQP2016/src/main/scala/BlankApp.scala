import de.tu_dortmund.cs.ls14.cls.interpreter.ReflectedRepository
import de.tu_dortmund.cs.ls14.cls.types.syntax._
import de.tu_dortmund.cs.ls14.cls.types._
import de.tu_dortmund.cs.ls14.cls.interpreter.combinator

import scala.collection.JavaConverters._
import ionicmqp2016._;
import _root_.java.nio.file._

object BlankApp extends App {
  trait BlankAppTrait {

    // variable to be used within semanticType
    val gameVar = Variable("gameName")
    lazy val kinding =
      Kinding(gameVar)
      .addOption('mastermind)
      .addOption('hangman)
      .addOption('lightsout)
      .addOption('dummy)

    @combinator object MastermindHTML {
      def apply(): String = {
        return """
    <div class="row">
      <input class="item item-input" id="guess_{{levelNum}}" placeholder="Guess a {{word.length}} letter word">
      &nbsp;
      <button class="button button-positive" ng-click="submit()">Submit</button>
    </div>
    <div class="row">
      <h4>{{result}}</h4>
    </div>"""
      }
      val semanticType:Type = 'mastermind :&: 'html
    }

    @combinator object MastermindJS {
      def apply(): String = {
        return """
    // Word to guess for each level
    $scope.word = [
       "rest",  "cats",  "fate",  "hurt",  "note",
       "wolf",  "milk",  "yaks",  "know",  "sync",
      "spade", "exist", "group", "topic", "fruit",
      "movie", "style", "vital", "lynch", "rythm"
    ][$stateParams.levelNum-1];
    console.log("Word for this level:", $scope.word);

    $scope.result = "";
    $scope.submit = function() {
      guess = document.getElementById("guess_"+$stateParams.levelNum).value;
      console.log("Guess: "+guess);
      if (guess.length != $scope.word.length) {
        console.log(guess.length, "didn't match", $scope.word.length);
        $scope.result = "Please guess a word of length "+$scope.word.length;
        return;
      }
      $http.get("https://owlbot.info/api/v1/dictionary/"+guess+"?format=json")
      .success(function(data, status, headers, config) {
        console.log("Got response:", data);
        if (data.length == 0) {
          console.log("No dictionary value, not a word");
          $scope.result = "Not a valid word!";
          return;
        }
        console.log("Valid word");
        if (guess == $scope.word) {
          $scope.result = "";
          $scope.completeLevel();
          return;
        }

        matches = 0;
        for (var letter in $scope.word) {
          if (guess.includes($scope.word[letter])) {
            matches++;
          }
        }
        console.log(matches+" letters match between "+$scope.word+" and "+guess);
        $scope.result = matches+" matching letter"+(matches==1?"":"s");
      }, function (err) {console.log(err);});
    }"""
      }
      val semanticType:Type = 'mastermind :&: 'js
    }

    @combinator object HangmanHTML {
      def apply(): String = {
        return """
		<div class="card">
			<div class= "item row">
				<div class= "col" ng-repeat="letter in myLetters track by $index">
					<div class= "guessable {{levelNum}} {{letter}}">{{letter}}</div>
				</div>
			</div>
			<p>Guessed Letters:</p>
			<div class = "row" id="guessed_{{levelNum}}"></div>
			<p>Tries Left: {{guessesLeft}}</p>
		</div>

		<ion-input class="item item-input item-stacked-label">
			<ion-label >Guess</ion-label>
			<input type="text" maxLength="1" class= "letterguess" id="letterguess_{{levelNum}}" placeholder="Type a letter here">
		</ion-input>

		<div class="col" style="text-align:center">
      <button class="button button-assertive levelBtn" ng-click="makeGuess()">Guess</button>
</div>"""
      }
      val semanticType:Type = 'hangman :&: 'html
    }

    @combinator object HangmanJs {
      def apply(): String = {
        return """
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
"""
      }
      val semanticType:Type = 'hangman :&: 'js
    }

    @combinator object LightsOutHTML {
      def apply(): String = {
        return """
    <div class="row" ng-repeat="row in buttons">
      <div class="col col-25"> </div>
      <div class="col" ng-repeat="button in row">
        <a class="button button-dark" ng-click="toggle('{{button}}')" id="{{levelNum}}_{{button}}">&nbsp</a>
      </div>
      <div class="col col-25"> </div>
    </div>"""
      }
      val semanticType:Type = 'lightsout :&: 'html
    }

    @combinator object LightsOutJS {
      def apply(): String = {
        return """
  // Redefined so that it can be used in the HTML.
  $scope.levelNum = $stateParams.levelNum;

  // Define buttons for use in HTML
  $scope.buttonsList = [
    [
      [0, 1, 0, 0, 0, 0],
      [1, 1, 1, 0, 0, 0],
      [0, 1, 0, 0, 0, 0],
      [0, 0, 0, 0, 0, 0],
      [0, 0, 0, 0, 0, 0],
      [0, 0, 0, 0, 0, 0],
    ],
    [
      [0, 1, 0, 0, 0, 0],
      [1, 1, 1, 0, 0, 0],
      [0, 0, 0, 0, 0, 0],
      [1, 1, 1, 0, 0, 0],
      [0, 1, 0, 0, 0, 0],
      [0, 0, 0, 0, 0, 0],
    ],
    [
      [0, 0, 0, 0, 0, 0],
      [0, 1, 1, 1, 0, 0],
      [1, 0, 1, 0, 1, 0],
      [0, 1, 1, 1, 0, 0],
      [0, 0, 1, 0, 0, 0],
      [0, 0, 0, 0, 0, 0],
    ],
    [
      [0, 0, 1, 0, 0, 0],
      [0, 0, 1, 0, 0, 0],
      [1, 1, 0, 1, 1, 0],
      [0, 0, 1, 0, 0, 0],
      [0, 0, 1, 0, 0, 0],
      [0, 0, 0, 0, 0, 0],
    ],
    [
      [0, 0, 0, 0, 0, 0],
      [0, 1, 0, 0, 1, 0],
      [1, 1, 0, 0, 1, 1],
      [0, 0, 0, 0, 0, 0],
      [0, 0, 1, 1, 0, 0],
      [0, 0, 0, 0, 0, 0],
    ],
    [
      [0, 0, 0, 0, 1, 0],
      [1, 0, 0, 0, 0, 0],
      [0, 0, 0, 0, 0, 0],
      [0, 0, 0, 0, 0, 0],
      [1, 0, 0, 0, 0, 0],
      [1, 0, 0, 0, 1, 1],
    ],
    [
      [0, 0, 0, 0, 0, 0],
      [0, 0, 0, 0, 0, 0],
      [0, 0, 0, 0, 1, 0],
      [0, 0, 0, 0, 0, 0],
      [0, 0, 0, 0, 0, 0],
      [0, 0, 0, 0, 0, 0],
    ],
    [
      [0, 0, 0, 0, 0, 0],
      [0, 0, 0, 0, 1, 0],
      [0, 0, 0, 1, 0, 1],
      [0, 0, 0, 0, 1, 0],
      [0, 0, 0, 0, 0, 0],
      [0, 0, 0, 0, 0, 0],
    ],
    [
      [0, 0, 0, 0, 0, 0],
      [1, 1, 0, 0, 0, 0],
      [0, 0, 1, 0, 0, 0],
      [1, 1, 0, 0, 0, 0],
      [0, 0, 0, 0, 0, 0],
      [0, 0, 0, 0, 0, 0],
    ],
    [
      [0, 0, 0, 0, 0, 0],
      [0, 0, 1, 0, 0, 0],
      [0, 0, 1, 1, 0, 0],
      [1, 1, 0, 0, 0, 0],
      [0, 1, 0, 0, 0, 0],
      [0, 0, 0, 0, 0, 0],
    ]
  ][$stateParams.levelNum-1];

  // Used by the HTML to name the buttons
  $scope.buttons = [
    ["0_0", "0_1", "0_2", "0_3", "0_4", "0_5"],
    ["1_0", "1_1", "1_2", "1_3", "1_4", "1_5"],
    ["2_0", "2_1", "2_2", "2_3", "2_4", "2_5"],
    ["3_0", "3_1", "3_2", "3_3", "3_4", "3_5"],
    ["4_0", "4_1", "4_2", "4_3", "4_4", "4_5"],
    ["5_0", "5_1", "5_2", "5_3", "5_4", "5_5"]
  ];

  // This runs whenever a level is entered
  $scope.$on("$ionicView.afterEnter", function(scopes, states){
    console.log("Entered "+states.stateName+" "+$stateParams.levelNum);
    $scope.initializeLevel();
  });

  $scope.initializeLevel = function () {
    for (var row=0; row < $scope.buttonsList.length; row++) {
      for (var col=0; col < $scope.buttonsList[0].length; col++) {
        var name = $stateParams.levelNum + "_" + row + "_" + col;
        var button = document.getElementById(name);
        if (button === null) {
          console.log("Couldn't find button "+name);
        } else if ($scope.buttonsList[row][col] == 1) {
          button.className = "button button-energized";
        } else {
          button.className = "button button-dark";
        }
      }
    }
  }

  $scope.toggleButton = function (row, col) {
    var name = $stateParams.levelNum + "_" + row + "_" + col;
    var button = document.getElementById(name);
    if (button === null) {
      return;
    }
    //console.log(button.className);
    if (button.className.includes("button-energized")) {
      button.className = "button button-dark";
    } else if (button.className.includes("button-dark")) {
      button.className = "button button-energized";
    }
  }

  // Logic for toggling lights.
  $scope.toggle = function (button_name) {
    // All of our buttons are named "#_#", so if the table was 10 rows or 10
    // columns, this logic would need to change.
    var row = parseInt(button_name.charAt(0));
    var col = parseInt(button_name.charAt(2));

    $scope.toggleButton(row, col);
    $scope.toggleButton(row, col+1);
    $scope.toggleButton(row, col-1);
    $scope.toggleButton(row+1, col);
    $scope.toggleButton(row-1, col);

    // Check for game completion
    for (var row=0; row < $scope.buttonsList.length; row++) {
      for (var col=0; col < $scope.buttonsList[0].length; col++) {
        var name = $stateParams.levelNum + "_" + row + "_" + col;
        var button = document.getElementById(name);
        if (button.className.includes("button-energized")) {
          // Found a button which wasn't off, level not complete
          return;
        }
      }
    }
    console.log("All lights out, completing level.")
    $rootScope.completeLevel($state, $stateParams.levelNum);
  }"""
      }
      val semanticType:Type = 'lightsout :&: 'js
    }

    @combinator object DummyHTML {
      def apply(): String = {
        return """
    <div class="col" style="text-align:center">
      <button class="button button-assertive levelBtn" ng-click="completeLevel()">Click Me!</button>
    </div>"""
      }
      val semanticType:Type = 'dummy :&: 'html
    }

    @combinator object DummyJS {
      def apply(): String = {
        return ""
      }
      val semanticType:Type = 'dummy :&: 'js
    }

    @combinator object GameHTML {
      def apply(contents: String): String = {
        return """
<ion-view view-title="Level {{levelNum}}">
  <ion-header-bar class="bar bar-header bar-dark">
    <h1 class="title">Level {{levelNum}}</h1>
  </ion-header-bar>
  <ion-content class="has-header" padding="true">
""" + contents + """
  </ion-content>
  <ion-footer-bar class="bar bar-footer bar-stable" align-title="left">
    <a class="button ion-home" href="#/level_select">&nbsp; BACK</a>
    <button class="button button-calm" ng-click="restart()">&nbsp; RESTART</button>
  </ion-footer-bar>
</ion-view>
"""
      }
      val semanticType:Type = gameVar :&: 'html =>: gameVar :&: 'gameHtml
    }

    @combinator object GameJs {
      def apply(contents:String): String = {
        return """
angular.module("game", ["ionic", "sql"])

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

""" + contents + """

  $scope.completeLevel = function () {
    $rootScope.completeLevel($state, $stateParams.levelNum);
  }

  $scope.restart = function () {
    console.log("Restarting level...");
    $state.reload();
  }
});
"""
      }
      val semanticType:Type = gameVar :&: 'js =>: gameVar :&: 'gameJs
    }

    @combinator object IndexHTML {
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
    <script src="js/game.js"></script>
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
      val semanticType:Type = 'indexHtml
    }

    @combinator object MainPage {
      def apply(gameTitle:String): String = {
        return """
<ion-view view-title="Main Page">
  <ion-header-bar class="bar bar-header bar-dark">
    <h1 class="title">""" + gameTitle + """</h1>
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
      val semanticType:Type = gameVar :&: 'gameTitle =>: 'mainPage :&: gameVar
    }

    @combinator object LightsOutTitle {
      def apply(): String = {
        return "Lights Out"
      }
      val semanticType:Type = 'lightsout :&: 'gameTitle
    }

    @combinator object HangmanTitle {
      def apply(): String = {
        return "Hangman"
      }
      val semanticType:Type = 'hangman :&: 'gameTitle
    }

    @combinator object MastermindTitle {
      def apply(): String = {
        return "Word Mastermind"
      }
      val semanticType:Type = 'mastermind :&: 'gameTitle
    }

    @combinator object DummyTitle {
      def apply(): String = {
        return "Dummy App"
      }
      val semanticType:Type = 'dummy :&: 'gameTitle
    }

    @combinator object LevelSelect {
      def apply(): String = {
        return """
<ion-view view-title="Level Select">
  <ion-header-bar class="bar bar-header bar-dark">
    <h1 class="title">Level Select</h1>
  </ion-header-bar>
  <ion-content class="has-header" padding="true">
    <div class="container">
      <!-- Here's where a bit of magic happens: Ionic will populate this table
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
      val semanticType:Type = 'levelSelect
    }

    @combinator object Settings {
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
      val semanticType:Type = 'settings
    }

    @combinator object AppJs {
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
  "game" /* Our game */
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
      val semanticType:Type = 'appJs
    }

    @combinator object SQL {
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
      val semanticType:Type = 'sql
    }

    @combinator object States {
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
    templateUrl: "templates/game.html",
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
      val semanticType:Type = 'states
    }


    @combinator object LevelList {
      def apply(): String = {
        return """
        $rootScope.levels = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
            11, 12, 13, 14, 15, 16, 17, 18, 19, 20];
            """
      }
      val semanticType:Type = 'levelList
    }

    @combinator object Controllers {
      def apply(levelList:String): String = {
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

  """ + levelList + """

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
      val semanticType:Type = 'levelList =>: 'controllers
    }

    class Bind(sym:Symbol, filePath:String) {
      def apply(expr:String) : Tuple = {
			  return new Tuple(expr, filePath)
      }
      val semanticType:Type = sym =>: 'BoundFile :&: sym :&: gameVar
    }

    @combinator object Bind0 extends Bind('indexHtml, "www/index.html")

    @combinator object Bind1 extends Bind('states, "www/js/states.js")
    @combinator object Bind2 extends Bind('appJs, "www/js/app.js")
    @combinator object Bind3 extends Bind('controllers, "www/js/controllers.js")
    @combinator object Bind4 extends Bind('sql, "www/js/sql.js")

    @combinator object Bind7 extends Bind('levelSelect, "www/templates/level_select.html")
    @combinator object Bind9 extends Bind('settings, "www/templates/settings.html")

    class GameBind(sym:Symbol, filePath:String) {
      def apply(expr:String) : Tuple = {
			  return new Tuple(expr, filePath)
      }
      val semanticType:Type = sym :&: gameVar =>: 'BoundFile :&: sym :&: gameVar
    }

    @combinator object Bind5 extends GameBind('gameHtml, "www/templates/game.html")
    @combinator object Bind6 extends GameBind('gameJs, "www/js/game.js")
    @combinator object Bind8 extends GameBind('mainPage, "www/templates/main.html")
  }

  // Initializes the CLS system
  val repository = new BlankAppTrait {}
  val reflectedRepository = ReflectedRepository (repository, kinding=repository.kinding)

  // Get the interpreted response from CLS
  val reply = reflectedRepository.inhabit[Tuple] ('BoundFile :&: 'lightsout)

  // Pass the response into our defined output, currently just a printer
  val iter = reply.interpretedTerms.values.flatMap(_._2).iterator.asJava
  DirectoryMaker.printResults(iter)
}
