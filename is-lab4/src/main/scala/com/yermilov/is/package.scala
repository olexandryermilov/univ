package com.yermilov

import com.yermilov.is.Course.allCourses

import scala.util.Random

package object is {
  implicit def booleanToInt(bool: Boolean): Int = if (bool) 1 else 0

  implicit class SeqExtensions[T](val seq: Seq[T]) extends AnyVal {
    def repetitions: Int = seq.groupBy(identity).values.filter(_.size > 1).map(_.size - 1).sum
    def randomElement: T = seq(Random.nextInt(seq.size))
  }

  //Course id is [0; 47]
  implicit def intToCourse(id: Int): Course = allCourses(id)
}