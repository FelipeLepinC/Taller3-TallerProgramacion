package Programas

import javax.jms._
import org.apache.activemq.ActiveMQConnectionFactory

object SensorTemperatura {
  val activeMqUrl: String = "tcp://localhost:61616"

  def main(args: Array[String]): Unit = {
    val cFactory = new ActiveMQConnectionFactory(activeMqUrl)
    val connection = cFactory.createConnection()
    connection.start()

    val session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)
    val cola = session.createQueue("mqHost1")
    println("Conexion creada")
    val productor = session.createProducer(cola)
    val uuid = java.util.UUID.randomUUID.toString
    val txtMessage = session.createTextMessage(uuid + ";1,12,5")
    productor.send(txtMessage)
    println("Mensaje enviado")
    connection.close()

    var thread = new MainThread(uuid)
    thread.start()
  }
}
