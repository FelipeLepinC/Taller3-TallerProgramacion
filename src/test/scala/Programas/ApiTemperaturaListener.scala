package Programas

import javax.jms._
import org.apache.activemq.ActiveMQConnectionFactory
import .activeMqUrl

object ApiTemperaturaListener {
  val activeMqUrl: String = "tcp://localhost:61616"
  def main(args: Array[String]): Unit = {
    val cFactory = new ActiveMQConnectionFactory(activeMqUrl)
    val connection = cFactory.createConnection
    connection.start

    val session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)
    val cola = session.createQueue("mqHost1")

    val consumidor = session.createConsumer(cola)
    println("Conexion creada")
    val listener = new MessageListener {
      def onMessage(message: Message): Unit ={
        message match {
          case text: TextMessage => {
            val datos : Array[String] = text.getText.split(";")

            val session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)
            val cola = session.createQueue(datos(0))
            val datosSuma : Array[String] = datos(1).split(",")
            val datosInt : Array[Int] = datosSuma.map(_.toInt)
            val suma = datosInt(1) + datosInt(2)
            val productor = session.createProducer(cola)
            val txtMessage = session.createTextMessage(suma.toString)
            productor.send(txtMessage)
            println(s"Mensaje recibido : " + text.getText)
            println(s"Mensaje procesado: " + suma.toString)
          }
          case _ => {
            throw new Exception("Error desconocido")
          }
        }
      }
    }
    consumidor.setMessageListener(listener)
  }
}
