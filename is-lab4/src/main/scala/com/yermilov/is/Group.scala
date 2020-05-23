package com.yermilov.is

case class Group(id: Int, courses: Set[Course])

object Group {

  val allGroups: Seq[Group] = Seq(
    Group(0, Set(0, 1, 3, 8, 12)),
    Group(1, Set(2, 21, 23)),
    Group(2, Set(4, 5, 6, 7, 10, 11)),
    Group(3, Set(13, 14, 15, 16)),
    Group(4, Set(20, 22, 24, 25)),
    Group(5, Set(9, 28, 29)),
    Group(6, Set(17, 18, 19)),
    //Group(6, Set(33, 34, 35, 36, 37, 38, 39)),
    //Group(7, Set(44, 46, 47))
  )

  private def noRepeats(): Unit =
    if (allGroups.flatMap(_.courses).map(c => c.id).repetitions > 0)
      throw new RuntimeException(
        s"One course is taught in two groups: ${allGroups.flatMap(_.courses).map(c => c.id).groupBy(identity).filter(_._2.size > 1)}"
      )

  private def courseLimitations(): Unit =
    if (allGroups.exists(_.courses.size > 7)) throw new RuntimeException("Can't teach more than 7 courses")

  def validate(): Unit = {
    courseLimitations()
    noRepeats()
  }

}
