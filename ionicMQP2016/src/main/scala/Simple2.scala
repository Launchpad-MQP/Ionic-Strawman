import de.tu_dortmund.cs.ls14.cls.interpreter.ReflectedRepository
import de.tu_dortmund.cs.ls14.cls.types.syntax._
import de.tu_dortmund.cs.ls14.cls.types._
import de.tu_dortmund.cs.ls14.cls.interpreter.combinator

import scala.collection.JavaConverters._
import ionicmqp2016._;
import _root_.java.nio.file._

object Simple2 extends App {
  trait Simple2Trait {

    // Defines a list of types. Basically the function prototypes. Includes parameters.

    @combinator object SQLvariables {
      def apply() : Array[String] = {
        return Array("""
// Initialize SQL database and database access so that they
// are available at global scope
var db = undefined;
var apidb = undefined;
""", """
    if (window.cordova) { // If true, running in an emulator.
      db = $cordovaSQLite.openDB("levels.db");
    } else { // If false, running in a browser.
      db = window.openDatabase("levels.db", "1", "levels", 10000000);
    }

    apidb = $cordovaSQLite;
""");
      }
      val semanticType:Type = 'variableArray;
    }

    class EmptyApp {
      def apply(initArray:Array[String]) : String = {
        return """
/**
 * Path: www/js/app.js
 * The main javascript file. Contains global variable definitions, and loads
 * other javascript templates.
**/
""" + initArray(0) + """
// A list of other javascript files to include
angular.module("starter", ["ionic"])

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
""" + initArray(1) + """
  });
});
""";
      }
      val semanticType:Type = 'variableArray =>: 'mainPage;
    }

    @combinator object Stupid extends EmptyApp()

  }

  // Initializes the CLS system
  val reflectedRepository = ReflectedRepository (new Simple2Trait {})

  // Get the interpreted response from CLS
  val reply = reflectedRepository.inhabit[String] ('mainPage)

  // Pass the response into our defined output, currently just a printer
  val it = reply.interpretedTerms.values.flatMap(_._2).iterator
  DirectoryMaker.parseResults(it.asJava)
  PrintFragments.processResults(it.asJava)

}
