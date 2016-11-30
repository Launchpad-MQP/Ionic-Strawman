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
        return """angular.module("game", ["ionic", "sql"])

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

          """+ contents +"""

          $scope.completeLevel = function () {
            $rootScope.completeLevel($state, $stateParams.levelNum);
          }

          $scope.restart = function () {
            console.log("Restarting level...");
            $state.reload();
          }
        });"""
      }
      val semanticType:Type = gameVar :&: 'js =>: gameVar :&: 'gameJs
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

    @combinator object LevelList {
      def apply(): Array[Int] = {
        return (1 to 20).toArray
      }
      val semanticType:Type = 'levelList
    }

    // JS files
    @combinator object ScriptList {
      def apply(): Array[String] = {
        // sql is a library, so it must be listed before controllers
        return Array("sql", "states", "controllers", "game")
      }
      val semanticType:Type = 'scriptList
    }

    // HTML files
    @combinator object StateList {
      def apply(): Array[String] = {
        return Array("main", "level_select", "settings")
      }
      val semanticType:Type = 'stateList
    }

    @combinator object IndexHTML {
      def apply(scriptList:Array[String]): String = {
        return html.index.render(scriptList).toString()
      }
      val semanticType:Type = 'scriptList =>: 'indexHtml
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
      def apply(scriptList:Array[String]): String = {
        return js.app.render(scriptList).toString()
      }
      val semanticType:Type = 'scriptList =>: 'appJs
    }

    @combinator object SQL {
      def apply(): String = {
        return js.sql.render().toString()
      }
      val semanticType:Type = 'sql
    }

    @combinator object States {
      def apply(stateList:Array[String]): String = {
        return js.states.render(stateList).toString()
      }
      // List of states is also list of scripts
      val semanticType:Type = 'stateList =>: 'states
    }

    @combinator object Controllers {
      def apply(levelList:Array[Int]): String = {
        return js.controllers.render(levelList).toString()
      }
      val semanticType:Type = 'levelList =>: 'controllers
    }

    class Bind(sym:Symbol, filePath:String) {
      def apply(expr:String) : Tuple = {
			  return new Tuple(expr, filePath)
      }
      val semanticType:Type = sym =>: 'BoundFile :&: sym :&: gameVar
    }

    class GameBind(sym:Symbol, filePath:String) {
      def apply(expr:String) : Tuple = {
			  return new Tuple(expr, filePath)
      }
      val semanticType:Type = sym :&: gameVar =>: 'BoundFile :&: sym :&: gameVar
    }

    @combinator object Bind0 extends Bind('indexHtml, "www/index.html")
    @combinator object Bind1 extends Bind('states, "www/js/states.js")

    @combinator object Bind2 extends Bind('appJs, "www/js/app.js")
    @combinator object Bind3 extends Bind('controllers, "www/js/controllers.js")
    @combinator object Bind4 extends Bind('sql, "www/js/sql.js")
    @combinator object Bind5 extends GameBind('gameHtml, "www/templates/game.html")
    @combinator object Bind6 extends GameBind('gameJs, "www/js/game.js")

    @combinator object Bind7 extends Bind('levelSelect, "www/templates/level_select.html")
    @combinator object Bind8 extends GameBind('mainPage, "www/templates/main.html")
    @combinator object Bind9 extends Bind('settings, "www/templates/settings.html")

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
