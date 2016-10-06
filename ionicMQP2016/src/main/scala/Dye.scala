import de.tu_dortmund.cs.ls14.cls.interpreter.ReflectedRepository
import de.tu_dortmund.cs.ls14.cls.types.syntax._
import de.tu_dortmund.cs.ls14.cls.types._
import de.tu_dortmund.cs.ls14.cls.interpreter.combinator

import scala.collection.JavaConverters._
import ionicmqp2016._;
import _root_.java.nio.file._

object DyeExample extends App {
  trait SimpleTrait {

    // Defines a list of types. Basically the function prototypes. Includes parameters.

    // Represents the color red
    @combinator object Red {
      def apply() : String = {
        return "red";
      }
      val semanticType:Type = 'color :&: 'red;
    }

    // Represents the color blue
    @combinator object Blue {
      def apply() : String = {
        return "blue";
      }
      val semanticType:Type = 'color :&: 'blue;
    }

    // Represents a shirt
    @combinator object Shirt {
      def apply() : String = {
        return "shirt";
      }
      val semanticType:Type = 'thing;
    }

    // Represents a car
    @combinator object Car {
      def apply() : String = {
        return "car";
      }
      val semanticType:Type = 'thing;
    }

    class Dyer(a:Type, b:Type) {
      def apply(d:String, e:String) : String = {
        return d + " " + e;
      }
      val semanticType:Type = a =>: b =>: 'dyedthing :&: a;
    }

    @combinator object Comb1 extends Dyer('color :&: 'blue, 'thing)

  }
  // Initializes the CLS system
  val reflectedRepository = ReflectedRepository (new SimpleTrait {})

  // Get the interpreted response from CLS
  val reply = reflectedRepository.inhabit[String] ('color)

  // Pass the response into our defined output, currently just a printer
  val it = reply.interpretedTerms.values.flatMap(_._2).iterator
  //DirectoryMaker.makeDirectory()
  PrintFragments.processResults(it.asJava)

}
