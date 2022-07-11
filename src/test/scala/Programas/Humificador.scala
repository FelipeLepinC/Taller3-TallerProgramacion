package Programas
import javax.jms._
import org.apache.activemq.ActiveMQConnectionFactory
import org.apache.activemq.command.ActiveMQObjectMessage


object Humificador {
  val activeMqUrl: String = "tcp://localhost:61616"
  def main(args: Array[String]): Unit = {
    System.setProperty ("org.apache.activemq.SERIALIZABLE_PACKAGES", "*")
    val cFactory = new ActiveMQConnectionFactory(activeMqUrl)
    cFactory.setTrustAllPackages(true)
    val connection = cFactory.createConnection
    connection.start

    val session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)
    val cola = session.createQueue("estadoHumidificador")

    val consumidor = session.createConsumer(cola)
    println("Conexion creada")


    val listener = new MessageListener {
      def onMessage(message: Message): Unit = {
        message match {
          case obj: ObjectMessage => {
            val queueMessage = obj.asInstanceOf[ActiveMQObjectMessage]
            val payload = queueMessage.getObject().asInstanceOf[SeñalEstado]
            val flag = payload.getFlagEstado()
            if(flag){
              println("Humificador activado!")
            }
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
