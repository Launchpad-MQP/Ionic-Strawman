import de.tu_dortmund.cs.ls14.cls.interpreter.ReflectedRepository
import de.tu_dortmund.cs.ls14.cls.types.syntax._
import de.tu_dortmund.cs.ls14.cls.types._
import de.tu_dortmund.cs.ls14.cls.interpreter.combinator

import scala.collection.JavaConverters._
import ionicmqp2016._;
import _root_.java.nio.file._

object FactorialExample extends App {
  trait FactorialExampleTrait {
    
    // Scala enhancement
    trait Expr 
    case class Const (n:Int) extends Expr
    case class Mult (e1:Expr, e2:Expr) extends Expr
    
    @combinator object Fact1 {
      def apply() : String = { return "1"; }
      val semanticType:Type = 'factorial('N1);
    }
    
    class FactorialExtension(current:Type, previous:Type) {
      def apply(previousFact:String) : String = { return "(" + current.toString().substring(1) + ") " + "*" + previousFact; }
      val semanticType:Type = 'factorial(previous) =>: 'factorial(current);
    }
   
    @combinator object Fact2 extends FactorialExtension ('N2, 'N1)
    @combinator object Fact3 extends FactorialExtension ('N3, 'N2)
    @combinator object Fact4 extends FactorialExtension ('N4, 'N3)
    @combinator object Fact5 extends FactorialExtension ('N5, 'N4)
    @combinator object Fact6 extends FactorialExtension ('N6, 'N5)
   
  }
  
  val repository = new FactorialExampleTrait { }
  
  val reflectedRepository = ReflectedRepository (repository)
  
  // 965 trials finds it!
  val reply = reflectedRepository.inhabit[String]  ('factorial('N5))
  // with these results
  val it = reply.interpretedTerms.values.flatMap(_._2).iterator
  
  //MySample.processResults(it.asJava)
  FragmentExplorer.processResults(it.asJava)
  val output = Paths.get("SomeFile.txt")
  
  //val reply2 = reflectedRepository.inhabit[String] ('expression, output)
   
  // with these results
  //val i2t = reply2.interpretedTerms.values.flatMap(_._2).iterator
  
  
  
  
}