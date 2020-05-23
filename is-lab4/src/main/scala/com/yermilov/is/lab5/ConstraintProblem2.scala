package com.yermilov.is.lab5

import com.yermilov.is._

import scala.collection.mutable
import scala.util.{Failure, Success, Try}

object ConstraintProblem2 {
  type RemainingCourses = mutable.Set[(Course, LessonType)]

  def validateData(): Unit = {
    Course.validate()
    Group.validate()
    Teacher.validate()
    Room.validate()
  }

  def mrv(): Schedule = {
    val start = System.nanoTime()

    def generatePair0(remainingCourses: RemainingCourses, lessonAmount: Int = 4): (Seq[Lesson], RemainingCourses) = {
      val availableTeachers: mutable.Set[Teacher] = mutable.Set(Teacher.allTeachers: _*)
      val availableRooms: mutable.Set[Room] = mutable.Set(Room.allRooms: _*)
      val availableGroups: mutable.Set[Group] = mutable.Set(Group.allGroups: _*)
      val availableCourses: RemainingCourses = remainingCourses.clone()

      var iteration = 0
      (1 to lessonAmount).map(_ => {
        var maybeLesson: Option[Lesson] = None

        while (maybeLesson.isEmpty || maybeLesson.get.conflict > 0) {
          iteration += 1
          if(iteration > 100000) {
            //println("iteration > 1000")
            throw new RuntimeException("Iteration > 100000")
          }
          val (course, lessonType): (Course, LessonType) = availableCourses.toSeq.randomElement
          val room = lessonType match {
            case Lecture => availableRooms.filter(_.ttype == LectureRoom).toSeq.randomElement
            case Practical => availableRooms.filter(_.ttype == PracticalRoom).toSeq.randomElement
          }
          val maybeTeacher = availableTeachers.find(_.possibleCourses.contains(course))
          val maybeGroup = availableGroups.find(_.courses.contains(course))
          maybeLesson = maybeGroup.flatMap(group => maybeTeacher.map(teacher =>
            Lesson(
              course,
              lessonType,
              group,
              teacher,
              room
            )))
        }
        val lesson = maybeLesson.get
        availableRooms.remove(lesson.room)
        availableGroups.remove(lesson.group)
        availableTeachers.remove(lesson.teacher)
        availableCourses.remove(lesson.course -> lesson.lessonType)
        lesson
      }) -> availableCourses
    }

    def generatePair(courses: ConstraintProblem.RemainingCourses): (Seq[Lesson], RemainingCourses) = {
      Try(generatePair0(courses)) match {
        case Failure(_) => {
          if((System.nanoTime() - start)/1000000000 > 20) throw new RuntimeException
          generatePair(courses)
        }
        case Success(value) => value
      }
    }

    def generateDay(remainingCourses: RemainingCourses, lessonAmount: Int = 12): (Day, RemainingCourses) = {
      var newRemainingCourses = remainingCourses
      var result = generatePair(newRemainingCourses)
      val firstPair = result._1
      newRemainingCourses = result._2
      println(firstPair)
      println(newRemainingCourses.size)

      result = generatePair(newRemainingCourses)
      val secondPair = result._1
      newRemainingCourses = result._2
      println(secondPair)
      println(newRemainingCourses.size)

      result = generatePair(newRemainingCourses)
      val thirdPair = result._1
      newRemainingCourses = result._2
      println(thirdPair)
      println(newRemainingCourses.size)

      Day(firstPair, secondPair, thirdPair) -> newRemainingCourses
    }

    var remainingCourses: RemainingCourses = mutable.Set(Course.allCourses.flatMap(course => Seq(course -> Practical, course -> Lecture)): _*)
    var result = generateDay(remainingCourses)
    val monday = result._1
    remainingCourses = result._2
    println(monday)

    result = generateDay(remainingCourses)
    val tuesday = result._1
    remainingCourses = result._2
    println(tuesday)

    result = generateDay(remainingCourses)
    val wednesday = result._1
    remainingCourses = result._2
    println(wednesday)

    result = generateDay(remainingCourses)
    val thursday = result._1
    remainingCourses = result._2
    println(thursday)

    result = generateDay(remainingCourses)
    val friday = result._1
    remainingCourses = result._2
    println(friday)

    Schedule(monday, tuesday, wednesday, thursday, friday)
  }

  def mrv0(): Schedule = Try(mrv()) match {
    case Failure(_) => mrv0()
    case Success(value) => value
  }


  def main(args: Array[String]): Unit = {
    validateData()
    val schedule = mrv0()
    if(schedule.conflict > 0) println("!#!@#!@#!@#!@#")
    println(schedule)
    println("it's all folks")
  }
}
