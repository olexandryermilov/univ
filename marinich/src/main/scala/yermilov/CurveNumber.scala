package yermilov

class CurveNumber(val curve: Curve, val x: Number, val y: Number) {

  import Number._

  def +(b: CurveNumber): CurveNumber = {
    if (this.x == b.x && this.y != b.y) {
      return new CurveNumber(this.curve, 0, 0)
    }
    if (this.x == 0 && this.y == 0) {
      return b
    }
    if (b.x == 0 && b.y == 0) {
      return this
    }
    val a = this
    val slope = if (this == b) {
      ((3 * a.x * a.x + a.curve.a) * (2 * a.y).rev(a.curve.p)) % a.curve.p
    }
    else {
      ((a.y + a.curve.p - b.y) * ((a.x + a.curve.p - b.x).rev(a.curve.p))) % a.curve.p
    }
    val cx = (slope * slope + ((2 * a.curve.p - a.x) - b.x)) % a.curve.p
    val cy = (slope * (a.x + a.curve.p - cx) + a.curve.p - a.y) % a.curve.p
    new CurveNumber(a.curve, cx, cy)
  }

  override def toString: String = s"($x $y)"

  def ==(b: CurveNumber): Boolean = {
    x == b.x && y == b.y
  }

  def *(b: Number, i: Number = 0): CurveNumber = {
    if (b == 2) this + this
    else if (b == 1) return this
    else if (b == 0) return new CurveNumber(this.curve, 0, 0) else {
      //println(s"$i b = $b")
      val curveNum = this * (b div 2, i + 1)
      //println(s"$i curveNum = $curveNum")
      if (b % 2 == 1) curveNum + curveNum + this
      else curveNum + curveNum
    }
  }

  def inverse(): CurveNumber = {
    new CurveNumber(curve, x, (curve.p - y) % curve.p)
  }
}
