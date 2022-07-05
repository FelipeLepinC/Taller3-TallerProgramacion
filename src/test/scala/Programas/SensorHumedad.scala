package Programas

import javax.jms._
import org.apache.activemq.ActiveMQConnectionFactory

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

    val data = new Datos(20)
    val obj: ObjectMessage = session.createObjectMessage()
    obj.setObject(data)

    val ser: Datos = obj.getObject().asInstanceOf[Datos]
    productor.send(obj)
    println("Mensaje enviado")
    connection.close()

    //var thread = new MainThread(uuid)
    //thread.start()
  }
}
