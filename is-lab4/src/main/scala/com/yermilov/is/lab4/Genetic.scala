package com.yermilov.is.lab4

import com.yermilov.is
import com.yermilov.is.{Course, Day, Group, Lecture, LectureRoom, Lesson, Room, Schedule, Teacher}

import scala.util.Random

case class Population(population: Seq[Schedule])

object MainApp {

  def validateData(): Unit = {
    Course.validate()
    Group.validate()
    Teacher.validate()
    Room.validate()
  }

  val mutationRate = 7
  val elit = 3
  val populationSize = 14
  val tournamentSelection = 7

  def geneticMagic: Schedule = {
    var currentPopulation: Population = Population((1 to populationSize).map(_ => Schedule.randomSchedule()))
    var index = 0
    val startTime = System.nanoTime()
    var lastTime = System.nanoTime()
    while (currentPopulation.population.head.fitnessScore > 0) {
      if (index % 100 == 0) {
        println(s"Population $index: ${currentPopulation.population.head.fitnessScore}; Time took: ${(System.nanoTime() - lastTime) / 1000000} ms")
        lastTime = System.nanoTime()
      }
      if (index % 10000 == 0) {
        println(currentPopulation.population.head)
      }
      currentPopulation = evolve(currentPopulation)
      index = index + 1
    }
    println((System.nanoTime() - startTime) / 1000000)
    currentPopulation.population.head
  }

  def evolve(population: Population): Population = {
    mutatePopulation(crossoverPopulation(population))
  }

  def mutatePopulation(population: Population): Population =
    Population(population.population.map(mutateSchedule).sortWith((x, y) => x.fitnessScore < y.fitnessScore))

  def mutateSchedule(schedule: Schedule): Schedule = {
    val tempScheduleLessons = Schedule.randomSchedule().allLessons
    val currScheduleLessons = schedule.allLessons
    val newSchedule = Schedule.fromLessonsSeq(currScheduleLessons.zipWithIndex.map(lessonWithIndex => {
      val currLesson = lessonWithIndex._1
      val tempLesson = tempScheduleLessons(lessonWithIndex._2)
      if (Random.nextInt(100) > mutationRate)
        currLesson.copy(
          room = if (Random.nextInt(100) > 50 && currLesson.lessonType == Lecture && currLesson.room.ttype != LectureRoom) Room.lectureRoom.randomElement else currLesson.room
        ) else tempLesson
    }))
    if (newSchedule.fitnessScore < schedule.fitnessScore) newSchedule else schedule
  }

  def crossoverPopulation(population: Population): Population = {
    val crossover: Population = Population(population.population.take(elit))
    val toAppend: Seq[Schedule] = (elit to populationSize).map(_ => {
      val schedule1 = selectTournament(population)
      val schedule2 = Schedule.randomSchedule()//selectTournament(population)
      if (Random.nextBoolean()) crossoverSchedule(schedule1, schedule2) else crossoverSchedule2(schedule1, schedule2)
    })
    Population(crossover.population ++ toAppend ++ Seq(Schedule.randomSchedule(), Schedule.randomSchedule()))
  }

  def crossoverSchedule(firstSchedule: Schedule, secondSchedule: Schedule): Schedule = {
    if (firstSchedule == secondSchedule) firstSchedule else {
      val newSchedule = Schedule(
        monday = if (Random.nextBoolean()) firstSchedule.monday else secondSchedule.monday,
        tuesday = if (Random.nextBoolean()) firstSchedule.tuesday else secondSchedule.tuesday,
        wednesday = if (Random.nextBoolean()) firstSchedule.wednesday else secondSchedule.wednesday,
        thursday = if (Random.nextBoolean()) firstSchedule.thursday else secondSchedule.thursday,
        friday = if (Random.nextBoolean()) firstSchedule.friday else secondSchedule.friday,
      )
      Seq(firstSchedule, newSchedule, secondSchedule).minBy(_.fitnessScore)
    }
  }

  def fixPair(pair: Seq[Lesson]): Seq[Lesson] = if (Random.nextInt(100) > 75) {
    val reps = pair.map(_.group.id).groupBy(identity).toSeq.map(c => c._1 -> c._2.size).find(_._2 > 1)
    if (reps.nonEmpty) {
      val gr = reps.get
      pair.map(course => if (Random.nextBoolean() && course.group.id == gr._1) Lesson.randomLesson() else course)
    } else pair
  } else pair

