package yermilov

import java.util.Collections

import scala.collection.mutable

object Cryptography extends App {

  import Number._

  def rhoPollardFactorize(number: Number): Seq[Number] = {
    def rhoPollard(n: Number): Number = {
      if (n == 1) return 1
      var x: Number = if (n < 3) 1 else random(1, n - 2)
      var y: Number = 1
      var i: Number = 0
      var stage: Number = 2
      while (gcd(n, (x - y).abs) == Number(1)) {
        if (i == stage) {
          y = x
          stage = stage * 2
        }
        x = (x * x - 1) % n
        i = i + 1
      }
      gcd(n, (x - y).abs)
    }

    var res = Seq[Number]()
    var n = number
    println(n)
    if (isPrime(n, 90)) {
      res ++ Seq(n)
    }
    else {
      println("h")
      while (n > 1) {
        var div = rhoPollard(n)
        var ndivdiv = n div div
        if (div != Number(1) && div != n) {
          res = res ++ rhoPollardFactorize(div) ++ rhoPollardFactorize(ndivdiv)
          res.map(x => if (!isPrime(x, 5)) throw new Exception) //rhoPollardFactorize(x))
          return res.sorted
        }
        //n = ndivdiv
      }
      res.sorted
    }
  }

  def isPrime(n: Number, k: Int): Boolean = {
    if (n <= 3) true else {
      var j = 2
      while (j * j <= n && j <= 100) {
        if (n % j == 0) return false
        j = j + 1
      }
      for (i <- 1 to k) {
        val a: Number = random(2, n - 1)
        if (gcd(a, n) != Number(1)) {
          return false
        }
        if ((a ^% (((n - 1) div 2), n)) == (a div n)) return false
      }
      true
    }
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


  def disLog(a: Number, b: Number, p: Number): Number = {
    var u0: Number = 0
    var u1: Number = 0
    var v0: Number = 0
    var v1: Number = 0
    var z0: Number = 1
    var z1: Number = 1
    if (!isPrime(p, 90)) {
      throw new Exception("p is not prime")
    }
    for (i <- 1 to 10000) {
      if (z0 * 3 < p) u0 = (u0 + 1) % (p - 1)
      else if (z0 * 3 < p * 2) u0 = (u0 + u0) % (p - 1)
      if (z0 * 3 > p * 2) v0 = (v0 + 1) % (p - 1)
      else if (z0 * 3 > p) v0 = (v0 + v0) % (p - 1)
      z0 = ((a ^% (u0, p)) * (b ^% (v0, p))) % p
      if (z1 * 3 < p) u1 = (u1 + 1) % (p - 1)
      else if (z1 * 3 < p * 2) u1 = (u1 + u1) % (p - 1)
      if (z1 * 3 > p * 2) v1 = (v1 + 1) % (p - 1)
      else if (z1 * 3 > p) v1 = (v1 + v1) % (p - 1)
      z1 = ((a ^% (u1, p)) * (b ^% (v1, p))) % p
      if (z1 * 3 < p) u1 = (u1 + 1) % (p - 1)
      else if (z1 * 3 < p * 2) u1 = (u1 + u1) % (p - 1)
      if (z1 * 3 > p * 2) v1 = (v1 + 1) % (p - 1)
      else if (z1 * 3 > p) v1 = (v1 + v1) % (p - 1)
      z1 = ((a ^% (u1, p)) * (b ^% (v1, p))) % p
      if (z0 == z1) {
        val d1: Number = (u0 + p - 1 - u1) % (p - 1)
        val d2: Number = (v1 + p - 1 - v0) % (p - 1)

        if (gcd(d2, p - 1) > 1) {
          val d = gcd(d2, p - 1)
          if (d > 10) { // 1000
            val x0 = (d2.rev((p - 1) div d) * d1) % (p - 1)
            var q: Number = x0
            var m = 0
            while (m < d - 1) {
              if ((a ^% (q, p)) == b) return q
              q = q + (p - 1) div d
              m = m + 1
            }
          }
          else {
            val q = (d2.rev(p - 1) * d1) % (p - 1)
            return q
          }
        }
      }
    }
    0
  }

  def jacobi(_a: Number, _b: Number, n: Number = 0): Number = {
    var a = _a
    var b = _b
    if (gcd(a, b) != 1)
      return 0

    var r = 1
    var pow2 = 0
    while (a > 0) {
      pow2 = 0
      while (a % 2 == 0) {
        a = a div 2
        pow2 = pow2 + 1
      }

      if (pow2 % 2 == 1 && (b % 8 == 3 || b % 8 == 5))
        r = -r

      if (a % 4 == 3 && b % 4 == 3)
        r = -r

      var c = a
      a = b % c
      b = c
    }
    if (r == 1)
      return r
    else
      return n - 1
  }

  def lezhandr(a: Number, p: Number, n: Number = 3): Number = {
    if (!isPrime(p, 15)) {
      throw new Exception("p is not prime")
    }
    jacobi(a, p, n)
  }

  def euler(n: Number): Number = {
    val div = rhoPollardFactorize(n)
    var r: Number = 1
    var i = 0
    while (i < div.size) {
      r = r * (div(i) - 1)
      while (i < div.size - 1 && (div(i) == div(i + 1))) {
        r = r * div(i)
        i += 1
      }
      i += 1
    }
    r
  }

  def mebius(n: Number): Number = {
    var c = 0
    val div = rhoPollardFactorize(n)
    var i = 0
    while (i < div.size) {
      c += 1
      if (i < div.size - 1 && (div(i) == div(i + 1))) return 0
      i += 1
    }
    1 - 2 * (c % 2)
  }

  //noinspection ComparingUnrelatedTypes
  def getPrimeRoot(p: Number): Number = {
    if (!isPrime(p, 20)) {
      throw new Exception("Number is not prime")
    }
    val phi = p - 1
    val factor = rhoPollardFactorize(phi)
    var tr = Seq[Number]()
    var i = 0
    while (i < factor.size) {
      if (i == 0 || (factor(i) != factor(i - 1))) tr = tr ++ Seq(phi div factor(i))
      i += 1
    }
    var root = 2
    while (root < p) {
      var ok = true
      for (j <- tr) {
        if ((root ^% (j, p)) == 1) {
          ok = false
        }
      }
      if (ok) return root
      root = root + 1
    }
    0
  }

  private def fact(n: Number): Number = {
    var res: Number = 1
    var i: Number = 1
    while (i <= n) {
      res = res * i
      i = i + 1
    }
    res
  }

  private def c(n: Number, k: Number): Number = {
    var a = fact(n)
    var b = fact(k)
    var c = fact(n - k)
    (a div b) div c
  }

  private def calcSqrt(a: Number, b: Number, p: Number): Number = {
    var k: Number = p - (p % 2)
    var res: Number = 0
    while (k >= 0) {
      res = res + c(p, k) * (a ^ (p - k)) * (b ^ (k div 2))
      println(s"res = $res k=$k c = ${c(p, k)} a= ${a ^ (p - k)} b = ${b ^ (k div 2)}")
      k = k - 2
    }
    res
  }

  def cipolla(p: Number, n: Number): Number = {
    if (!isPrime(p, 20)) {
      throw new Exception("Number is not prime")
    }
    var a: Number = 2
    while (lezhandr(a * a - n, p) == 1) {
      a = random(1, p)
    }
    var sqrt = calcSqrt(a, a * a - n, (p + 1) div 2)
    println(s"sqrt = $sqrt a = $a b = ${a * a - n} p = ${(p + 1) div 2}")
    (a + sqrt) ^% ((p + 1) div 2, p * p)
  }

  def tonelli(p: Number, n: Number): Number = {
    var k = p - 1
    var rpf = rhoPollardFactorize(k)
    var s = rpf.count(_ == 2)
    var q: Number = 1
    for (i <- rpf.filterNot(_ == 2)) {
      q = q * i
    }
    if (s == 1) {
      return n ^% ((p + 1) div 4, p)
    }
    var z: Number = 1
    while (lezhandr(z, p) != -1 && z != p - 1) {
      z = z + 1
    }
    var c = z ^% (q, p)
    var r = n ^% ((q + 1) div 2, p)
    var t = n ^% (q, p)
    var m = s
    while (true) {
      if (t % p == 1) return r
      var i = 1
      while ((t ^% (2 ^ i, p)) != 1) i = i + 1
      var b = c ^% (2 ^ (m - i - 1), p)
      r = (r * b) % p
      t = (t * b * b) % p
      c = (b * b) % p
      m = i
    }
    r
  }

  def generatePrime(num: Number = 16): Number = {
    var res = random(0, Number("10000000000000000000000000000000".reverse, '+')) + num
    if (res % 2 == 0) res = res + 1
    while (!isPrime(res, 30)) res = res + 2
    res
  }

  def elHamal(text: Option[Number] = None): Unit = {
    var p = generatePrime()
    var g = getPrimeRoot(p)
    var x = random(0, p)
    var y = g ^% (x, p)
    println(s"Open key = ($p, $g, $y)")
    println(s"Closed key = $x")
    var m = text match {
      case Some(num) => num
      case None => random(0, p - 1)
    }
    println(s"Text = $m")
    val k = random(0, p)
    println(s"Session key = $k")
    val a = g ^% (k, p)
    val b = ((y ^% (k, p)) * m) % p
    println(s"Code = ($a, $b)")
    val decoded = (b * (a ^% (x, p)).rev(p)) % p
    println(s"Decoded = $decoded")
  }

  def RSA(text: Option[Number] = None): Unit = {
    val p = generatePrime()
    val q = generatePrime(p + 1)
    val n = p * q
    val phi = (p - 1) * (q - 1)
    var e: Number = 3

    while (gcd(e, phi) > 1) e = random(0, phi)

    val (d, temp, k): (Number, Number, Number) = extEuclid(e, phi, 0, 0, phi)

    println(s"phi = $phi")
    println(s"n = $n, p = $p q = $q")
    println(s"Open key = ($n, $e)")
    println(s"Closed key = ($n, $d)")
    var m = text match {
      case Some(num) => num
      case None => random(0, n - 1)
    }
    println(s"Text = $m")

    val c = m ^% (e, n)
    println(s"Code = $c")

    val decoded = c ^% (d, n)

    println(s"Decoded = $decoded")
  }

  lazy val p = Number("115792089237316195423570985008687907853269984665640564039457584007908834671663".reverse, '+')
  lazy val a: Number = 0
  lazy val b: Number = 7
  lazy val curve = Curve(a,b,p)
  lazy val x = Number("55066263022277343669578718895168534326250603453777594175500187360389116729240".reverse, '+')
  lazy val y = Number("32670510020758816978083085130507043184471273380659243275938904335757337482424".reverse, '+')
  lazy val G = new CurveNumber(curve, x, y)
  lazy val period = Number("115792089237316195423570985008687907852837564279074904382605163141518161494337".reverse, '+')
  lazy val map = mutable.Map[CurveNumber, Number]()

  def prepare(): Unit = {
    map.put(G, 1)
    for(i<-2 to 15){
      map.put(G + map.keys.toSeq(i-2), i)
    }
  }


  def elHamalCr(text: Option[Number] = None): Unit = {
    var m = text match {
      case Some(num) => num
      case None => random(0, 15000000)
    }
    println(s"Text is $m")
    val X = random(0, period)
    val Y = G * X
    val M = G * m

    val K = random(0, period)
    val A = G * K
    val B = Y * K+ M

    println(s"A = $A")
    println(s"B = $B")
    val MD = B + (A*X).inverse()
    println(s"Decrypted message = $MD")
    println(s"Message is ${m}")
  }

  def discreteLog(a: Number, b: Number, p: Number): Number = {
    val n: Number = p.sqrt + 1
    val vals = mutable.Map[Number, Number]()
    var an: Number = 1
    var i: Number = n
    while (i >= 1) {
      vals.put(a ^% (i * n, p), i)
      i = i - 1
    }
    i = 0
    while (i <= n) {
      val cur = ((a ^% (i, p)) * b) % p
      if (vals.contains(cur)) {
        val ans = vals(cur) * n - i
        if (ans < p) return ans
      }
      i = i + 1
    }
    -1
  }

  private def gen(p: Number, small: Number): Number = {
    val fact = rhoPollardFactorize(p - 1)
    val phi = p - 1
    var res: Number = 2
    while (res <= p) {
      var ok = true
      var i = 0
      while (i < fact.size && ok) {
        ok = ok && (res ^% (phi div fact(i), p)) != 1
        i = i + 1
      }
      if (ok && res > small) return res
      res = res + 1
    }
    -1
  }

  def discreteRoot(a: Number, n: Number, k: Number = 2): Number = {
    var sq = n.sqrt() + 1
    var dec = Seq[(Number, Number)]()
    var g: Number = gen(n, 0)
    var small: Number = 1
    while (small < n - 1) {
      g = gen(n, small)
      var res = discreteLog(g ^ k, a, n)
      if (((g ^% (res, n)) ^% (k, n)) == a) return (g ^% (res, n))
      else small = g
    }
    -1
  }

  println(discreteRoot(10,13))

}
