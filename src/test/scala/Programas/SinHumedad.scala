package Programas
import javax.jms._
import org.apache.activemq.ActiveMQConnectionFactory
import org.apache.activemq.command.ActiveMQObjectMessage


object SinHumedad {
  val activeMqUrl: String = "tcp://localhost:61616"
  def main(args: Array[String]): Unit = {
    System.setProperty ("org.apache.activemq.SERIALIZABLE_PACKAGES", "*")
    val cFactory = new ActiveMQConnectionFactory(activeMqUrl)
    cFactory.setTrustAllPackages(true)
    val connection = cFactory.createConnection
    connection.start

    val session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)
    val cola = session.createQueue("regSinHumedad")

    val consumidor = session.createConsumer(cola)
    println("Conexion creada")


    val listener = new MessageListener {
      def onMessage(message: Message): Unit = {
        message match {
          case obj: ObjectMessage => {
            val queueMessage = obj.asInstanceOf[ActiveMQObjectMessage]
            val payload = queueMessage.getObject().asInstanceOf[DatosSensor]
            val humedad = payload.getHumedad()
            val fecha = payload.getFecha()
            println(s"Registro guardado: $humedad - $fecha")
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
