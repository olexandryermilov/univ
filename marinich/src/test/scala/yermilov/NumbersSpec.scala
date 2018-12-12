package yermilov

import org.scalatest.{FlatSpec, Matchers}

import scala.util.Random

class NumbersSpec extends FlatSpec with Matchers {

  import Number._
  "+" should
    "7 + 6  == 13" in {
    val first = 7
    val second = 6
    (Number(first) + Number(second)) should be(Number(first + second))
  }
  it should " -7 + -6 == -13" in {
    val first = 7
    val second = 6
    (Number(first) + Number(second)) should be(Number(first + second))
  }

  it should " -7 + 6 == -1" in {
    val first = -7
    val second = 6
    (Number(first) + Number(second)) should be(Number(first + second))
  }

  it should " 7 + -6 == 1" in {
    val first = 7
    val second = -6
    (Number(first) + Number(second)) should be(Number(first + second))
  }

  it should "pentest" in {
    for (i <- 5000 to 5000) {
      for (j <- -5000 to 5000)
        Number(i) + Number(j) should be(Number(i + j))
    }
  }


  ">" should
    "7>1" in {
    val first = 7
    val second = 1
    Number(first) > Number(second) should be(true)
  }
  it should "173 > 34" in {
    val first = 173
    val second = 34
    Number(first) > Number(second) should be(true)
  }

  it should "173 > 171" in {
    val first = 173
    val second = 171
    Number(first) > Number(second) should be(true)
  }

  it should "173 > -34" in {
    val first = 173
    val second = -34
    Number(first) > Number(second) should be(true)
  }

  it should "-173 > 34" in {
    val first = -173
    val second = 34
    Number(first) > Number(second) should be(false)
  }

  it should "-7>-1" in {
    val first = -7
    val second = -1
    Number(first) > Number(second) should be(false)
  }
  it should "-173 > -34" in {
    val first = -173
    val second = -34
    Number(first) > Number(second) should be(false)
  }

  it should "-173 > -171" in {
    val first = -173
    val second = -171
    Number(first) > Number(second) should be(false)
  }

  "<" should
    "7<1" in {
    val first = 7
    val second = 1
    Number(first) < Number(second) should be(false)
  }
  it should "173 < 34" in {
    val first = 173
    val second = 34
    Number(first) < Number(second) should be(false)
  }

  it should "173 < 171" in {
    val first = 173
    val second = 171
    Number(first) < Number(second) should be(false)
  }

  it should "173 < -34" in {
    val first = 173
    val second = -34
    Number(first) < Number(second) should be(false)
  }

  it should "-173 < 34" in {
    val first = -173
    val second = 34
    Number(first) < Number(second) should be(true)
  }

  it should "-7<-1" in {
    val first = -7
    val second = -1
    Number(first) < Number(second) should be(true)
  }
  it should "-173 < -34" in {
    val first = -173
    val second = -34
    Number(first) < Number(second) should be(true)
  }

  it should "-173 < -171" in {
    val first = -173
    val second = -171
    Number(first) < Number(second) should be(true)
  }

  "==" should
    "7==7" in {
    val first = 7
    val second = 7
    Number(first) == Number(second) should be(true)
  }

  it should "-7==-7" in {
    val first = -7
    val second = -7
    Number(first) == Number(second) should be(true)
  }

  it should "7==-7" in {
    val first = 7
    val second = -7
    Number(first) == Number(second) should be(false)
  }

  it should "-7==7" in {
    val first = -7
    val second = 7
    Number(first) == Number(second) should be(false)
  }

  "!=" should
    "7!=7" in {
    val first = 7
    val second = 7
    Number(first) != Number(second) should be(false)
  }

  it should "-7!=-7" in {
    val first = -7
    val second = -7
    Number(first) != Number(second) should be(false)
  }

  it should "7!=-7" in {
    val first = 7
    val second = -7
    Number(first) != Number(second) should be(true)
  }

  it should "-7!=7" in {
    val first = -7
    val second = 7
    Number(first) != Number(second) should be(true)
  }

  "*" should
    "7*5=35" in {
    val first = 7
    val second = 5
    Number(first) * Number(second) should be(Number(first * second))
  }

  it should "77*5=385" in {
    val first = 77
    val second = 5
    Number(first) * Number(second) should be(Number(first * second))
  }

  it should "7777*5555=43201235" in {
    val first = 7777
    val second = 5555
    Number(first) * Number(second) should be(Number(first * second))
  }

  it should "7777777777776546465L*555543432342332432L" in {
    val first = 7777777777776546465L
    val second = 555543432342332432L
    Number(first) * Number(second) should be(Number("4320893362661901534495368583924452880".reverse, '+'))
  }

