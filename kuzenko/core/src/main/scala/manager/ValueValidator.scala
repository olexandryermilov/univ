package manager

import domain.Column

import scala.util.{Success, Try}
import domain.Type._

class ValueValidator {

  def validateValues(columnsWithValue: Seq[(Column, String)]): Try[Unit] = {
    val res = columnsWithValue.map(validateValue).filter(_.isFailure)
    if (res.nonEmpty) res.head else Success()
  }

  def validateValue(columnWithValue: (Column, String)): Try[Unit] = {
    val column = columnWithValue._1
    val value = columnWithValue._2
    val validator = column.columnType match {
      case Integer => IntegerValidator()
      case String => StringValidator()
      case Real => RealValidator()
      case Char => CharValidator()
      case CharInv => CharInvValidator()
    }
    validator.validate(value)
  }
}

sealed trait Validator {
  def validate(value: String): Try[Unit]
}

//Integer, Real, Char, String, CharInv

case class StringValidator() extends Validator {
  override def validate(value: String): Try[Unit] = Success()
}

case class IntegerValidator() extends Validator {
  override def validate(value: String): Try[Unit] = Try {
    val res = value.toInt
  }.orElse(throw WrongType(Integer.toString, value))
}

case class RealValidator() extends Validator {
  override def validate(value: String): Try[Unit] = Try {
    val res = value.toDouble
  }.orElse(throw WrongType(Real.toString, value))
}

case class CharValidator() extends Validator {
  override def validate(value: String): Try[Unit] = Try {
    if (value.length == 1) Success() else throw WrongType(Char.toString, value)
  }
}

case class CharInvValidator() extends Validator {
  override def validate(value: String): Try[Unit] = Try {
    if (value.length == 2 && value(1) > value(0)) Success() else throw WrongType(CharInv.toString, value)
  }
}

case class WrongType(typeToBe: String, value: String)
  extends RuntimeException(s"Wrong type of $value: should be $typeToBe")