package com.yermilov.is

import com.yermilov.is.lab4.Course.allCourses

import scala.util.Random

package object lab4 {
  implicit def booleanToInt(bool: Boolean): Int = if (bool) 1 else 0

  implicit class SeqExtensions[T](val seq: Seq[T]) extends AnyVal {
    def repetitions: Int = seq.groupBy(identity).toSeq.map(_._2.size - 1).sum
    def randomElement: T = seq(Random.nextInt(seq.size))

  }

  //Course id is [0; 47]
  implicit def intToCourse(id: Int): Course = allCourses(id)
}
