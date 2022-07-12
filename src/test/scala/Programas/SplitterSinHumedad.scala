package Programas

import javax.jms._
import org.apache.activemq.ActiveMQConnectionFactory
import org.apache.activemq.command.ActiveMQObjectMessage

object SplitterSinHumedad {
  val activeMqUrl: String = "tcp://localhost:61616"
  def replicar(message: Message): Unit ={
    message match {
      case obj: ObjectMessage => {
        val queueMessage = obj.asInstanceOf[ActiveMQObjectMessage]
        val payload = queueMessage.getObject().asInstanceOf[DatosSensor]

        System.setProperty ("org.apache.activemq.SERIALIZABLE_PACKAGES", "*")
        val cFactory = new ActiveMQConnectionFactory(activeMqUrl)
        cFactory.setTrustAllPackages(true)
        val connection = cFactory.createConnection()
        connection.start()

        val session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)
        val cola = session.createQueue("estadoHumidificador")
        val cola2 = session.createQueue("regSinHumedad")

        val productor = session.createProducer(cola)
        val productor2 = session.createProducer(cola2)

        val medicion = new SeñalEstado(true)
        val objMedicion: ObjectMessage = session.createObjectMessage(medicion)
        productor.send(objMedicion)
        println("Señal de Encendido del Humidificador enviada")

        productor2.send(obj)
        val humedad = payload.getHumedad()
        println(s"Registro de humedad baja ($humedad%) enviado")

      }
      case _ => {
        throw new Exception("Error desconocido")
      }
    }
  }
}
