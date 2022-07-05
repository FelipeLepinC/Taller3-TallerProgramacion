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

    val limiteInferior: Int = 45
    val limiteSuperior: Int = 55

    val listener = new MessageListener {
      def onMessage(message: Message): Unit = {
        message match {
          case obj: ObjectMessage => {
            val queueMessage = obj.asInstanceOf[ActiveMQObjectMessage]
            val payload = queueMessage.getObject().asInstanceOf[DatosSensor]

            val humedad: Int = payload.getHumedad()
            val fecha = payload.getFecha()

            val session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)

            val data = new DatosControl(humedad, fecha)
            if(humedad < limiteInferior){
              println(s"Baja humedad: $humedad%")
              val cola = session.createQueue("SinHumedad")
              val productor = session.createProducer(cola)
              val obj: ObjectMessage = session.createObjectMessage(data)
              productor.send(obj)
            }else if(humedad > limiteSuperior){
              println(s"Sobre humedad: $humedad%")
              val cola = session.createQueue("SobreHumedad")
              val productor = session.createProducer(cola)
              val obj: ObjectMessage = session.createObjectMessage(data)
              productor.send(obj)
            }else{
              println(s"Humedad normal: $humedad%")
            }
          }
          case _ => {
            throw new Exception("Error desconocido")
          }
        }
      }
      consumidor.setMessageListener(listener)
    }
  }