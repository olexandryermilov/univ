package com.yermilov.is.lab4

class Genetic {

}

object MainApp {

  def validateData(): Unit ={
    Course.validate()
    Group.validate()
    Teacher.validate()
    Room.validate()
  }

  def main(args: Array[String]): Unit = {
    validateData()

    val schedule = Schedule.randomSchedule()

    println(schedule)
  }
}