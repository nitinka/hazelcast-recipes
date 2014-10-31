package nitinka.hazelcast.recipes.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by nitinka on 29/10/14.
 */
public class ConsumerMonitor extends Thread{

    private static Logger logger = LoggerFactory.getLogger(ConsumerMonitor.class);
    private final List<ConsumerConfig> consumerConfigs;
    private final ActiveMQConnectionFactory connectionFactory;
    private Map<String, List<Connection>> connectionsMap;

    public ConsumerMonitor(List<ConsumerConfig> consumerConfigs, ActiveMQConnectionFactory connectionFactory) {
        this.consumerConfigs = consumerConfigs;
        this.connectionFactory = connectionFactory;
        this.connectionsMap = new HashMap<String, List<Connection>>();
    }
    public void run() {
        while(true) {
            for(ConsumerConfig consumerConfig : consumerConfigs) {
                try {
                    balanceConnections(consumerConfig);
                }
                catch(Exception e) {
                    e.printStackTrace();
                    System.out.println(e.getLocalizedMessage());
                }

            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void balanceConnections(ConsumerConfig consumerConfig) {
        List<Connection> connections = connectionsMap.get(consumerConfig.getName());
        if(connections == null) {
            connections = new LinkedList<Connection>();
        }
        synchronized (consumerConfig) {
            System.out.println("Required Consumers: "+consumerConfig.getConsumers());
            System.out.println("Live Connections: "+connections.size());
            if(connections.size() > consumerConfig.getConsumers()) {
                for(int i=1; i<=connections.size() - consumerConfig.getConsumers(); i++) {
                    try {
                        System.out.println("Stopping a Connection");
                        connections.remove(i-1).close();
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                for(int i=1; i<=consumerConfig.getConsumers() - connections.size(); i++) {
                    try {
                        System.out.println("Starting a Connection");
                        connections.add(connectionFactory.createConnection());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        connectionsMap.put(consumerConfig.getName(), connections);
    }
}
