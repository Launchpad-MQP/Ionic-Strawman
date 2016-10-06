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
    @combinator object RedCloth {
      def apply() : String = {
        return "red"
      }
      val semanticType:Type = 'cloth :&: 'red
    }

    // Represents the color blue
    @combinator object BlueCloth {
      def apply() : String = {
        return "blue"
      }
      val semanticType:Type = 'cloth :&: 'blue
    }

    class Dyer(a:Type, b:Type) {
      def apply(c:String) : String = {
        return c + " shirt"
      }
      val semanticType:Type = a =>: b
    }

    @combinator object Comb1 extends Dyer('cloth, 'shirt)

  }
  // Initializes the CLS system
  val reflectedRepository = ReflectedRepository (new SimpleTrait {})

  // Get the interpreted response from CLS
  val reply = reflectedRepository.inhabit[String] ('shirt)

  // Pass the response into our defined output, currently just a printer
  val it = reply.interpretedTerms.values.flatMap(_._2).iterator
  //DirectoryMaker.makeDirectory()
  PrintFragments.processResults(it.asJava)

}
