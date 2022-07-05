package Programas

import javax.jms._
import org.apache.activemq.ActiveMQConnectionFactory

import java.util.Calendar

object SplitterSinHumedad {
  val activeMqUrl: String = "tcp://localhost:61616"
  def replicar(message: Message): Unit ={
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
    val objMedicion: ObjectMessage = session.createObjectMessage()
    objMedicion.setObject(medicion)
    productor.send(objMedicion)
    println("Señal de Encendido del Humidificador enviada")
    
    val registro = new Registro(info = "Poca humedad en el ambiente, se enciende el humidificador", fecha = Calendar.getInstance().getTime)
    val objRegistro: ObjectMessage = session.createObjectMessage()
    objRegistro.setObject(registro)
    productor2.send(objRegistro)
    println("Registro de Encendido de Humidificador enviado")
  }
}