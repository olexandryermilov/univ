import scala.collection.mutable

class Number(var value: String, var sign: Char) {

  import Number._

  private val zeroCharCode: Int = '0'

  def length = value.length

  def max(a: Int, b: Int) = Math.max(a, b)

  def cleanLeadingZeroes(s: String): String = {
    if (s.endsWith("0") && s.length > 1) cleanLeadingZeroes(s.substring(0, s.length - 1))
    else s
  }

  def +(n: Number): Number = {
    if (n.sign == this.sign) {
      new Number(absAdd(n).value, this.sign)
    }
    else {
      if (this.sign == '+')
        this - Number(n.value, '+')
      else
        n - Number(this.value, '+')
    }
  }

  def absAdd(n: Number): Number = {
    val res: StringBuilder = new StringBuilder
    var carry = 0
    for (i <- 0 to max(this.length, n.length)) {
      val addition = (if (this.length > i) this.value(i) - zeroCharCode else 0) + (if (n.length > i) n.value(i) - zeroCharCode else 0) + carry
      carry = addition / 10
      res.append(addition % 10)
    }
    Number(cleanLeadingZeroes(res.toString), sign)
  }

  def absSubtract(n: Number): Number = {
    val res: StringBuilder = new StringBuilder
    var carry = 0
    val iTo = max(this.length, n.length)
    for (i <- 0 to iTo) {
      var subtraction = (if (this.length > i) this.value(i) - zeroCharCode else 0) - (if (n.length > i) n.value(i) - zeroCharCode else 0) + carry
      if (subtraction < 0) {
        carry = -1
        subtraction = subtraction + 10
      }
      else carry = 0
      res.append(subtraction % 10)
    }

    Number(cleanLeadingZeroes(res.toString), sign)
  }

  def -(n: Number): Number = {
    if (this.sign == n.sign) {
      if (this.abs() > n.abs) {
        val number = absSubtract(n)
        Number(number.value, this.sign)
      }
      else {
        if (this == n) {
          Number(0)
        }
        else {
          if (this.sign == '+')
            Number((n absSubtract this).value, '-')
          else
            Number((n absSubtract this).value, '+')
        }
      }
    }
    else {
      if (this.abs > n.abs) {
        val number = absAdd(n)
        Number(number.value, this.sign)

      }
      else {
        if (this == n) {
          2 * this
        }
        else {
          if (this.sign == '+')
            Number((n absAdd this).value, '+')
          else
            Number((this absAdd n).value, '-')
        }
      }
    }
  }

  def abs(): Number = {
    Number(this.value, '+')
  }

  def /(n: Number): (Number, Number) = {
    if (n > this) (Number(0), this)
    else {
      var divider = n.abs()
      var resDiv = new StringBuilder
      var a = value.reverse
      var car = Number(0)
      var num = Number(a.substring(0, divider.length).reverse, '+')
      for (i <- divider.length to a.length) {
        var b = Number(0)
        var j = 0
        while (j <= 9 && (divider * Number(j + 1) <= num)) {
          j = j + 1
        }
        if (Number(j + 1) * divider > num) {
          b = Number(j) * divider
          resDiv.append(j)
        }
        car = num - b
        num = car * Number(10) + Number(if (i < a.length) a(i).toString else "0", '+')
      }
      var sign = if (this.sign == n.sign) '+' else '-'
      (Number(resDiv.toString.reverse, sign), car)
    }
  }

  def *(n: Number): Number = {
    var res1 = Number(0)
    var res2 = Number(0)
    if (this.length < n.length) n * this
    else {
      for (i <- 0 until n.length) {
        res2 = this multiplyByDigit (n.value(i) - '0')
        for (j <- 0 until i) {
          res2.multiplateByTen()
        }
        res1 = res1 + res2
      }
      val signOfResult = if (this.sign == n.sign) '+' else '-'
      Number(res1.value, signOfResult)
    }
  }

  def multiplyByDigit(num: Int): Number = {
    var carry = 0
    var res = new StringBuilder()
    for (i <- 0 to this.length) {
      val resMult = ((if (i < this.length) this.value(i) else '0') - '0') * num + carry
      res append (resMult % 10)
      carry = resMult / 10
    }
    Number(res.toString, this.sign)
  }

  def <(n: Number): Boolean = {
    !(this > n)
  }

  def >(n: Number): Boolean = {
    if (this == n) false
    else {
      if (this.sign != n.sign) {
        this.sign == '+'
      }
      else {
        if (this.length > n.length) {
          this.sign == '+'
        }
        else {
          if (this.length < n.length) {
            !(this.sign == '+')
          }
          else {
            (this.value.reverse > n.value.reverse) ^ (this.sign == '-')
          }
        }
      }
    }
  }


  def ==(n: Number): Boolean = {
    equals(n)
  }

  def !=(n: Number): Boolean = {
    !(this == n)
  }

  def <=(n: Number): Boolean = {
    this == n || this < n
  }

  def >=(n: Number): Boolean = {
    this == n || this > n
  }

