package Programas

import javax.jms._
import org.apache.activemq.ActiveMQConnectionFactory
import java.time.LocalDateTime

object SensorHumedad {

  val activeMqUrl: String = "tcp://localhost:61616"
  def main(args: Array[String]): Unit = {
    System.setProperty ("org.apache.activemq.SERIALIZABLE_PACKAGES", "*")
    val cFactory = new ActiveMQConnectionFactory(activeMqUrl)
    cFactory.setTrustAllPackages(true)
    val connection = cFactory.createConnection()
    connection.start()

    val session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)
    val cola = session.createQueue("medicionHumedad")
    println("Conexion y cola creada")

    val productor = session.createProducer(cola)
    val data = new DatosSensor()
    val obj: ObjectMessage = session.createObjectMessage(data)

    //val ser: DatosSensor = obj.getObject().asInstanceOf[DatosSensor]
    productor.send(obj)
    println("Mensaje enviado")
    connection.close()

  }
}
