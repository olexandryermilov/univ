package com.yermilov.is.lab4

import scala.util.Random

case class Day(firstPair: Seq[Lesson], secondPair: Seq[Lesson], thirdPair: Seq[Lesson]) {

  lazy val random = Random

  def conflicts: Int = pairConflict(firstPair) + pairConflict(secondPair) + pairConflict(thirdPair)

  def pairConflict(pair: Seq[Lesson]): Int = {
    val coursesConflicts = pair.map(_.course).repetitions
    val lessonConflicts = pair.map(_.conflict).sum
    val groupConflicts = pair.map(_.group).repetitions
    val teacherConflicts = pair.map(_.teacher).repetitions
    val roomConflicts = pair.map(_.room).repetitions
    coursesConflicts + lessonConflicts + groupConflicts + teacherConflicts + roomConflicts
  }

  override def toString: String =
    s"""
       |Pair 1: ${firstPair.mkString("\n")}
       |Pair 2: ${secondPair.mkString("\n")}
       |Pair 3: ${thirdPair.mkString("\n")}
       |""".stripMargin

}

object Day {
  def randomDay(lessonsRemaining: Int): Day = {
    val lessonsAmount = getLessonsAmount(lessonsRemaining)
    Day(
      randomPair(lessonsAmount(0)),
      randomPair(lessonsAmount(1)),
      randomPair(lessonsAmount(2)),
    )
  }

  private def getLessonsAmount(amountOfLessons: Int): Seq[Int] = {
    var remainingLessons = amountOfLessons
    val firstPairLessons = Random.nextInt(remainingLessons / 3)
    remainingLessons = remainingLessons - firstPairLessons
    val secondPairLessons = Random.nextInt(remainingLessons / 2)
    remainingLessons = remainingLessons - secondPairLessons
    val thirdPairLessons = remainingLessons
    Seq(firstPairLessons, secondPairLessons, thirdPairLessons)
  }

  def randomPair(lessons: Int): Seq[Lesson] = (1 to lessons).map(_ => Lesson.randomLesson()).toList

}
