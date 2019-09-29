package domain

object Type extends Enumeration {
  type Type = Value
  val Integer, Real, Char, String, CharInv = Value

  def toName(ttype: Type): String = ttype match {
    case Integer => "Integer"
    case Real => "Real"
    case Char => "Char"
    case String => "String"
    case CharInv => "CharInv"
  }

  def toType(name: String): Type = name match {
    case "Integer" => Integer
    case "Real" => Real
    case "Char" => Char
    case "String " => String
    case "CharInv" => CharInv
  }
}
