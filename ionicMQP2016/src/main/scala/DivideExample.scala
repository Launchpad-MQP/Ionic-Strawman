import de.tu_dortmund.cs.ls14.cls.interpreter.ReflectedRepository
import de.tu_dortmund.cs.ls14.cls.types.syntax._
import de.tu_dortmund.cs.ls14.cls.types._
import de.tu_dortmund.cs.ls14.cls.interpreter.combinator

import scala.collection.JavaConverters._
import ionicmqp2016._;

import java.nio.file.Paths

object DivideExample extends App {
	trait DivideExampleTrait {

		// variable to be used within semanticType
		val geomTypeVar = Variable("geomTypeName")
		lazy val kinding =
				Kinding.empty
				.merge(
						Kinding(geomTypeVar)
						.addOption('sin)
						.addOption('cos)
						)

			// Expression that evalues to number
			@combinator
			object Divide {
			def apply(expressionA:String, expressionB:String) : String = {
					return "(" + expressionB + " == 0 ?: (NaN) : (" + "(" + expressionA + ") / (" + expressionB + ") )"; }

			val semanticType:Type = 'expression :&: geomTypeVar =>: 'expression :&: geomTypeVar =>: 'expression :&: geomTypeVar
		}

		@combinator
		object SafeDivide {
			def apply(expressionA:String, expressionB:String) : String = {
					return "(" + expressionA + ") / (" + expressionB + ") )"; }
			val semanticType:Type = 'expression :&: geomTypeVar =>: ('expression :&: geomTypeVar :&: 'NonZero) =>: 'expression :&: geomTypeVar
		}

		@combinator
		object SinExpression {
			def apply() : String = { return "Math.sin(x)"; }
			val semanticType:Type = 'expression :&: 'sin
		}

		@combinator
		object CosExpression {
			def apply() : String = { return "Math.cos(x)"; }
			val semanticType:Type = 'expression :&: 'cos
		}

		class Bind(geomType:Symbol, fileName: String) {
			def apply(expr:String) : Tuple = { return new Tuple(expr, fileName); }
			val semanticType:Type = 'expression :&: geomType =>: 'BoundFile('expression :&: geomType, 'FileName)
		}

		@combinator
		object BindCos extends Bind('cos, "CosFile.txt")

		@combinator
		object BindSin extends Bind('sin, "SinFile.txt")

	}

	val repository = new DivideExampleTrait {}

	val reflectedRepository = ReflectedRepository (repository, kinding=repository.kinding)

	val reply = reflectedRepository.inhabit[Tuple] ('BoundFile('expression, 'FileName))
	val it = reply.interpretedTerms.values.flatMap(_._2).iterator
	FragmentExplorer.processResultsTuple(it.asJava)
}