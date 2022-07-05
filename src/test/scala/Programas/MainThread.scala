package Programas

import org.apache.activemq.ActiveMQConnectionFactory
import Programas.ApiTemperatura.activeMqUrl

import javax.jms.{Session, TextMessage}

object MainAppThread {
  def main(args: Array[String]): Unit = {
    var thread = new MainThread("mqResp1")
    thread.start()
  }
}

class MainThread(mqid : String) extends Thread {
  override def run(): Unit = {
    val activeMqUrl: String = "tcp://localhost:61616"
    val cFactory = new ActiveMQConnectionFactory(activeMqUrl)
    val connection = cFactory.createConnection
    connection.start

    val session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)
    val cola = session.createQueue(mqid)

    val consumidor = session.createConsumer(cola)
    // TextMessaage mensaje = (TextMessage)(consumidor.receive())
    val mensaje:TextMessage = consumidor.receive().asInstanceOf[TextMessage]
    val txt = mensaje.getText
    println(s"Recibido : $txt")
    consumidor.close()
    session.close()
    connection.close()
    println("Thread en scala")
  }
}
