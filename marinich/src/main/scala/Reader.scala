import java.io.{BufferedReader, InputStreamReader}
import scala.language.implicitConversions
import yermilov.Number

object Reader extends App {

  import Reader._
  import yermilov.Cryptography._

  override def main(args: Array[String]): Unit = {
    while (true) {
      //prepare()
      val x = new BufferedReader(new InputStreamReader(System.in))
      while (true) {
        println(
          """
            |1. Перевірити чи є число простим
            |2. Факторизувати число
            |3. Функція Ейлера
            |4. Функція Мьобіуса
            |5. Символ Лежандра
            |6. Символ Якобі
            |7. Дискретний квадратний корінь
            |8. Дискретний логарифм
            |9. Криптосистема над еліптичними кривими
            |10. Криптосистема Ель-Гамаля
          """.stripMargin)
        var r = x.readLine().toInt
        r match {
          case 1 => var num = Number(x.readLine().reverse, '+')
            println(isPrime(num, 1000))
          case 2 => var num = Number(x.readLine().reverse, '+')
            println(rhoPollardFactorize(num))
          case 3 => var num = Number(x.readLine().reverse, '+')
            println(euler(num))
          case 4 => var num = Number(x.readLine().reverse, '+')
            println(mebius(num))
          case 5 => var a = Number(x.readLine().reverse, '+')
            var b = Number(x.readLine().reverse, '+')
            println(lezhandr(a, b))
          case 6 => var a = Number(x.readLine().reverse, '+')
            var b = Number(x.readLine().reverse, '+')
            println(jacobi(a, b))
          case 7 => var a = Number(x.readLine().reverse, '+')
            var b = Number(x.readLine().reverse, '+')
            println(s"$a $b")
            println(discreteRoot(a, b))
          case 8=> var a = Number(x.readLine().reverse, '+')
            var b = Number(x.readLine().reverse, '+')
            var p = Number(x.readLine().reverse, '+')
            println(discreteLog(a, b, p))
          case 9=> var num = Number(x.readLine().reverse, '+')
            elHamalCr(Some(num))
          case 10 => var num = Number(x.readLine().reverse, '+')
            elHamal(Some(num))
        }
      }
      //println(parse(x.readLine()))
    }
  }

  def parse(x: String): String = {
    var str = x.toLowerCase
    if (beginsWith(str, "system")) {
      val ops = str.split(" ").tail
      val n = Integer.valueOf(ops.head)
      var a = Seq[Number]()
      var m = Seq[Number]()
      for (i <- 1 to n) {
        a = a ++ Seq(toNumber(ops(i)).right.get)
      }
      for (i <- 1 to n) {
        m = m ++ Seq(toNumber(ops(i + n)).right.get)
      }
      Number.solveSystem(a, m) match {
        case Some(value) => value.toString
        case _ => "No answer"
      }
    }
    else {
      val ops = str.split(" ")
      var stack = List[Number]()
      var res = ""
      for (i <- ops.indices) {
        toNumber(ops(i)) match {
          case Right(value) => stack = stack ++ List(value)
          case Left(value) => {
            ops(i) match {
              case "+" =>
                val a = stack.last
                val b = stack(stack.length - 2)
                res = (a + b) toString
              case "-" =>
                val a = stack.last
                val b = stack(stack.length - 2)
                res = (a - b) toString
              case "*" =>
                val a = stack.last
                val b = stack(stack.length - 2)
                res = (a * b) toString
              case "/" =>
                val a = stack.last
                val b = stack(stack.length - 2)
                res = (a / b) toString
              case "^" =>
                val a = stack.last
                val b = stack(stack.length - 2)
                res = (a ^ b) toString
              case "gcd" =>
                val a = stack.last
                val b = stack(stack.length - 2)
                res = Number.gcd(a, b) toString
              case "%" =>
                val a = stack.last
                val b = stack(stack.length - 2)
                res = a % b toString
              case "+%" =>
                val a = stack.last
                val b = stack(stack.length - 2)
                val c = stack.head
                res = a +% (b, c) toString
              case "-%" =>
                val a = stack.last
                val b = stack(stack.length - 2)
                val c = stack.head
                res = a -% (b, c) toString
              case "/%" =>
                val a = stack.last
                val b = stack(stack.length - 2)
                val c = stack.head
                res = a /% (b, c) toString()
              case "*%" =>
                val a = stack.last
                val b = stack(1)
                val c = stack.head
                res = a *% (b, c) toString
              case "^%" =>
                val a = stack.last
                val b = stack(1)
                val c = stack.head
                res = a ^% (b, c) toString
              case "sqrt" =>
                val a = stack.head
                res = a sqrt() toString
              case ">" =>
                val a = stack.last
                val b = stack(stack.length - 2)
                res = (a > b) toString
              case "<" =>
                val a = stack.last
                val b = stack(stack.length - 2)
                res = (a < b) toString
              case "==" =>
                val a = stack.last
                val b = stack(stack.length - 2)
                res = (a == b) toString
            }
          }
        }
      }
      res
    }
  }

  implicit def beginsWith(x: String, y: String): Boolean = x.reverse.endsWith(y.reverse)

  implicit def reversed(toReverse: String): String = {
    def tailRec(x: String, res: String): String = {
      if (x.isEmpty) res
      else tailRec(x.substring(1), res + x.head)
    }

    tailRec(toReverse, "")
  }

  def toNumber(x: String): Either[Error, Number] = {
    if (isNumber(x)) {
      if (x.startsWith("-")) Right(Number(x.tail.reverse, '-'))
      else Right(Number(x.reverse, '+'))
    }
    else {
      Left(new Error)
    }
  }

  def isNumber(str: String): Boolean = {
    if (str.head == '+' || str.head == '-' && str.length > 1)
      for (i <- 1 until str.length) {
        if (str(i) < '0' || str(i) > '9') return false
      }
    if (str.head == '+' || str.head == '-' && str.length == 1) return false
    else if (str.head >= '0' && str.head <= '9')
      for (i <- 0 until str.length) {
        if (str(i) < '0' || str(i) > '9') return false
      }
    else {
      return false
    }
    true
  }
}
////999999000001
////70312316987348207
//17649306047