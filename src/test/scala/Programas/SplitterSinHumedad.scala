package Programas

import javax.jms._
import org.apache.activemq.ActiveMQConnectionFactory

object SplitterSinHumedad {
  val activeMqUrl: String = "tcp://localhost:61616"
  def replicar(message: Message): Unit ={
    System.setProperty ("org.apache.activemq.SERIALIZABLE_PACKAGES", "*")
    val cFactory = new ActiveMQConnectionFactory(activeMqUrl)
    cFactory.setTrustAllPackages(true)
    val connection = cFactory.createConnection()
    connection.start()

    val session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)
    val cola = session.createQueue("recibeMedicion")
    val cola2 = session.createQueue("regSinHumedad")

    val productor = session.createProducer(cola)
    val productor2 = session.createProducer(cola2)

    productor()
  }
  def main(args: Array[String], message: Message): Unit = {

  }
}
