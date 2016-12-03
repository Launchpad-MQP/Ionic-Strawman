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
      def apply(guessButton:String): String = {
        return """
        <ion-content class="has-header" padding="true">
          <div class="row">
            <input class="item item-input" maxLength="{{word.length}}" id="guess_{{levelNum}}" placeholder="Guess a {{word.length}} letter word">
            &nbsp; """ + guessButton + """
          </div>
          <div class="row">
            <h4>{{result}}</h4>
          </div>
      </ion-content>"""
      }
      val semanticType:Type = 'guessButton =>: 'mastermind :&: 'html
    }

    @combinator object GuessButton {
      def apply(): String = {
        return """<button class="button button-positive levelBtn" ng-click="makeGuess()">Guess</button>"""
      }
      val semanticType:Type = 'guessButton
    }

    @combinator object HangmanHTML {
      def apply(guessButton:String): String = {
        """<div class= "item row">
    			<div ng-repeat="letter in myLetters track by $index">
    				<div class= "guessable {{levelNum}} {{letter}}">{{letter}}</div>
    			</div>
    		</div>
    		<br>
    		<div class="row">
    			<ion-input class="item item-input item-stacked-label">
    				<input type="text" maxLength="1" class= "letterguess" id="letterguess_{{levelNum}}" placeholder="Guess a letter here">
    			</ion-input>
    			&nbsp; """ + guessButton + """
    		</div>

    		<h4>Guessed Letters:</h4>
    		<div class = "row" id="guessed_{{levelNum}}"></div>
    <h4>Tries Left: {{guessesLeft}}</h4>"""
      }
      val semanticType:Type = 'guessButton =>: 'hangman :&: 'html
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

    @combinator object DummyHTML {
      def apply(): String = {
        return """
    <div class="col" style="text-align:center">
      <button class="button button-assertive levelBtn" ng-click="completeLevel()">Click Me!</button>
    </div>"""
      }
      val semanticType:Type = 'dummy :&: 'html
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

    class Textify(game:Symbol, sym:Symbol, output:String) {
      def apply() : String = {
			  return output
      }
      val semanticType:Type = game :&: sym
    }

    class TextifyGeneral(sym:Symbol, output:String) {
      def apply(): String = {
        return output
      }
      val semanticType:Type = sym
    }

    @combinator object SQL extends TextifyGeneral('sql, js.sql.render().toString())
    @combinator object LevelSelect extends TextifyGeneral('levelSelect, html.levelselect.render().toString())
    @combinator object Settings extends TextifyGeneral('settings, html.settings.render().toString())

    @combinator object HangmanTitle extends Textify('hangman, 'gameTitle, "Hangman")
    @combinator object MastermindTitle extends Textify('mastermind, 'gameTitle, "Mastermind")
    @combinator object LightsOutTitle extends Textify('lightsout, 'gameTitle, "Lights Out")
    @combinator object DummyTitle extends Textify('dummy, 'gameTitle, "Dummy")

    @combinator object HangmanJS extends Textify('hangman, 'js, js.hangman.render().toString())
    @combinator object MastermindJS extends Textify('mastermind, 'js, js.mastermind.render().toString())
    @combinator object LightsOutJS extends Textify('lightsout, 'js, js.lightsout.render().toString())
    @combinator object DummyJS extends Textify('dummy, 'js, "")

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

    @combinator object AppJs {
      def apply(scriptList:Array[String]): String = {
        return js.app.render(scriptList).toString()
      }
      val semanticType:Type = 'scriptList =>: 'appJs
    }

    @combinator object CSS {
      def apply(): String = {
        return """.row.unlimited-items {
          flex-wrap: wrap;
        }

        .row.unlimited-items .col{
          flex: none;
          width: 20%;
        }

        .guessable {
        	color : rgba(0, 0, 0, 0);
        	width : 30px;
        	border : thin solid black;
        	text-align: center;
        	font-size: 20px;
        }

        .discovered {
        	color : rgba(0, 0, 0, 1);
        	width : 30px;
        	border : thin solid black;
        	text-align: center;
        	font-size: 20px;
        }
"""
      }
      val semanticType:Type = 'css
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

    @combinator object BindA extends Bind('css, "www/css/style.css")

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
