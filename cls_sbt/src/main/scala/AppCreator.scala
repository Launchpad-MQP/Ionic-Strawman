import de.tu_dortmund.cs.ls14.cls.interpreter.ReflectedRepository
import de.tu_dortmund.cs.ls14.cls.types.syntax._
import de.tu_dortmund.cs.ls14.cls.types._
import de.tu_dortmund.cs.ls14.cls.interpreter.combinator

import play.twirl.api.JavaScript
import play.twirl.api.Html
import scala.collection.JavaConverters._
import ionicmqp2016._
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
      .addOption('nim)

    type buttonType = (String, String, String) => Html
    @combinator object Button {
      def apply(): buttonType = {
        return (callback:String, text:String, color:String) =>
          new Html(s"""<button class="button button-$color levelBtn" ng-click="$callback()">$text</button>""")
      }
      val semanticType:Type = 'button
    }

    type toggleType = (String, String) => Html
    @combinator object Toggle {
      def apply(): toggleType = {
        return (model:String, name:String) =>
          new Html(s"""$name<label class="toggle">
           <input type="checkbox" ng-model="$model">
           <div class="track">
             <div class="handle"></div>
           </div>
        </label>""")
      }
      val semanticType:Type = 'toggle
    }

    type rangeType = (String, String, String, String, String, String, String) => Html
    @combinator object Range {
      def apply(): rangeType = {
        return (min:String, max:String, iconLeft:String, iconRight:String, name:String, model:String, callback:String) =>
          new Html(s"""<div class="item range">
          <i class="icon $iconLeft"></i>
          <input type="range" name="$name" min="$min" max="$max" ng-model="$model" on-release="$callback('$name')">
          <i class="icon $iconRight"></i></div>""")
      }
      val semanticType:Type = 'range
    }

    type checkboxType = (Array[String], Array[String]) => Html
    @combinator object Checkboxes {
      def apply(): checkboxType = {
        def buildChoices(models:Array[String], choices:Array[String]): String = {
          var a = ""
          var i = 0
          for(i <- 0 until choices.length) {
            a += s"""\n\t<ion-checkbox ng-model="${models(i)}" ng-value="${models(i)}">${choices(i)}</ion-checkbox>"""
          }
          return a
        }
        return (models:Array[String], choices:Array[String]) => new Html("<ion-list>" + buildChoices(models, choices) + "</ion-list>")
      }
      val semanticType:Type = 'checkboxes
    }

    type radioType = (String, Array[String], Array[String]) => Html
    @combinator object RadioButtons {
      def apply(): radioType = {
        def buildChoices(model:String, values:Array[String], choices:Array[String]): String = {
          var a = ""
          var i = 0
          for(i <- 0 until choices.length) {
            a += s"""\n\t<ion-radio ng-model="$model" ng-value="${values(i)}">${choices(i)}</ion-radio>"""
          }
          return a
        }
        return (model:String, values:Array[String], choices:Array[String]) => new Html("<ion-list>" + buildChoices(model, values, choices) + "</ion-list>")
      }
      val semanticType:Type = 'radiobuttons
    }

    @combinator object MastermindHTML {
      def apply(button:buttonType): String = {
        return html.html.mastermind.render(button).toString()
      }
      val semanticType:Type = 'button =>: 'mastermind :&: 'html
    }

    @combinator object HangmanHTML {
      def apply(button:buttonType): String = {
        return html.html.hangman.render(button).toString()
      }
      val semanticType:Type = 'button =>: 'hangman :&: 'html
    }

    @combinator object LightsOutHTML {
      def apply(): String = {
        return html.html.lightsout.render().toString()
      }
      val semanticType:Type = 'lightsout :&: 'html
    }

    @combinator object DummyHTML {
      def apply(): String = {
        return html.html.dummy.render().toString()
      }
      val semanticType:Type = 'dummy :&: 'html
    }

    @combinator object FrankensteinHTML {
      def apply(radioButtons:radioType, checkBoxes:checkboxType, endButton:buttonType, toggler:toggleType, rangey:rangeType): String = {
        return html.html.frankenstein.render(radioButtons, checkBoxes, endButton, toggler, rangey).toString()
      }
      val semanticType:Type = 'radiobuttons =>: 'checkboxes =>: 'button =>: 'toggle =>: 'range =>: 'monster :&: 'html
    }

    @combinator object NimHTML {
      def apply(range:rangeType): String = {
        return html.html.nim.render(range).toString()
      }
      val semanticType:Type = 'range =>: 'nim :&: 'html
    }

    @combinator object GameHTML {
      def apply(contents:String): String = {
        return html.html.game.render(contents).toString()
      }
      val semanticType:Type = gameVar :&: 'html =>: gameVar :&: 'gameHtml
    }

    @combinator object GameJs {
      def apply(contents:JavaScript): String = {
        return js.js.game.render(contents).toString()
      }
      val semanticType:Type = gameVar :&: 'js =>: gameVar :&: 'gameJs
    }

    @combinator object MainPage {
      def apply(gameTitle:String): String = {
        return html.html.mainpage.render(gameTitle).toString()
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
    @combinator object NimTitle extends Title('nim, "Nim")

    @combinator object LevelList {
      def apply(): Array[Int] = {
        return (1 to 20).toArray
      }
      val semanticType:Type = 'levelList
    }

    @combinator object SQLColumns {
      def apply(): Map[String, String] = {
        return Map("state" -> "VARCHAR(50)", "time" -> "INTEGER")
      }
      val semanticType:Type = 'sqlcolumns
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
        return html.html.index.render(scriptList).toString()
      }
      val semanticType:Type = 'scriptList =>: 'indexHtml
    }

    @combinator object AppJs {
      def apply(scriptList:Array[String]): String = {
        return js.js.app.render(scriptList).toString()
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
        return js.js.states.render(stateList).toString()
      }
      // List of states is also list of scripts
      val semanticType:Type = 'stateList =>: 'states
    }

    @combinator object Controllers {
      def apply(levelList:Array[Int]): String = {
        return js.js.controllers.render(levelList).toString()
      }
      val semanticType:Type = 'levelList =>: 'controllers
    }

    @combinator object SQL {
      def apply(sqlcolumns:Map[String, String]): String = {
        return js.js.sql.render(sqlcolumns).toString()
      }
      val semanticType:Type = 'sqlcolumns =>: 'sql
    }

    class Render(inputType:Type, output:String) {
      def apply(): String = {
        return output
      }
      val semanticType:Type = inputType
    }

    class RenderJS(inputType:Type, output:JavaScript) {
      def apply(): JavaScript = {
        return output
      }
      val semanticType:Type = inputType
    }

    @combinator object LevelSelect extends Render('levelSelect, html.html.levelselect.render().toString())
    @combinator object Settings extends Render('settings, html.html.settings.render().toString())

    @combinator object HangmanJS extends RenderJS('hangman :&: 'js, js.js.hangman.render())
    @combinator object MastermindJS extends RenderJS('mastermind :&: 'js, js.js.mastermind.render())
    @combinator object LightsOutJS extends RenderJS('lightsout :&: 'js, js.js.lightsout.render())
    @combinator object DummyJS extends RenderJS('dummy :&: 'js, js.js.dummy.render())
    @combinator object FrankensteinJS extends RenderJS('monster :&: 'js, js.js.frankenstein.render())
    @combinator object NimJS extends RenderJS('nim :&: 'js, js.js.nim.render())

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
  val reply = reflectedRepository.inhabit[Tuple] ('BoundFile :&: 'hangman)

  // Pass the response into our defined output, currently just a printer
  val iter = reply.interpretedTerms.values.flatMap(_._2).iterator.asJava
  DirectoryMaker.parseResults(iter)
}
