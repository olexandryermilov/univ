package com.yermilov.is.lab4

import scala.util.Random

case class DayWithPairs(firstPair: Seq[Lesson], secondPair: Seq[Lesson], thirdPair: Seq[Lesson]) {

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

object DayWithPairs {
  def randomDayWithPairs(lessonsRemaining: Int): DayWithPairs = {
    val lessonsAmount = getLessonsAmount(lessonsRemaining)
    DayWithPairs(
      randomPair(lessonsAmount(0)),
      randomPair(lessonsAmount(1)),
      randomPair(lessonsAmount(2)),
    )
  }

  private def getLessonsAmount(amountOfLessons: Int): Seq[Int] = {
    /* var remainingLessons = amountOfLessons
     val firstPairLessons = nextIntWithoutErrors(remainingLessons / 3)
     remainingLessons = remainingLessons - firstPairLessons
     val secondPairLessons = nextIntWithoutErrors(remainingLessons / 2)
     remainingLessons = remainingLessons - secondPairLessons
     val thirdPairLessons = remainingLessons*/
    Seq(amountOfLessons / 3, amountOfLessons / 3, amountOfLessons - 2 * (amountOfLessons / 3)) //Seq(firstPairLessons, secondPairLessons, thirdPairLessons)
  }

  @scala.annotation.tailrec
  private def nextIntWithoutErrors(bound: Int): Int = if (bound == 0) nextIntWithoutErrors(1) else Random.nextInt(bound)

  def randomPair(lessons: Int): Seq[Lesson] = (1 to lessons).map(_ => Lesson.randomLesson())

  def fromLessonsSeq(lessons: Seq[Lesson]): DayWithPairs = {
    val lessonsAmount = getLessonsAmount(lessons.size)
    DayWithPairs(
      lessons.take(lessonsAmount(0)),
      lessons.slice(lessonsAmount(0), lessonsAmount(0) + lessonsAmount(1)),
      lessons.drop(lessonsAmount(0) + lessonsAmount(1)),
    )
  }

}

