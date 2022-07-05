package Programas

import javax.jms._
import org.apache.activemq.ActiveMQConnectionFactory

object ApiTemperatura {
  val activeMqUrl: String = "tcp://localhost:61616"
  def main(args: Array[String]): Unit = {
    val cFactory = new ActiveMQConnectionFactory(activeMqUrl)
    val connection = cFactory.createConnection
    connection.start

    val session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)
    val cola = session.createQueue("mqResp1")

    val consumidor = session.createConsumer(cola)
    // TextMessaage mensaje = (TextMessage)(consumidor.receive())
    val mensaje:TextMessage = consumidor.receive().asInstanceOf[TextMessage]
    println("Conexion creada")
    val txt = mensaje.getText
    println(s"Recibido : $txt")
    consumidor.close()
    session.close()
    connection.close()
  }
}
