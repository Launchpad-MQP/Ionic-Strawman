import de.tu_dortmund.cs.ls14.cls.interpreter.ReflectedRepository
import de.tu_dortmund.cs.ls14.cls.types.syntax._
import de.tu_dortmund.cs.ls14.cls.types._
import de.tu_dortmund.cs.ls14.cls.interpreter.combinator

import play.twirl.api.JavaScript
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
      .addOption('monster)

    @combinator object MastermindHTML {
      def apply(button:buttonType): String = {
        return """
      <div class="row">
        <input class="item item-input" maxLength="{{word.length}}" id="guess_{{levelNum}}" placeholder="Guess a {{word.length}} letter word">
        &nbsp; """ + button("makeGuess", "Guess", "positive") + """
      </div>
      <div class="row">
        <h4>{{result}}</h4>
      </div>"""
      }
      val semanticType:Type = 'button =>: 'mastermind :&: 'html
    }

    type buttonType = (String, String, String) => String
    @combinator object Button {
      def apply(): buttonType = {
        return (callback:String, text:String, color:String) =>
          s"""<button class="button button-$color levelBtn" ng-click="$callback()">$text</button>"""
      }
      val semanticType:Type = 'button
    }

    type toggleType = (String) => String
    @combinator object Toggle {
      def apply(): toggleType = {
        return (name:String) =>
          s"""$name<label class="toggle">
           <input type="checkbox">
           <div class="track">
             <div class="handle"></div>
           </div>
        </label>"""
      }
      val semanticType:Type = 'toggle
    }

    type rangeType = (String, String, String, String) => String
    @combinator object Range {
      def apply(): rangeType = {
        return (iconLeft:String, iconRight:String, name:String, callback:String) =>
          s"""<div class="item range">
          <i class="icon $iconLeft"></i>
          <input type="range" name="$name" min="0" max="100" ng-model="value" ng-change="$callback(value)">
          <i class="icon $iconRight"></i></div>"""
      }
      val semanticType:Type = 'range
    }

    // //don't think this one actually works
    // type checkboxType = (Array[String]) => String
    // @combinator object Checkboxes {
    //   def apply(): checkboxType = {
    //     return (choices:Array[String]) =>
    //       val ret = "<ion-list>"
    //       for(choice <- choices) {
    //         ret += s"""<ion-radio ng-model="choice" ng-value="'A'">$choice</ion-radio>"""
    //       }
    //       ret += "</ion-list>"
    //   }
    //   val semanticType:Type = 'radiobuttons
    // }

    // //don't think this one actually works
    // type radioType = (Array[String]) => String
    // @combinator object RadioButtons {
    //   def apply(): radioType = {
    //     return (names:Array[String]) =>
    //       val ret = "<ion-list>"
    //       for(name <- names) {
    //         ret += s"""<ion-checkbox>$name</ion-checkbox>"""
    //       }
    //       ret += "</ion-list>"
    //   }
    //   val semanticType:Type = 'checkboxes
    // }

    @combinator object HangmanHTML {
      def apply(button:buttonType): String = {
        return """
      <div class= "item row">
    		<div ng-repeat="letter in myLetters track by $index">
    			<div class= "guessable {{levelNum}} {{letter}}">{{letter}}</div>
    		</div>
    	</div>
    	<br>
    	<div class="row">
    		<ion-input class="item item-input item-stacked-label">
    			<input type="text" maxLength="1" class= "letterguess" id="letterguess_{{levelNum}}" placeholder="Guess a letter here">
    		</ion-input>
    		&nbsp; """ + button("makeGuess", "Guess", "positive") + """
    	</div>

    	<h4>Guessed Letters:</h4>
        <div class = "row" id="guessed_{{levelNum}}"></div>
      <h4>Tries Left: {{guessesLeft}}</h4>"""
      }
      val semanticType:Type = 'button =>: 'hangman :&: 'html
    }

    @combinator object LightsOutHTML {
      def apply(): String = {
        return """
      <div class="row" ng-repeat="row in buttons">
        <div class="col col-25"></div>
        <div class="col" ng-repeat="button in row">
          <a class="button button-dark" ng-click="toggle('{{button}}')" id="{{levelNum}}_{{button}}">&nbsp</a>
        </div>
        <div class="col col-25"></div>
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

    @combinator object FrankensteinGame {
      def apply(): String = {
        return """
      <div class="row">
        <div class="col" style="text-align:center">
          Alive?
          <label class="toggle">
            <input type="checkbox">
            <div class="track">
              <div class="handle"></div>
            </div>
          </label>
        </div>
      </div>
      <div class="row">
        <div class="col" style="text-align:center">
          <ion-list>
            <ion-checkbox ng-model="frankenVars.cBox1">Cool</ion-checkbox>
            <ion-checkbox ng-model="frankenVars.cBox2">Scary</ion-checkbox>
            <ion-checkbox ng-model="frankenVars.cBox3">Handsome</ion-checkbox>
          </ion-list>
        </div>
      </div>
      <div class="row">
        <div class="col" style="text-align:center">
          <ion-list>
            <ion-radio>Red</ion-radio>
            <ion-radio>Blue</ion-radio>
            <ion-radio>Green</ion-radio>
          </ion-list>
        </div>
      </div>
      <div class="row">
        <div class="col" style="text-align:center">
          <div class="item range">
            <i class="icon ion-flash-off"></i>
            <input type="range" name="power">
            <i class="icon ion-flash"></i>
          </div>
        </div>
      </div>
      <div class="row">
        <div class="col" style="text-align:center">
          <button class="button button-assertive levelBtn" ng-click="checkComplete()">Click Me!</button>
        </div>
      </div>"""
      }
      val semanticType:Type = 'monster :&: 'html
    }

    @combinator object GameHTML {
      def apply(contents:String): String = {
        return html.game.render(contents).toString()
      }
      val semanticType:Type = gameVar :&: 'html =>: gameVar :&: 'gameHtml
    }

    @combinator object GameJs {
      def apply(contents:JavaScript): String = {
        return js.game.render(contents).toString()
      }
      val semanticType:Type = gameVar :&: 'js =>: gameVar :&: 'gameJs
    }

    @combinator object MainPage {
      def apply(gameTitle:String): String = {
        return html.mainpage.render(gameTitle).toString()
      }
      val semanticType:Type = gameVar :&: 'gameTitle =>: 'mainPage :&: gameVar
    }

    class Title(game:Symbol, title:String) {
      def apply() : String = {
			  return title
      }
      val semanticType:Type = game :&: 'gameTitle
    }

    @combinator object HangmanTitle extends Title('hangman, "Hangman")
    @combinator object MastermindTitle extends Title('mastermind, "Mastermind")
    @combinator object LightsOutTitle extends Title('lightsout, "Lights Out")
    @combinator object DummyTitle extends Title('dummy, "Dummy")
    @combinator object FrankensteinTitle extends Title('monster, "Beelzebub")

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
        return xml.style.render().toString()
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

    class Render(inputType:Type, output:String) {
      def apply(): String = {
        return output
      }
      val semanticType:Type = inputType
    }

    class Render2(inputType:Type, output:JavaScript) {
      def apply(): JavaScript = {
        return output
      }
      val semanticType:Type = inputType
    }

    @combinator object SQL extends Render('sql, js.sql.render().toString())
    @combinator object LevelSelect extends Render('levelSelect, html.levelselect.render().toString())
    @combinator object Settings extends Render('settings, html.settings.render().toString())

    @combinator object HangmanJS extends Render2('hangman :&: 'js, js.hangman.render())
    @combinator object MastermindJS extends Render2('mastermind :&: 'js, js.mastermind.render())
    @combinator object LightsOutJS extends Render2('lightsout :&: 'js, js.lightsout.render())
    @combinator object DummyJS extends Render('dummy :&: 'js, "")
    @combinator object FrankensteinJS extends Render2('monster :&: 'js, js.frankenstein.render())

    class Bind(inputType:Type, filePath:String){
      def apply(expr:String) : Tuple = {
        return new Tuple(expr, filePath)
      }
      val semanticType:Type = inputType =>: 'BoundFile :&: inputType :&: gameVar
    }

    @combinator object Bind0 extends Bind('indexHtml, "www/index.html")
    @combinator object Bind1 extends Bind('states, "www/js/states.js")

    @combinator object Bind2 extends Bind('appJs, "www/js/app.js")
    @combinator object Bind3 extends Bind('controllers, "www/js/controllers.js")
    @combinator object Bind4 extends Bind('sql, "www/js/sql.js")
    @combinator object Bind5 extends Bind('gameHtml :&: gameVar, "www/templates/game.html")
    @combinator object Bind6 extends Bind('gameJs :&: gameVar, "www/js/game.js")

    @combinator object Bind7 extends Bind('levelSelect, "www/templates/level_select.html")
    @combinator object Bind8 extends Bind('mainPage :&: gameVar, "www/templates/main.html")
    @combinator object Bind9 extends Bind('settings, "www/templates/settings.html")

    @combinator object BindA extends Bind('css, "www/css/style.css")

  }

  // Initializes the CLS system
  val repository = new AppTrait {}
  val reflectedRepository = ReflectedRepository (repository, kinding=repository.kinding)

  // Get the interpreted response from CLS
  val reply = reflectedRepository.inhabit[Tuple] ('BoundFile :&: 'monster)

  // Pass the response into our defined output, currently just a printer
  val iter = reply.interpretedTerms.values.flatMap(_._2).iterator.asJava
  DirectoryMaker.parseResults(iter)
}
