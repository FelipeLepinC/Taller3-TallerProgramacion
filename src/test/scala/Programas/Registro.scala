package Programas

import java.util.Date

class Registro(val info: String, val fecha: Date) extends Serializable{
  def getInfo(): String = {
    return this.info
  }
  def getFecha(): Date = {
    return this.fecha
  }
}