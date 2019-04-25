import scala.io.Source

object App extends App {

  def path = "input.txt"

  def lines: Vector[String] = Source.fromFile(path).getLines().toVector

  override def main(args: Array[String]): Unit = {
    var graph = readGraph
    graph.colors = Array.fill(graph.vertices)(White())
    graph = fillVertEdges(graph)
    maximiseBlack(graph)
    println(s"Can paint maximum $maxBlack vertices")
    println((1 to graph.vertices).map(i => s"$i -> ${bestSolution(i - 1)}").mkString("\n"))
  }

  def readGraph: Graph =
    Graph(lines.head.toInt, lines.tail.map(_.split(" ").map(_.toInt - 1)).map(arr => (arr(0), arr(1))))

  def fillVertEdges(graph: Graph): Graph = {
    val vertEdges: Seq[Seq[Int]] = (0 until graph.vertices)
      .map(
        number => graph
          .edges
          .filter(edge => edge._1 == number || edge._2 == number)
          .map(edge => if (edge._1 == number) edge._2 else edge._1)
      )
    graph.copy(vertEdges = vertEdges)
  }

  var maxBlack = 0
  var bestSolution: Seq[Color] = Seq.empty

  def maximiseBlack(graph: Graph, p: Int = 0, count: Int = 0): Unit = {
    if (checkColoring(graph, p - 1)) {
      if (count > maxBlack) {
        maxBlack = count
        bestSolution = graph.colors
      }
      if (p < graph.vertices) {
        val newColors = graph.colors.clone()
        newColors(p) = Black()
        maximiseBlack(graph.copy(colors = newColors), p + 1, count + 1)
        maximiseBlack(graph, p + 1, count)
      }
    }
  }

  def checkColoring(graph: Graph, changedVertice: Int): Boolean = if (changedVertice < 0) true
    else graph.colors(changedVertice) match {
      case Black() => !graph.vertEdges(changedVertice).exists(x => graph.colors(x) match {
        case Black() => true
        case _ => false
      })
      case White() => true
  }
}