import de.tu_dortmund.cs.ls14.cls.interpreter.ReflectedRepository
import de.tu_dortmund.cs.ls14.cls.types.syntax._
import de.tu_dortmund.cs.ls14.cls.types._
import de.tu_dortmund.cs.ls14.cls.interpreter.combinator

import scala.collection.JavaConverters._
import ionicmqp2016._;
import _root_.java.nio.file._

object TestExample extends App {
  trait TestTrait {

    // Defines a list of types. Basically the function prototypes. Includes parameters.

    // variable to be used within semanticType
    val colorVar = Variable("colorName")
    lazy val kinding =
        Kinding.empty
        .merge(
            Kinding(colorVar)
            .addOption('red)
            .addOption('blue)
            )

    @combinator object Red {
      def apply() : String = {
        return "red"
      }
      val semanticType:Type = 'color :&: 'red
    }

    @combinator object Blue {
      def apply() : String = {
        return "blue"
      }
      val semanticType:Type = 'color :&: 'blue
    }

    // Represents the action of dying a shirt
    class Dyer(a:Type, b:Type) {
      def apply(c:String) : String = {
        return c + " shirt\na lovely " + c + " shirt\ni love my " + c + " shirt so much\nwhat a lovely shirt"
      }
      val semanticType:Type = 'color :&: colorVar =>: colorVar :&: b
    }

    // Represents the action of painting a car
    class Painter(a:Type, b:Type) {
      def apply(c:String) : String = {
        return c + " car"
      }
      val semanticType:Type = 'color :&: colorVar =>: colorVar :&: b
    }

    class Bind(thing:Symbol, fileName: String) {
      def apply(expr:String) : Tuple = { return new Tuple(expr, fileName); }
      val semanticType:Type = thing =>: 'BoundFile :&: thing
    }

    @combinator object Comb1 extends Dyer('color, 'shirt)
    @combinator object Comb2 extends Painter('color, 'car)

    @combinator object Bind1 extends Bind('shirt, "TestDirectory/www/js/Shirt.txt")
    @combinator object Bind2 extends Bind('car, "TestDirectory/www/js/Car.txt")

  }

  // Initializes the CLS system
  val repository = new TestTrait {}
  val reflectedRepository = ReflectedRepository (repository, kinding=repository.kinding)

  // Get the interpreted response from CLS
  val reply = reflectedRepository.inhabit[Tuple] ('BoundFile)

  // Pass the response into our defined output, currently just a printer
  val iter = reply.interpretedTerms.values.flatMap(_._2).iterator.asJava
  DirectoryMaker.parseResults(iter)
  //FragmentExplorer.processResultsTuple(it.asJava)

}
