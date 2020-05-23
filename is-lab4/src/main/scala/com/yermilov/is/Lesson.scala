package com.yermilov.is

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
  import Group._
  import Room._
  import Teacher._
  def randomLesson(): Lesson = {
    val teacher = allTeachers.randomElement
    val teacherCourse = teacher.possibleCourses.toSeq
    val group = allGroups.randomElement
    Lesson(
      Try(teacherCourse.filter(c => group.courses.contains(c)).randomElement).getOrElse(teacherCourse.randomElement),
      if(Random.nextBoolean) Lecture else Practical,
      group,
      teacher,
      allRooms.randomElement
    )
  }
}

sealed trait LessonType {
  def reverse: LessonType
  def matchRoomType(roomType: RoomType): Boolean
}
case object Lecture extends LessonType {
  override def reverse: LessonType = Practical

  override def matchRoomType(roomType: RoomType): Boolean = roomType == LectureRoom
}
case object Practical extends LessonType {
  override def reverse: LessonType = Lecture

  override def matchRoomType(roomType: RoomType): Boolean = roomType == PracticalRoom
}
