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
      // def apply() : String = { <java code> }
      def apply() : String = { return "angular.module('starter', ['ionic'])"; }
      val semanticType:Type = 'emptyApp;
    }

    @combinator object MainPage {
      // def apply() : String = { <java code> }
      def apply() : String = { return ".run(function ($ionicPlatform){})"; }
      val semanticType:Type = 'mainPage;
    }

    @combinator object SimpleApp extends BasicCombinator()

    // Combines the basic EmptyApp with a basic MainPage and returns it. No parameters needed.
    class BasicCombinator() {
      // takes a (String) and b (String) returns (String) according to <java code>
      def apply(a:String, b:String) : String = { return a + '\n' + b; }
      val semanticType:Type = 'emptyApp =>: 'mainPage =>: 'appWithMainPage;
    }
  }

  // Initializes the CLS system
  val reflectedRepository = ReflectedRepository (new SimpleTrait {})

  // Get the interpreted response from CLS
  val reply = reflectedRepository.inhabit[String] ('appWithMainPage)

  // Pass the response into our defined output, currently just a printer
  val it = reply.interpretedTerms.values.flatMap(_._2).iterator
  PrintFragments.processResults(it.asJava)

}