  def +%(n: Number, mod: Number) = {
    (this + n) % mod
  }

  def -%(n: Number, mod: Number) = {
    (this - n) % mod
  }

  def /%(n: Number, mod: Number) = {
    (this + n) / mod
  }

  def *%(n: Number, mod: Number) = {
    (this * n) % mod
  }

  def %(n: Number): Number = {
    (this / n)._2
  }

  def ^(n: Number): Number = {
    if (n == 0) {
      1
    }
    else {
      if (n == 1) {
        this
      }
      else if (n == 2) {
        this * this
      }
      else {
        (this ^ (n div 2) ^ 2) * (this ^ (n % 2))
      }
    }
  }

  def ^%(n: Number, mod: Number): Number = {
    (this ^ n) % mod
  }

  def sqrt(): Number = {
    var res = Number(1)
    // ^ Number(this.length)
    var decreased = false
    var newRes = Number(1)
    while (true) {
      newRes = res + (this div res) div 2
      if ((decreased && res < newRes) || res == newRes) return res
      decreased = newRes < res
      res = newRes
    }
    res
  }

  def sqrtMod(mod: Number): Number = {
    sqrt % mod
  }

  def div(n: Number): Number = {
    (this / n)._1
  }

  override def toString: String = (if (this.sign == '-') this.sign else "") + this.value.reverse

  override def equals(obj: scala.Any): Boolean = obj match {
    case num: Number => if (this.sign == num.sign && this.value == num.value) true else false
    case n: Long => equals(Number(n))
    case n: Int => equals(Number(n))
    case _ => false
  }

  def unary_- = Number(this.value, if (this.sign == '+' && this.value != "0") '-' else '+')

  private def multiplateByTen(): Unit = {
    this.value = "0" + this.value
  }

  def inverseElementForMod(mod: Number): Option[Number] = {
    val (g, x, _) = gcdExtended(this, mod)
    if (g == Number(1)) {
      Some((x % mod + mod) % mod)
    }
    else {
      None
    }
  }

}

object Number extends App {
  val ZERO = Number(0)

  def apply(int: Int): Number = {
    if (int == 0)
      new Number("0", '+')
    else {
      var absI = Math.abs(int)
      val s: StringBuilder = new StringBuilder
      while (absI > 0) {
        s.append(absI % 10)
        absI = absI / 10
      }
      new Number(s.toString(), if (int >= 0) '+' else '-')
    }
  }

  def apply(int: Long): Number = {
    if (int == 0)
      new Number("0", '+')
    else {
      var absI = Math.abs(int)
      val s: StringBuilder = new StringBuilder
      while (absI > 0) {
        s.append(absI % 10)
        absI = absI / 10
      }
      new Number(s.toString(), if (int >= 0) '+' else '-')
    }
  }

  def cleanLeadingZeroes(s: String): String = {
    if (s.endsWith("0") && s.length > 1) cleanLeadingZeroes(s.substring(0, s.length - 1))
    else s
  }

  def apply(value: String, sign: Char) = new Number(cleanLeadingZeroes(value), sign)

  def apply(number: Number): Number = number

  def max(a: Number, b: Number): Number = if (a > b) a else b

  implicit def intToNumber(n: Int): Number = Number(n)

  /**
    * Solve equations like a*x === b(mod m)
    *
    * @param a
    * @param b
    * @param m
    * @return x
    */
  def solveEquation(a: Number, b: Number, m: Number): Option[Number] = {
    var (g, x1, y1) = gcdExtended(a.abs(), m.abs())
    if (b % g == 0) {
      x1 *= b div g
      y1 *= b div g
      if (a < 0) x1 *= -1
      if (m < 0) y1 *= -1
      Some(x1)
    }
    else None
  }

  def solveSystem(a: Seq[Number], m: Seq[Number]): Option[Number] = {
    var x: mutable.Seq[Number] = mutable.Seq()
    for (i <- a.indices) {
      x = x ++ Seq(a(i))
      for (j <- 0 until i) {
        val r = reversed(m, j, i).getOrElse(return None)
        x(i) = r * (x(i) - x(j))
        x(i) = x(i) % m(i)
        if (x(i) < 0) x(i) = x(i) + m(i)
      }
    }
    var res = Number(0)
    var mult = Number(1)
    for (i <- x.indices) {
      res = res + x(i) * mult
      mult = mult * m(i)
    }
    Some(res)
  }

  def gcd(a: Number, b: Number): Number = {
    if (b == Number(0))
      a
    else
      gcd(b, a % b)
  }

  def gcdExtended(a: Number, b: Number): (Number, Number, Number) = {
    if (a == Number(0)) {
      (b, Number(0), Number(1))
    }
    else {
      val (d, x1, y1) = gcdExtended(b % a, a)
      (d, y1 - ((b div a) * x1), x1)
    }
  }

  def reversed(m: Seq[Number], i: Int, j: Int): Option[Number] = {
    m(i).inverseElementForMod(m(j))
  }
}