  it should "check for signs" in {
    var first = -1
    var second = 1
    Number(first) * Number(second) should be(Number(first * second))
    first = 1
    second = -1
    Number(first) * Number(second) should be(Number(first * second))
    first = -1
    second = -1
    Number(first) * Number(second) should be(Number(first * second))
  }

  it should "pentest" in {
    for (i <- 5000 to 5000) {
      for (j <- -5000 to 5000)
        Number(i) * Number(j) should be(Number(i * j))
    }
  }

  "%" should "give right result" in {
    var first = 100
    var second = 15
    Number(first) % Number(second) should be(Number(first % second))
    first = 11000
    second = 11000
    Number(first) % Number(second) should be(Number(first % second))
    first = 100
    second = 200
    Number(first) % Number(second) should be(Number(first % second))
  }

  it should "pentest" in {
    for (i <- 5000 to 5000) {
      for (j <- -5000 to 5000)
        if (j != 0)
          Number(i) % Number(j) should be(Number(i % j))
    }
  }

  "random" should "be sane" in{
    for(i<-1 to 10){
      var a = Random.nextInt(15)
      var b = 15 + Random.nextInt(100)
      var big = random(a,b)
      //println(s"$big $a $b")
      big<=b&&big>=a should be(true)
    }
  }

  "/" should "100/3" in {
    val first = 100
    val second = 3
    Number(first) / Number(second) should be((Number(first / second), Number(first % second)))
  }

  it should "pentest" in {
    for (i <- -500 to 500) {
      for (j <- -500 to 500)
        if (j != 0)
          Number(i) / Number(j) should be((Number(i / j), Number(i % j)))
    }
  }

  it should "1/3" in {
    val first = 1
    val second = 3
    Number(first) / Number(second) should be((Number(first / second), Number(first % second)))
  }

  it should "1030/32" in {
    val first = 1030
    val second = 32
    Number(first) / Number(second) should be((Number(first / second), Number(first % second)))
  }

  it should "111/75" in {
    val first = 111
    val second = 75
    Number(first) / Number(second) should be((Number(first / second), Number(first % second)))
  }

  it should "75/111" in {
    val first = 75
    val second = 111
    Number(first) / Number(second) should be((Number(first / second), Number(first % second)))
  }

  "^" should "5^5" in {
    val first = 5
    val second = 5
    Number(first) ^ Number(second) should be(Number(pow(first, second)))
  }

  it should "5^1" in {
    val first = 5
    val second = 1
    Number(first) ^ Number(second) should be(Number(pow(first, second)))
  }

  it should "5^0" in {
    val first = 5
    val second = 0
    Number(first) ^ Number(second) should be(Number(pow(first, second)))
  }

  def pow(a: Int, b: Int): Long = {
    var res = 1L
    for (i <- 1 to b) {
      res = res * a
    }
    res
  }

  "sqrt" should "sqrt(1)" in {
    val first = 1
    val sqrt = 1
    Number(first).sqrt() should be(Number(sqrt))
  }

  it should "sqrt (0)" in {
    val first = 0
    val sqrt = 0
    Number(first).sqrt() should be(Number(sqrt))
  }

  it should "sqrt (16)" in {
    val first = 16
    val sqrt = 4
    Number(first).sqrt() should be(Number(sqrt))
  }

  it should "sqrt (15)" in {
    val first = 15
    val sqrt = 3
    Number(first).sqrt() should be(Number(sqrt))
  }

  it should "sqrt (17)" in {
    val first = 17
    val sqrt = 4
    Number(first).sqrt() should be(Number(sqrt))
  }

  "-" should "pentest" in {
    for (i <- -500 to 500) {
      for (j <- -500 to 500) {
        //println(Number(i) +" "+  Number(j))
        (Number(i) - Number(j)) should be(Number(i - j))
      }
    }
  }

  "^%" should "pentest" in {
    for (i <- 0 to 5000) {
      for (j <- 0 to 5) {
        for (k <- 2 to 30) {
          if(i+j!=0)(Number(i) ^%(Number(j),Number(k))) should be(Number(pow(i,j)%k))
        }
      }
    }
  }

  it should "be sane" in {
    Number("100000000000000",'+') ^%(Number("42432342000034243",'+'),Number(10))
  }

  "solve system" should "solve some system" in {
    solveSystem(m = Seq(7, 5, 101), a = Seq(3, 1, 5)) should be(Some(Number(1116)))
  }

  "solve equation" should "solve equation from book" in {
    solveEquation(111, 75, 321) should be(Some(Number(-650)))
  }
}
