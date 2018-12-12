package yermilov

import org.scalatest.{FlatSpec, Matchers}

class CryptographySpec extends FlatSpec with Matchers {

  import Number._
  import Cryptography._

  "rhoPollardFactorize" should
    "9699690" in {
    val num = 9699690
    val res = rhoPollardFactorize(num)
    var t1: Number = 1
    for (i <- res.indices) {
      t1 = t1 * res(i)
      isPrimeSqrt(res(i)) should be(true)
    }
    t1 should be(num)
  }

  "rhoPollardFactorize" should
    "pentest" in {
    for (num <- 2 to 1300) {
      val res = rhoPollardFactorize(num)
      var t1: Number = 1
      for (i <- res.indices) {
        t1 = t1 * res(i)
        if(!isPrimeSqrt(res(i))) println(s"${res(i)} $num")
        isPrimeSqrt(res(i)) should be(true)
      }
      t1 should be(num)
    }
  }


  "isPrime" should
  "pentest" in {
    for(num <- 2 to 1000){
      var res = isPrime(num, Math.min(50,num-1))
      if(isPrimeSqrt(num) != res) println(num)
      res should be(isPrimeSqrt(num))
    }
  }

  it should "15,485,863" in{
    isPrime(15485863, 20) should be(true)
  }

  it should "15,485,864" in{
    isPrime(15485864, 20) should be(false)
  }

  it should "29,996,224,275,833" in{
    isPrime(Number("29996224275833".reverse,'+'), 20) should be(true)
  }

  "jacobi" should "" in{
    jacobi(219,383) should be(1)
  }

  "lezhandr" should "" in{
    lezhandr(219,383) should be(1)
  }

  "mebius" should "" in{
    mebius(81) should be(0)
  }

  "euler" should "" in {
    euler(7) should be(6)
  }

  it should "for big prime number" in {
    euler(Number("29996224275833".reverse,'+')) should be(Number("29996224275832".reverse,'+'))
  }


  def isPrimeSqrt(n: Number): Boolean = {
    if (n == 2) true
    else if (n % 2 == 0) false
    else {
      var i: Number = 3
      var res = true
      while (res && i <= n.sqrt()) {
        if (n % i == 0) res = false
        i = i + 2
      }
      res
    }
  }



}
