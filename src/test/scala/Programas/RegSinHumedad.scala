package Programas

import javax.jms._
import org.apache.activemq.ActiveMQConnectionFactory
import org.apache.activemq.command.ActiveMQObjectMessage
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClientBuilder
import com.google.gson.Gson
import org.apache.http.entity.StringEntity
import org.apache.http.util.EntityUtils

object RegSinHumedad {
  val activeMqUrl: String = "tcp://localhost:61616"
  def main(args: Array[String]): Unit = {
    System.setProperty ("org.apache.activemq.SERIALIZABLE_PACKAGES", "*")
    val cFactory = new ActiveMQConnectionFactory(activeMqUrl)
    cFactory.setTrustAllPackages(true)
    val connection = cFactory.createConnection
    connection.start()

    val session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)
    val cola = session.createQueue("regSinHumedad")

    val consumidor = session.createConsumer(cola)
    println("Conexion creada")


    val listener = new MessageListener {
      def onMessage(message: Message): Unit = {
        message match {
          case obj: ObjectMessage => {
            val queueMessage = obj.asInstanceOf[ActiveMQObjectMessage]
            val objMens = queueMessage.getObject().asInstanceOf[Registro]

            val regToJson = new Gson().toJson(objMens)

            val post = new HttpPost("https://hpeu4bkf0h.execute-api.us-east-1.amazonaws.com/desa/sensorhumedad")
            val params = new StringEntity(regToJson)
            post.addHeader("content-type", "application/json")
            post.setEntity(params)

            val client = HttpClientBuilder.create().build()
            val response = client.execute(post)
            val body = EntityUtils.toString(response.getEntity)
            println(body)
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