  def fixPairRoom(pair: Seq[Lesson]): Seq[Lesson] = if (Random.nextInt(100) > 75) {
    val reps = pair.map(_.room.number).groupBy(identity).toSeq.map(c => c._1 -> c._2.size).find(_._2 > 1)
    if (reps.nonEmpty) {
      val gr = reps.get
      pair.map(course => if (Random.nextBoolean() && course.room.number == gr._1){
        var room = Room.lectureRoom.randomElement
        while(room.number == gr._1)
          room = Room.lectureRoom.randomElement
        course.copy(room = room)
      } else course)
    } else pair
  } else pair

  def fixPairCourse(pair: Seq[Lesson]): Seq[Lesson] = if (Random.nextInt(100) > 75) {
    val reps = pair.filterNot(l => l.group.courses.contains(l.course))
    if (reps.nonEmpty) {
      pair.map(course => if (Random.nextBoolean() && reps.contains(course)){
        course.copy(course.group.courses.toSeq.randomElement)
      } else course)
    } else pair
  } else pair

  def fixPairTeacher(pair: Seq[Lesson]): Seq[Lesson] = if (Random.nextInt(100) > 75) {
    val reps = pair.filterNot(l => l.teacher.possibleCourses.contains(l.course))
    if (reps.nonEmpty) {
      pair.map(course => if (Random.nextBoolean() && reps.contains(course)){
        course.copy(teacher = Teacher.allTeachers.find(_.possibleCourses.contains(course.course)).get)
      } else course)
    } else pair
  } else pair

  def crossoverSchedule2(firstSchedule: Schedule, secondSchedule: Schedule): Schedule = {
    if (firstSchedule == secondSchedule) firstSchedule else {
      val newSchedule = Schedule(
        monday = crossoverDay(firstSchedule.monday, secondSchedule.monday),
        tuesday = crossoverDay(firstSchedule.tuesday, secondSchedule.tuesday),
        wednesday = crossoverDay(firstSchedule.wednesday, secondSchedule.wednesday),
        thursday = crossoverDay(firstSchedule.thursday, secondSchedule.thursday),
        friday = crossoverDay(firstSchedule.friday, secondSchedule.friday),
      )
      Seq(firstSchedule, newSchedule, secondSchedule).minBy(_.fitnessScore)
    }
  }

  def crossoverDay(firstDayUnm: Day, secondDayUnm: Day): Day = {
    val firstDay = if(Random.nextBoolean())mutateDay(firstDayUnm) else firstDayUnm
    val secondDay =if(Random.nextBoolean()) mutateDay(secondDayUnm) else secondDayUnm
    is.Day(
      firstPair =  Seq(fixPairTeacher(fixPairCourse(fixPairRoom(fixPair(firstDay.firstPair)))),  fixPairTeacher(fixPairCourse(fixPairRoom(fixPair(secondDay.firstPair))))).minBy(_.map(_.conflict).sum), //if (Random.nextBoolean()) firstDay.firstPair else secondDay.firstPair,
      secondPair = Seq(fixPairTeacher(fixPairCourse(fixPairRoom(fixPair(firstDay.secondPair)))), fixPairTeacher(fixPairCourse(fixPairRoom(fixPair(secondDay.secondPair))))).minBy(_.map(_.conflict).sum), //if (Random.nextBoolean()) firstDay.secondPair else secondDay.secondPair,
      thirdPair =  Seq(fixPairTeacher(fixPairCourse(fixPairRoom(fixPair(firstDay.thirdPair)))),  fixPairTeacher(fixPairCourse(fixPairRoom(fixPair(secondDay.thirdPair))))).minBy(_.map(_.conflict).sum), //if (Random.nextBoolean()) firstDay.thirdPair else secondDay.thirdPair,
    )
  }

  def mutateDay(day: Day): Day = Day.fromLessonsSeq(Random.shuffle(day.allLessons))

  def selectTournament(population: Population): Schedule =
    (1 to tournamentSelection).map(_ => population.population.randomElement).minBy(_.fitnessScore)

  def main(args: Array[String]): Unit = {
    validateData()
    println(geneticMagic)
  }
}