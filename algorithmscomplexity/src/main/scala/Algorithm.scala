import scala.collection.mutable
import scala.util.Random

object Algorithm {

  final val INF = Int.MaxValue

  def max(a: Int, b: Int): Int = if (a > b) a else b

  def upperBound(array: mutable.ArraySeq[Int], n: Int): Int = {
    var l = 0
    var r = array.size - 1
    while (l < r) {
      val m: Int = (l + r) / 2
      if (array(m) > n) {
        r = m
      }
      else l = m + 1
    }
    if (array(l) > n) l else if (array(r) > n) r else -1
  }

  def dynamicFast(numbers: List[Int]): Int = {
    val n = numbers.size
    val dp = mutable.ArraySeq.fill(numbers.size)(INF)
    var res = 0
    dp(0) = Int.MinValue
    for (i <- numbers.indices) {
      val j: Int = upperBound(dp, numbers(i));
      if (dp(j - 1) < numbers(i) && numbers(i) < dp(j)) {
        dp(j) = numbers(i)
        res = j
      }
    }
    res
  }

  def dynamicSlow(numbers: List[Int]): Int = {
    val n = numbers.size
    var dp = mutable.ArraySeq.fill(n)(1)
    for (i <- numbers.indices) {
      for (j <- 0 until i) {
        if (numbers(i) > numbers(j)) dp(i) = max(dp(j) + 1, dp(i))
      }
    }
    dp.max
  }

  def generateList(size: Int): List[Int] = {
    val randomGenerator = new Random(System.nanoTime())
    (for (i <- 1 to size) yield randomGenerator.nextInt()).toList
  }

  def benchmark(numbers: List[Int], func: List[Int] => Int, funcName: String): Long = {
    val startTime = System.currentTimeMillis()
    func(numbers)
    val endTime = System.currentTimeMillis()
    println(
      s"""|Function: $funcName
         |Time: ${endTime - startTime} ms""".stripMargin)
    endTime - startTime
  }

  def main(args: Array[String]): Unit = {
    val sizes: List[Int] = (1 to 25).map(_ * 75).toList

    val list = generateList(10000)
    println("list generated")
    val slow = sizes.map(size => {
      val res = size -> benchmark(list.take(size), dynamicSlow, "dynamic N^2")
      println(s"slow for $size")
      res
    })
    val fast = sizes.map(size => {
      val res = size -> benchmark(list.take(size), dynamicFast, "dynamic NlogN")
      println(s"fast for $size")
      res
    })
    println(slow.map(_._1).mkString("\n"))
    println
    println(slow.map(_._2).mkString("\n"))
    println
    println(fast.map(_._2).mkString("\n"))
  }
}
