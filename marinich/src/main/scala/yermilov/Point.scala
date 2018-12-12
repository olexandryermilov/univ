package yermilov

class Point(val x: Number, val y: Number) {


  def +(that: Point): Point = {
    val k = (y - that.y) div (x - that.x)
    val x3 = k ^% (2, Point.mod)
    val y3 = -this.y + k * (this.x - x3)
    new Point(x3, y3)
  }

  def *(n: Number): Point = {
    var p = this
    var i = 0
    while(i<n) {
      p = p + this
      i = i + 1
    }
    p
  }

  def unary_-(): Point = {
    new Point(-x, -y)
  }

  override def toString: String = s"($x $y)"
}


object Point {
  var a = 12
  var b = 196
  var mod = 1000000007
}