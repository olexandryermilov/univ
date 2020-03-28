package com.yermilov.is.lab4

import scala.util.Random

case class Schedule(monday: Day, tuesday: Day, wednesday: Day, thursday: Day, friday: Day) {
  lazy val allDays: Seq[Day] = monday :: tuesday :: wednesday :: thursday :: friday :: Nil

  lazy val allLessons: Seq[Lesson] = allDays.flatMap(day => day.firstPair ++ day.secondPair ++ day.thirdPair)

  lazy val allCourses: Seq[Course] = allLessons.map(_.course)

  lazy val allGroupCourses: Seq[(Group, Seq[(Course, LessonType)])] =
    allLessons.groupBy(_.group).view.mapValues(lessons => lessons.map(lesson => lesson.course -> lesson.lessonType)).toSeq

  def fitnessScore: Int = allDays.map(_.conflicts).sum + conflict

  def conflict: Int = {
    allGroupCourses.map(groupWithCourses => {
      val group = groupWithCourses._1
      val courses = groupWithCourses._2
      val groupCoursesConflict = group.courses.toSeq.map(course => {
        val practicals = courses.count(scheduledCourse => scheduledCourse._1 == course && scheduledCourse._2 == Practical)
        val lectures = courses.count(scheduledCourse => scheduledCourse._1 == course && scheduledCourse._2 == Lecture)
        Math.abs(practicals - 1) + Math.abs(lectures - 1)
      }).sum
      groupCoursesConflict
    }).sum
  }

  override def toString: String =
    s"""
       |Monday: $monday
       |
       |Tuesday: $tuesday
       |
       |Wednesday: $wednesday
       |
       |Thursday: $thursday
       |
       |Friday: $friday
       |""".stripMargin
}

object Schedule {

  import Group._
  import Day._

  lazy val amountOfLessons = allGroups.flatMap(_.courses).size * 2

  def randomSchedule(amountOfLessons: Int = amountOfLessons): Schedule = {
    val lessons = getLessonsAmount(amountOfLessons)
    Schedule(
      randomDay(lessons(0)),
      randomDay(lessons(1)),
      randomDay(lessons(2)),
      randomDay(lessons(3)),
      randomDay(lessons(4)),
    )
  }

  private def getLessonsAmount(amountOfLessons: Int): Seq[Int] = {
    /*var remainingLessons = amountOfLessons
    val mondayLessons = Random.nextInt(remainingLessons / 2)
    remainingLessons = remainingLessons - mondayLessons
    val tuesdayLessons = Random.nextInt(remainingLessons / 2)
    remainingLessons = remainingLessons - tuesdayLessons
    val wednesdayLessons = Random.nextInt(remainingLessons / 2)
    remainingLessons = remainingLessons - wednesdayLessons
    val thursdayLessons = Random.nextInt(remainingLessons / 2)
    remainingLessons = remainingLessons - thursdayLessons
    val fridayLessons = remainingLessons*/
    Seq(20, 20, 15, 20, amountOfLessons - 20 - 20 - 15 - 20) //Seq(mondayLessons, tuesdayLessons, wednesdayLessons, thursdayLessons, fridayLessons)
  }

  def fromLessonsSeq(lessons: Seq[Lesson]): Schedule = {
    val dayLessonsAmount = getLessonsAmount(lessons.size)
    Schedule(
      Day.fromLessonsSeq(lessons.take(dayLessonsAmount(0))),
      Day.fromLessonsSeq(lessons.slice(dayLessonsAmount(0), dayLessonsAmount(0) + dayLessonsAmount(1))),
      Day.fromLessonsSeq(lessons.slice(dayLessonsAmount(0) + dayLessonsAmount(1), dayLessonsAmount(0) + dayLessonsAmount(1) + dayLessonsAmount(2))),
      Day.fromLessonsSeq(lessons.slice(dayLessonsAmount(0) + dayLessonsAmount(1) + dayLessonsAmount(2), dayLessonsAmount(0) + dayLessonsAmount(1) + dayLessonsAmount(2) + dayLessonsAmount(3))),
      Day.fromLessonsSeq(lessons.drop(dayLessonsAmount(0) + dayLessonsAmount(1) + dayLessonsAmount(2) + dayLessonsAmount(3)))
    )
  }
}
