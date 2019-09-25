import org.scalameter.api._

trait RangeBenchmark extends Bench.OfflineReport  {
  import Algorithm._
  val sizes: Gen[Int] = Gen.range("size")(100, 500, 100)

  val ranges: Gen[Range] = for {
    size <- sizes
  } yield 0 until size

  performance of "Range" in {
    measure method "dynamicSlow" in {
      using(ranges) in {
        range => dynamicSlow(range.toList)
      }
    }
  }
}

trait FastBenchmark extends Bench.OfflineReport  {
  import Algorithm._
  val sizes: Gen[Int] = Gen.range("size")(100, 500, 100)

  val ranges: Gen[Range] = for {
    size <- sizes
  } yield 0 until size

  performance of "Range" in {
    measure method "dynamicFast" in {
      using(ranges) in {
        range => dynamicFast(range.toList)
      }
    }
  }
}

object CollectionBenchmarks extends Bench.Group {
  performance of "running time" config(
    reports.resultDir -> "target/benchmarks/time"
    ) in {
    include(new RangeBenchmark {})
  }

  performance of "running time" config(
    reports.resultDir -> "target/benchmarks/time"
    ) in {
    include(new FastBenchmark {})
  }
}
