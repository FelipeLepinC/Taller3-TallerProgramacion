package Programas

import java.time.LocalDateTime

class DatosSensor() extends Serializable {
  val rnd = new scala.util.Random
  val humedad = (35 + rnd.nextInt( (65 - 35) + 1 ))
  val fecha = LocalDateTime.now()
  def getHumedad(): Int ={
    return humedad
  }
  def getFecha(): LocalDateTime ={
    return fecha
  }
}
