import de.tu_dortmund.cs.ls14.cls.interpreter.ReflectedRepository
import de.tu_dortmund.cs.ls14.cls.types.syntax._
import de.tu_dortmund.cs.ls14.cls.types._
import de.tu_dortmund.cs.ls14.cls.interpreter.combinator

import scala.collection.JavaConverters._
import ionicmqp2016._;
import _root_.java.nio.file._

object BlankApp extends App {
  trait BlankAppTrait {

    @combinator object SimpleListOfStates {
      def apply() : Array[String] = {
        return Array("main", "level_select", "level", "settings")
      }
      val semanticType:Type = 'stateList
    }

    @combinator object BlankListOfStates {
      def apply : Array[String] = {
        return Array()
      }
      val semanticType:Type = 'stateList :&: 'blank
    }

    @combinator object Controllers {
      def apply(subPages:Array[String]) : String = {
        var stateList = ""
        for (subPage <- "main" +: subPages) {
          stateList += """
  .state(\""""+subPage.capitalize+"""\", {
    url: "/\""""+subPage+"""\",
    templateUrl: "templates/"""+subPage+""".html",
    controller: \""""+subPage.capitalize+"""Ctrl"
  })
"""
        }
        var stateJs = """
/**
 * Path: www/js/states.js
 * A list of all the levels and states. This is what allows us to switch
 * between the different "pages". Each html page should have an associated
 * javascript controller, even if that controller is unused.
**/

angular.module("states", ["ionic"])

.config(function ($stateProvider, $urlRouterProvider){
  $stateProvider
""" + stateList + """
  $urlRouterProvider.otherwise("/main");
});
"""
        var controllerJs = """
/**
 * Path: www/js/controllers.js
 * This file holds all the per "page" javascript functions.
 * Each of these should be (mostly) scoped to the page they operate on.
**/

angular.module("controllers", ["ionic"])
"""
        for (subPage <- subPages) {
          controllerJs = """
/**
 * Path: www/js/"""+subPage+""".js
**/

angular.module(\""""+subPage+"""\", ["ionic"])

"""+controllerJs+"""
.controller(\""""+subPage.capitalize+"""Ctrl", function ($scope) {
  console.log("Now in the """+subPage.capitalize+"""\");
})
"""
        }
        return controllerJs
      }
      val semanticType:Type = 'stateList =>: 'controllers
    }

    @combinator object MainSubPage {
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

    Array("level", "settings")
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

    @combinator object App {
      def apply(subPages:Array[String]) : String = {
        var modules = "\"ionic\""
        var scripts = "    <script src=\"js/app.js\"></script>"
        for (subPage <- subPages) {
          modules += ", \""+subPage+"\""
          scripts += "\n    <script src=\"js/"+subPage+".js\"></script>"
        }
        return """
/**
 * Path: www/js/app.js
 * The main javascript file. Contains global variable definitions, and loads
 * other javascript templates.
**/

// A list of other javascript files to include
angular.module("starter", ["""+modules+"""])

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
  });
});

/**
 * Path: www/index.html
 * The main page. Loads all javascript and stylesheets,
**/
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

    <!-- cordova script (this will be a 404 during development) -->
    <script src="cordova.js"></script>

    <!-- your app's js -->
"""+scripts+"""
  </head>
  <body ng-app="starter">

    <ion-pane>
      <ion-header-bar class="bar-stable">
        <h1 class="title">Ionic Blank Starter</h1>
      </ion-header-bar>
      <ion-content>
      </ion-content>
    </ion-pane>
  </body>
</html>
"""
      }
      val semanticType:Type = 'stateList =>: 'app
    }

        @combinator object BlankApp {
      def apply(subPages:Array[String]) : String = {
        var modules = "\"ionic\""
        var scripts = "    <script src=\"js/app.js\"></script>"
        for (subPage <- subPages) {
          modules += ", \""+subPage+"\""
          scripts += "\n    <script src=\"js/"+subPage+".js\"></script>"
        }
        return """
/**
 * Path: www/js/app.js
 * The main javascript file. Contains global variable definitions, and loads
 * other javascript templates.
**/

// A list of other javascript files to include
angular.module("starter", ["""+modules+"""])

// Runs when the app is fully loaded.
.run(function ($ionicPlatform) {
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
  });
});

/**
 * Path: www/index.html
 * The main page. Loads all javascript and stylesheets,
**/
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

    <!-- cordova script (this will be a 404 during development) -->
    <script src="cordova.js"></script>

    <!-- your app's js -->
"""+scripts+"""
  </head>
  <body ng-app="starter">

    <ion-pane>
      <ion-header-bar class="bar-stable">
        <h1 class="title">Ionic Blank Starter</h1>
      </ion-header-bar>
      <ion-content>
      </ion-content>
    </ion-pane>
  </body>
</html>
"""
      }
      val semanticType:Type = 'stateList :&: 'blank =>: 'app :&: 'blank
    }

    /*class StringConcatenator(typeA:Type, typeB:Type, typeC:Type) {
      def apply(a:String, b:String) : String = {
        return a + '\n' + b;
      }
      val semanticType:Type = typeA =>: typeB =>: typeC;
    }

    @combinator object Comb1 extends StringConcatenator('app, 'controllers, 'dummy)
    */
    class Bind(sym:Symbol, filePath:String) {
      def apply(expr:String) : Tuple = {
			  return new Tuple(expr, filePath)
      }
      val semanticType:Type = sym =>: 'BoundFile :&: sym
    }

    //@combinator object BindIndex extends Bind('indexFile, "www/index.html")
    //@combinator object BinApp extends Bind('basicJavascript, "www/js/app.js")
    //@combinator object BindController extends Bind('controllersFile, "www/js/controllers.js")
    //@combinator object BindStates extends Bind('statesFile, "www/js/states.js")
    //@combinator object BindSubPage extends Bind('statesFile, "www/js/states.js")
    @combinator object BindDummyLevelPage extends Bind('dummyLevelFile, "www/games/dummy.html")
    @combinator object BindLevelSelectPage extends Bind('levelSelectFile, "www/templates/level_select.html")
    @combinator object BindMainPage extends Bind('mainFile, "www/templates/main.html")
  }

  // Initializes the CLS system
  val repository = new BlankAppTrait {}
  val reflectedRepository = ReflectedRepository (repository)

  // Get the interpreted response from CLS
  //val reply = reflectedRepository.inhabit[String] ('app :&: 'blank)
  val reply = reflectedRepository.inhabit[Tuple] ('BoundFile)

  
  // Pass the response into our defined output, currently just a printer
  val it = reply.interpretedTerms.values.flatMap(_._2).iterator
  //PrintFragments.processResults(it.asJava)
  DirectoryMaker.parseResults(it.asJava)
  //FragmentExplorer.processResultsTuple(it.asJava)

}
