import de.tu_dortmund.cs.ls14.cls.interpreter.ReflectedRepository
import de.tu_dortmund.cs.ls14.cls.types.syntax._
import de.tu_dortmund.cs.ls14.cls.types._
import de.tu_dortmund.cs.ls14.cls.interpreter.combinator

import scala.collection.JavaConverters._
import ionicmqp2016._;
import _root_.java.nio.file._

object BlankApp extends App {
  trait BlankAppTrait {

    @combinator object Controllers {
      def apply(subPages:Array[String]) : String = {
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
          templateJs = """
/**
 * Path: www/js/"""+subPage+""".js
**/

angular.module(\""""+subPage+"""\", ["ionic"])

"""+templateJs+"""
.controller(\""""+subPage.capitalize+"""Ctrl", function ($scope) {
  console.log("Now in the """+subPage.capitalize+"""\");
})
"""
        }
        return templateJs;
      }
      val semanticType:Type = 'controllers
    }

    class Blank {
      def apply(subPages:Array[String]) : String = {
        var modules = "\"ionic\""
        var scripts = "\t\t<script src=\"js/app.js\"></script>"
        for (subPage <- subPages) {
          modules += ", \""+subPage+"\""
          scripts += "\n\t\t<script src=\"js/"+subPage+".js\"></script>"
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

<!--
  Path: www/templates/index.html
  The main page. Loads all javascript and stylesheets,
-->
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
      val semanticType:Type = 'app :&: 'blank
    }

    @combinator object BlankApp extends Blank()
    @combinator object SimpleApp extends Blank()
  }

  // Initializes the CLS system
  val reflectedRepository = ReflectedRepository (new BlankAppTrait {})

  // Get the interpreted response from CLS
  val reply = reflectedRepository.inhabit[String] ('app)

  // Pass the response into our defined output, currently just a printer
  val it = reply.interpretedTerms.values.flatMap(_._2).iterator
  PrintFragments.processResults(it.asJava)

}