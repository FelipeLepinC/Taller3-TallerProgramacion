package Programas

class Registro(val mensaje: String, val humedad: Int) extends Serializable{
  def getMensaje(): String = {
    return this.mensaje
  }
  def getHumedad(): Int = {
    return this.humedad
  }
}