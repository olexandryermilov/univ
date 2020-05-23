package com.yermilov.is.lab5

import com.yermilov.is._

import scala.collection.mutable
import scala.util.{Failure, Success, Try}

object ConstraintProblem {
  type RemainingCourses = mutable.Set[(Course, LessonType)]
  type RemainingLessons = Set[Lesson]

  def validateData(): Unit = {
    Course.validate()
    Group.validate()
    Teacher.validate()
    Room.validate()
  }

  def generateAllLessons(remainingCourses: RemainingLessons, samePairLessons: Seq[Lesson]): Seq[(Lesson, RemainingLessons)] = {
    val availableTeachers: mutable.Set[Teacher] = mutable.Set(Teacher.allTeachers: _*)
    val availableRooms: mutable.Set[Room] = mutable.Set(Room.allRooms: _*)
    val availableGroups: mutable.Set[Group] = mutable.Set(Group.allGroups: _*)

    samePairLessons.foreach(lesson => {
      availableTeachers.remove(lesson.teacher)
      availableRooms.remove(lesson.room)
      availableGroups.remove(lesson.group)
    })

    availableRooms.flatMap(availableRoom => {
      availableGroups.flatMap(availableGroup => {
        availableTeachers.flatMap(availableTeacher => {
          availableTeacher.possibleCourses.filter(teachersCourse => {
            ???
          }).flatMap(_ => {
???
          })
        })
      })
    }).toSeq
  }

  def generateAllLessons1(remainingCourses: RemainingCourses, samePairLessons: Seq[Lesson]): Seq[(Lesson, RemainingCourses)] = {
    val availableTeachers: mutable.Set[Teacher] = mutable.Set(Teacher.allTeachers: _*)
    val availableRooms: mutable.Set[Room] = mutable.Set(Room.allRooms: _*)
    val availableGroups: mutable.Set[Group] = mutable.Set(Group.allGroups: _*)

    samePairLessons.foreach(lesson => {
      availableTeachers.remove(lesson.teacher)
      availableRooms.remove(lesson.room)
      availableGroups.remove(lesson.group)
    })

    availableRooms.flatMap(availableRoom => {
      availableGroups.flatMap(availableGroup => {
        availableTeachers.flatMap(availableTeacher => {
          availableTeacher.possibleCourses.filter(teachersCourse => {
            val courseWithLType = remainingCourses.find(_._1 == teachersCourse)
            courseWithLType match {
              case Some(value) =>
                availableGroup.courses.contains(value._1)
              case None => false
            }
          }).flatMap(teachersCourse => {
            remainingCourses.filter(c=>c._1 == teachersCourse && c._2.matchRoomType(availableRoom.ttype)).map { courseWithLessonType =>
              val courses = remainingCourses.clone()
              courses.remove(courseWithLessonType)
              val (course, lType) = courseWithLessonType
              Lesson(course, lType, availableGroup, availableTeacher, availableRoom
              ) -> courses
            }
          })
        })
      })
    }).toSeq
  }

  /*
  val availableTeachers: mutable.Set[Teacher] = mutable.Set(Teacher.allTeachers: _*)
  val availableRooms: mutable.Set[Room] = mutable.Set(Room.allRooms: _*)
  val availableGroups: mutable.Set[Group] = mutable.Set(Group.allGroups: _*)
  val availableCourses: RemainingCourses = remainingCourses.clone()

  samePairLessons.foreach(lesson => {
    availableTeachers.remove(lesson.teacher)
    availableRooms.remove(lesson.room)
    availableGroups.remove(lesson.group)
  })

  var iteration = 0
  var maybeLesson: Option[Lesson] = None

  while (maybeLesson.isEmpty || maybeLesson.get.conflict > 0) {
    iteration += 1
    if (iteration > 100000) {
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
  lesson -> availableCourses
}*/

  def generateScheduleRec(currentLessonNum: Int, samePairLessons: Seq[Lesson], availableCourses: RemainingCourses, agg: Seq[Lesson], depth: Int = 0): Option[Schedule] = {
    if (availableCourses.isEmpty) {
      return Some(Schedule.fromLessonsSeq(agg))
    }
    val generateLessonResult = generateAllLessons1(availableCourses, samePairLessons)
    if(System.nanoTime()%100000 == 0) println(s"$depth -> ${availableCourses.size} -> ${generateLessonResult.size}")
    if(samePairLessons.isEmpty && generateLessonResult.size < availableCourses.size) return None
    for (lessonResult <- generateLessonResult) {
      val (lesson, remainingCourses) = lessonResult
      val recDesc = if (currentLessonNum < 3)
        generateScheduleRec(currentLessonNum + 1, samePairLessons ++ Seq(lesson), remainingCourses.clone(), agg ++ Seq(lesson), depth + 1)
      else
        generateScheduleRec(0, Seq.empty, remainingCourses.clone(), agg ++ Seq(lesson), depth + 1)
      if (recDesc.nonEmpty) return recDesc
    }
    None
  }

  def generateSchedule(lessonAmount: Int = 60): Schedule = {
    val start = System.nanoTime()
    val remainingCourses: RemainingCourses = mutable.Set(Course.allCourses.flatMap(course => Seq(course -> Practical, course -> Lecture)): _*)
    println(generateAllLessons1(remainingCourses, Seq.empty).size)
    val result = generateScheduleRec(0, Seq.empty, remainingCourses, Seq.empty).get
    println((System.nanoTime() - start) / 1000000000)
    result
  }

  def main(args: Array[String]): Unit = {
    validateData()
    val schedule = generateSchedule()
    if (schedule.conflict > 0) println("!#!@#!@#!@#!@#")
    println(schedule)
    println("it's all folks")
  }
}
