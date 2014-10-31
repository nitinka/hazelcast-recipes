package nitinka.hazelcast.recipes.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nitinka on 29/10/14.
 */
public class DActivemqConsumerUtil {
    public static void main(String[] args) throws JMSException, InterruptedException {
        // Create a ConnectionFactory

        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        List<ConsumerConfig> consumerConfigList = new ArrayList<ConsumerConfig>();
        ConsumerConfig cc = new ConsumerConfig();
        cc.setMaxConsumers(10);
        cc.setConsumers(6);
        cc.setName("Test");
        cc.setType(ConsumerConfig.DestinationType.queue);
        cc.setClusterConsumerSize(20);

        consumerConfigList.add(cc);
        ConsumerMonitor cm = new ConsumerMonitor(consumerConfigList, connectionFactory);

        RandomConfigChanger rcc = new RandomConfigChanger(consumerConfigList);
        cm.start();
        rcc.start();
        cm.join();
        rcc.join();
//
//        // Create a Connection
//        Connection connection = connectionFactory.createConnection();
//        connection.start();
//
//        // Create a Session
//        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//
//        // Create the destination (Topic or Queue)
//        Destination destination = session.createQueue("TEST.FOO");
//
//        // Create a MessageProducer from the Session to the Topic or Queue
//        MessageProducer producer = session.createProducer(destination);
//        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
//
//        // Create a messages
//        String text = "Hello world!";
//        TextMessage message = session.createTextMessage(text);
//
//        // Tell the producer to send the message
//        System.out.println("Sent message: "+ message.hashCode() + " : " + Thread.currentThread().getName());
//        producer.send(message);
//
//        // Clean up
//        session.close();
//        connection.close();
    }
}
