package com.yermilov.is.lab4

case class Room(number: Int, ttype: RoomType)

sealed trait RoomType
case object LectureRoom extends RoomType
case object PracticalRoom extends RoomType

object Room {

  val allRooms: Seq[Room] = Seq(
    Room(0, LectureRoom),
    Room(1, PracticalRoom),
    Room(2, LectureRoom),
    Room(3, PracticalRoom),
    Room(4, LectureRoom),
    Room(5, PracticalRoom)
  )

  private def uniqueIds(): Unit = if(allRooms.map(_.number).repetitions > 0) throw new RuntimeException("Room numbers are not unique")

  def validate(): Unit = uniqueIds()
}
