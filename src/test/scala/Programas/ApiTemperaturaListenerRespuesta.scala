package Programas

import org.apache.activemq.ActiveMQConnectionFactory

import javax.jms._

object ApiTemperaturaListenerRespuesta {
  val activeMqUrl: String = "tcp://localhost:61616"
  def main(args: Array[String]): Unit = {
    val cFactory = new ActiveMQConnectionFactory(activeMqUrl)
    val connection = cFactory.createConnection
    connection.start

    val session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)
    val cola = session.createQueue("mqResp1")

    val consumidor = session.createConsumer(cola)
    println("Conexion creada")
    val listener = new MessageListener {
      def onMessage(message: Message): Unit ={
        message match {
          case text: TextMessage => {
            println(s"Mensaje procesado : " + text.getText)
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
