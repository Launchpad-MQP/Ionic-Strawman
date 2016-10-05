import de.tu_dortmund.cs.ls14.cls.interpreter.ReflectedRepository
import de.tu_dortmund.cs.ls14.cls.types.syntax._
import de.tu_dortmund.cs.ls14.cls.types._
import de.tu_dortmund.cs.ls14.cls.interpreter.combinator

import scala.collection.JavaConverters._
import ionicmqp2016._;
import _root_.java.nio.file._

object SimpleNameWhichDoesntMatchFileName extends App {
  trait SimpleTrait {

    // Defines a list of types. Basically the function prototypes. Includes parameters.

    // Very basic returns a string.
    @combinator object EmptyApp {
      def apply() : String = {
        return "angular.module('starter', ['ionic'])";
      }
      val semanticType:Type = 'emptyApp;
    }

    @combinator object MainPage {
      def apply() : String = {
        return ".run(function ($ionicPlatform){})";
      }
      val semanticType:Type = 'mainPage;
    }

    class StringConcatenator(a:Type, b:Type, c:Type) {
      def apply(a:String, b:String) : String = {
        return a + '\n' + b;
      }
      val semanticType:Type = a =>: b =>: c;
    }

    @combinator object Comb1 extends StringConcatenator('emptyApp, 'mainPage, 'appWithMainPage)
    @combinator object Comb2 extends StringConcatenator('appWithMainPage, 'mainPage, 'appWithTwoMainPages)

  }
  // Initializes the CLS system
  val reflectedRepository = ReflectedRepository (new SimpleTrait {})

  // Get the interpreted response from CLS
  val reply = reflectedRepository.inhabit[String] ('appWithTwoMainPages)

  // Pass the response into our defined output, currently just a printer
  val it = reply.interpretedTerms.values.flatMap(_._2).iterator
  PrintFragments.processResults(it.asJava)

}