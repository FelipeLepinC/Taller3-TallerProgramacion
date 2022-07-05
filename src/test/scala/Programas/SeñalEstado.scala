package Programas

class SeñalEstado(val flagEstado: Boolean) extends Serializable {
  def getFlagEstado(): Boolean ={
    return this.flagEstado
  }
}