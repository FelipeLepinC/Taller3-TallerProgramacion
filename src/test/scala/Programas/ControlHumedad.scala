package Programas

import javax.jms._
import org.apache.activemq.ActiveMQConnectionFactory
import org.apache.activemq.command.ActiveMQObjectMessage
import Programas.SensorHumedad.activeMqUrl
import org.apache.activemq

object ControlHumedad {
  val activeMqUrl: String = "tcp://localhost:61616"
  def main(args: Array[String]): Unit = {
    System.setProperty ("org.apache.activemq.SERIALIZABLE_PACKAGES", "*")
    val cFactory = new ActiveMQConnectionFactory(activeMqUrl)
    cFactory.setTrustAllPackages(true)
    val connection = cFactory.createConnection
    connection.start

    val session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)
    val cola = session.createQueue("medicionHumedad")

    val consumidor = session.createConsumer(cola)
    println("Conexion creada")

    val limiteInferior: Int = 25
    val limiteSuperior: Int = 75
    val listener = new MessageListener {
      def onMessage(message: Message): Unit = {
        message match {
          case obj: ObjectMessage => {
            val queueMessage = obj.asInstanceOf[ActiveMQObjectMessage]
            val payload = queueMessage.getObject().asInstanceOf[Datos]
            val porcentaje: Int = payload.getDato()

            val session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)
            val cola = session.createQueue(datos(0))
            val productor = session.createProducer(cola)

            if(porcentaje < limiteInferior){
              //Mandar
            }else if(porcentaje > limiteSuperior){

            }else{

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
