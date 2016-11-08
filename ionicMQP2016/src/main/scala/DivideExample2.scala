import de.tu_dortmund.cs.ls14.cls.interpreter.ReflectedRepository
import de.tu_dortmund.cs.ls14.cls.types.syntax._
import de.tu_dortmund.cs.ls14.cls.types._
import de.tu_dortmund.cs.ls14.cls.interpreter.combinator

import scala.collection.JavaConverters._
import ionicmqp2016._;

object DivideExample2 extends App {
	trait DivideExample2Trait {

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

		class Dyer(a:Type, b:Type) {
    	def apply(c:String) : String = {
    		return c + " shirt"
    	}
    	val semanticType:Type = a =>: b
    }

		class Painter(a:Type, b:Type) {
			def apply(c:String) : String = {
				return c + " car"
			}
			val semanticType:Type = a =>: b
		}

		@combinator object Comb1 extends Dyer('color, 'shirt)
		@combinator object Comb2 extends Painter('color, 'car)

		class Bind(sym:Symbol, file:String) {
			def apply(expr:String) : Tuple = {
				return new Tuple(expr, file)
			}
			val semanticType:Type = sym =>: 'BoundFile :&: sym
		}

		@combinator object BindShirt extends Bind('shirt, "Shirts.txt")
		@combinator object BindCar extends Bind('car, "Cars.txt")

	}

	val reflectedRepository = ReflectedRepository(new DivideExample2Trait {})

	val reply = reflectedRepository.inhabit[Tuple] ('BoundFile :&: 'car)

	val it = reply.interpretedTerms.values.flatMap(_._2).iterator.asJava

	FragmentExplorer.processResultsTuple(it)
}