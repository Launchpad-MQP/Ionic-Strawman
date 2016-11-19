import de.tu_dortmund.cs.ls14.cls.interpreter.ReflectedRepository
import de.tu_dortmund.cs.ls14.cls.types.syntax._
import de.tu_dortmund.cs.ls14.cls.types._
import de.tu_dortmund.cs.ls14.cls.interpreter.combinator

import scala.collection.JavaConverters._
import ionicmqp2016._;
import _root_.java.nio.file._

object AppCreator extends App {
  trait AppTrait {

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
        return js.mastermind.render().toString()
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
        return js.hangman.render().toString()
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
        return js.lightsout.render().toString()
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
      def apply(contents:String): String = {
        return html.game.render(contents).toString()
      }
      val semanticType:Type = gameVar :&: 'html =>: gameVar :&: 'gameHtml
    }

    @combinator object GameJs {
      def apply(contents:String): String = {
        return js.game.render(contents).toString()
      }
      val semanticType:Type = gameVar :&: 'js =>: gameVar :&: 'gameJs
    }

    @combinator object SomeScripts {
      def apply(): Array[String] = {
        return Array("example.js", "othersource.js")
      }
      val semanticType:Type = 'scripts
    }

    /*@combinator object BlankScripts {
      def apply(): Array[String] = {
        return Array()
      }
      val semanticType:Type = 'scripts
    }*/

    @combinator object IndexHTML {
      def apply(otherScripts:Array[String]): String = {
        var ret = """
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
    """

    if(!otherScripts.isEmpty){    
      ret += otherScripts.mkString("<script src=\"js/", "\"></script>\n    <script src=\"js/", "\"></script>\n");
    }

    ret += """
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
return ret;
      }
      val semanticType:Type = 'scripts =>: 'indexHtml
    }

    @combinator object MainPage {
      def apply(gameTitle:String): String = {
        return html.mainpage.render(gameTitle).toString()
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
        return html.levelselect.render().toString()
      }
      val semanticType:Type = 'levelSelect
    }

    @combinator object Settings {
      def apply(): String = {
        return html.settings.render().toString()
      }
      val semanticType:Type = 'settings
    }

    @combinator object AppJs {
      def apply(): String = {
        return js.app.render().toString()
      }
      val semanticType:Type = 'appJs
    }

    @combinator object SQL {
      def apply(): String = {
        return js.sql.render().toString()
      }
      val semanticType:Type = 'sql
    }

    /*@combinator object SomeStates {
      def apply(): Array[String] = {
        return Array("youlose", "hellstate")
      }
      val semanticType:Type = 'otherStates
    }*/

    class BlankStates(gameType:Type) {
      def apply(): Array[String] = {
        return Array()
      }
      val semanticType:Type = 'otherStates :&: gameType
    }

    @combinator object States {
      def apply(otherStates:Array[String]): String = {
        var ret = """
/**
 * A list of all the levels and states. This is what allows us to switch
 * between the different "pages". Each html page should have an associated
 * javascript controller, even if that controller is unused.
**/

angular.module("states", ["ionic"])

.config(function ($stateProvider, $urlRouterProvider){
  $stateProvider
"""

  for(state <- otherStates) {
    ret += """.state(""""+ state +"""", {
      url: "/"""+ state +"""",
      templateUrl: "templates/"""+ state +""".html",
      controller: """"+ state +"""Ctrl"
    })""" + "\n"
  }

  ret += """.state("main", {
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
return ret
      }
      val semanticType:Type = gameVar :&: 'otherStates =>: 'states :&: gameVar
    }


    class LevelList(gameType:Type) {
      def apply(): String = {
        return """[1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
            11, 12, 13, 14, 15, 16, 17, 18, 19, 20]"""
      }
      val semanticType:Type = 'levelList :&: gameType
    }

    @combinator object Controllers {
      def apply(levelList:String): String = {
        return js.controllers.render(levelList).toString()
      }
      val semanticType:Type = gameVar :&: 'levelList =>: 'controllers :&: gameVar
    }

    class Bind(sym:Symbol, filePath:String) {
      def apply(expr:String) : Tuple = {
			  return new Tuple(expr, filePath)
      }
      val semanticType:Type = sym =>: 'BoundFile :&: sym :&: gameVar
    }

    @combinator object Bind0 extends Bind('indexHtml, "www/index.html")

    @combinator object Bind2 extends Bind('appJs, "www/js/app.js")
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

    @combinator object Bind3 extends GameBind('controllers, "www/js/controllers.js")
    @combinator object General1 extends LevelList('lightsout)
    @combinator object Bind1 extends GameBind('states, "www/js/states.js")
    @combinator object General2 extends BlankStates('lightsout)
  }

  // Initializes the CLS system
  val repository = new AppTrait {}
  val reflectedRepository = ReflectedRepository (repository, kinding=repository.kinding)

  // Get the interpreted response from CLS
  val reply = reflectedRepository.inhabit[Tuple] ('BoundFile :&: 'lightsout)

  // Pass the response into our defined output, currently just a printer
  val iter = reply.interpretedTerms.values.flatMap(_._2).iterator.asJava
  DirectoryMaker.parseResults(iter)
}
