package com.yermilov.is.lab4

import scala.util.{Random, Try}

case class Lesson(course: Course, lessonType: LessonType, group: Group, teacher: Teacher, room: Room) {
  def conflict: Int = roomConflict + teacherConflict + groupConflict

  def roomConflict: Int = lessonType match {
    case Lecture => room.ttype match {
      case LectureRoom => 0
      case PracticalRoom => 1
    }
    case Practical => 0
  }

  def teacherConflict: Int = !teacher.possibleCourses.contains(course)

  def groupConflict: Int = !group.courses.contains(course)

  override def toString: String =
    s"Lesson(Course(${course.id} ${course.name}), Teacher(${teacher.id}, ${teacher.surName}, ${teacher.possibleCourses.contains(course)}), $lessonType, Room(${room.number}, ${room.ttype}), Group(${group.id}, ${group.courses.contains(course)}))"
}

object Lesson {
  import Course._
  import Group._
  import Teacher._
  import Room._
  def randomLesson(): Lesson = {
    val teacher = allTeachers.randomElement
    val group = allGroups.randomElement
    Lesson(
      Try(teacher.possibleCourses.filter(c => group.courses.contains(c)).toSeq.randomElement).getOrElse(teacher.possibleCourses.toSeq.randomElement),
      if(Random.nextBoolean) Lecture else Practical,
      group,
      teacher,
      allRooms.randomElement
    )
  }
}

sealed trait LessonType
case object Lecture extends LessonType
case object Practical extends LessonType
