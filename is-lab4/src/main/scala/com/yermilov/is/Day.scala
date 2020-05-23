package com.yermilov.is

import scala.util.Random

case class Day(firstPair: Seq[Lesson], secondPair: Seq[Lesson], thirdPair: Seq[Lesson]) {

  val allLessons = firstPair ++ secondPair ++ thirdPair

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
    //val lessonsAmount = getLessonsAmount(lessonsRemaining)
    Day(
      randomPair(lessonsRemaining / 3),
      randomPair(lessonsRemaining / 3 + 1),
      randomPair(lessonsRemaining - 2*(lessonsRemaining/3) - 1)
    )
  }

  private def getLessonsAmount(amountOfLessons: Int): Seq[Int] = {
   /* var remainingLessons = amountOfLessons
    val firstPairLessons = nextIntWithoutErrors(remainingLessons / 3)
    remainingLessons = remainingLessons - firstPairLessons
    val secondPairLessons = nextIntWithoutErrors(remainingLessons / 2)
    remainingLessons = remainingLessons - secondPairLessons
    val thirdPairLessons = remainingLessons*/
    Seq(4, 4, 4) //Seq(firstPairLessons, secondPairLessons, thirdPairLessons)
  }

  @scala.annotation.tailrec
  private def nextIntWithoutErrors(bound: Int): Int = if (bound <= 0) nextIntWithoutErrors(1) else Random.nextInt(bound)

  def randomPair(lessons: Int): Seq[Lesson] = (1 to lessons).map(_ => Lesson.randomLesson())

  def fromLessonsSeq(lessons: Seq[Lesson]): Day = {
    val lessonsAmount = getLessonsAmount(lessons.size)
    val lessons1 = lessons.splitAt(lessonsAmount(0))
    val lessons2 = lessons1._2.splitAt(lessonsAmount(1))
    Day(
      lessons1._1,
      lessons2._1,
      lessons2._2,
    )
  }

}
