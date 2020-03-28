package com.yermilov.is.lab4

import scala.util.Random

class Genetic {

}

case class Population(population: Seq[Schedule])

object MainApp {

  def validateData(): Unit = {
    Course.validate()
    Group.validate()
    Teacher.validate()
    Room.validate()
  }

  val mutationRate = 10
  val elit = 2
  val populationSize = 10
  val tournamentSelection = 8

  def geneticMagic: Schedule = {
    var currentPopulation: Population = Population((1 to populationSize).map(_ => Schedule.randomSchedule()))
    var index = 0
    val startTime = System.nanoTime()
    var lastTime = System.nanoTime()
    while (currentPopulation.population.minBy(_.fitnessScore).fitnessScore > 0) {

      currentPopulation = evolve(currentPopulation)
      if (index % 100 == 0) {
        println(s"Population $index: ${currentPopulation.population.minBy(_.fitnessScore).fitnessScore}; Time took: ${(System.nanoTime() - lastTime)/1000000} ms")
        lastTime = System.nanoTime()
      }
      index = index + 1
    }
    println((System.nanoTime() - startTime)/ 1000000)
    currentPopulation.population.minBy(_.fitnessScore)
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
      if (Random.nextInt(100) > mutationRate) currLesson else tempLesson
    }))
    if (newSchedule.fitnessScore < schedule.fitnessScore) newSchedule else schedule
  }

  def crossoverPopulation(population: Population): Population = {
    val crossover: Population = Population(population.population.take(elit))
    val toAppend: Seq[Schedule] = (elit to populationSize).map(_ => {
      val schedule1 = selectTournament(population)(0)
      val schedule2 = selectTournament(population)(0)
      crossoverSchedule(Seq(schedule1, schedule2))
    })
    Population(crossover.population ++ toAppend)
  }

  def crossoverSchedule(schedules: Seq[Schedule]): Schedule = {
    val newSchedule = Schedule(
      monday = Seq(schedules(0).monday, schedules(1).monday).randomElement,
      tuesday = Seq(schedules(0).tuesday, schedules(1).tuesday).randomElement,
      wednesday = Seq(schedules(0).wednesday, schedules(1).wednesday).randomElement,
      thursday = Seq(schedules(0).thursday, schedules(1).thursday).randomElement,
      friday = Seq(schedules(0).friday, schedules(1).friday).randomElement,
    )
    (schedules ++ Seq(newSchedule)).minBy(_.fitnessScore)
  }

  def selectTournament(population: Population): Seq[Schedule] =
    (1 to tournamentSelection).map(_ => population.population.randomElement).sortWith((x,y)=>x.fitnessScore < y.fitnessScore)

  def main(args: Array[String]): Unit = {
    validateData()
    println(geneticMagic)
  }
}