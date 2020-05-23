package com.yermilov.is

case class Teacher(id: Int, surName: String, possibleCourses: Set[Course])

object Teacher {

  val allTeachers = Seq(
    Teacher(0, "A", Set(1, 2, 3)),
    Teacher(1, "B", Set(0, 2, 3)),
    Teacher(2, "C", Set(4)),
    Teacher(3, "D", Set(5, 6, 7)),
    Teacher(4, "E", Set(5, 7, 8)),
    Teacher(5, "F", Set(9, 10, 11, 12, 13)),
    Teacher(6, "G", Set(12, 14)),
    Teacher(7, "H", Set(13, 24)),
    Teacher(8, "Q", Set(15, 16)),
    Teacher(9, "W", Set(15, 17)),
    Teacher(10, "R", Set(18, 19)),
    Teacher(11, "T", Set(20, 21, 22)),
    Teacher(12, "Y", Set(23, 27, 29)),
    Teacher(13, "U", Set(25, 26)),
    //Teacher(13, "U", Set(25, 26, 30)),
    //Teacher(13, "U", Set(24, 30, 38)),
    Teacher(14, "I", Set(28, 24)),
    //Teacher(14, "I", Set(25, 31)),
    //Teacher(15, "O", Set(26, 28, 32, 33, 34, 35)),
    //Teacher(16, "P", Set(36)),
    //Teacher(17, "S", Set(37, 38, 39, 40)),
    //Teacher(18, "J", Set(0, 41)),
    //Teacher(19, "K", Set(42)),
    //Teacher(20, "J", Set(43, 44))
  )

  private def validateAllCoursesAreTaught(): Unit = (0 until Course.allCourses.size).foreach(id =>
    if (!allTeachers.flatMap(_.possibleCourses.toSeq).exists(_.id == id))
      throw new RuntimeException(s"Course $id isn't taught by any teacher"))

  private def validateIdsAreUnique(): Unit = if (allTeachers.map(_.id).repetitions > 0)
    throw new RuntimeException("Ids are not unique")


  def validate(): Unit = {
    validateIdsAreUnique()
    validateAllCoursesAreTaught()
  }

}
