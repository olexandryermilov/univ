package com.yermilov.is.lab4

case class Course(id: Int, name: String, frequency: Int = 1)

object Course {
  val allCourses: Seq[Course] = Seq(
    Course(0, "How to learn"),
    Course(1, "Matan"),
    Course(2, "Andan"),
    Course(3, "Intellectual Systems"),
    Course(4, "Programming"),
    Course(5, "OOP"),
    Course(6, "FP"),
    Course(7, "Philosophy"),
    Course(8, "AI"),
    Course(9, "NLP"),
    Course(10, "CV"),
    Course(11, "ML"),
    Course(12, "Numerical methods"),
    Course(13, "Algorithms"),
    Course(14, "Web programming"),
    Course(15, "OS"),
    Course(16, "Psychology"),
    Course(17, "Statistics"),
    Course(18, "Computational geometry"),
    Course(19, "Telecom"),
    Course(20, "Modern technologies"),
    Course(21, "Hard maths"),
    Course(22, "Genetic algorithms"),
    Course(23, "Parallel computations"),
    Course(24, "Cloud computations"),
    Course(25, "Parallel cloud computations"),
    Course(26, "Cloud parallel computations"),
    Course(27, "Presentation skills"),
    Course(28, "Algebra"),
    Course(29, "Differential equalities"),
    Course(30, "Differential inequalities"),
    Course(31, "Game development"),
    Course(32, "Design"),
    Course(33, "Web design"),
    Course(34, "Databases"),
    Course(35, "Refactoring"),
    Course(36, "Spring and Hybernate"),
    Course(37, "Modern web frameworks"),
    Course(38, "Cryptology"),
    Course(39, "Cryptography"),
    Course(40, "Bitcoin applications"),
    Course(41, "How world works"),
    Course(42, "Economy"),
    Course(43, "Modern economy"),
    Course(44, "Ecology"),
    Course(45, "Version control systems"),
    Course(46, "How systems communicate"),
    Course(47, "Cybernetics applications")
  )

  private def validateIds(): Unit = if (allCourses.map(_.id).repetitions != 0) throw new RuntimeException("There are courses with not unique id")

  def validate(): Unit = validateIds()

}
