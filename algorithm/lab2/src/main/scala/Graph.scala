case class Graph(vertices: Int, edges: Seq[(Int, Int)], var colors: Array[Color] = Array.empty, var vertEdges: Seq[Seq[Int]] = Seq.empty)

sealed trait Color

case class Black() extends Color

case class White() extends Color
