package nitinka.hazelcast.recipes.activemq;

/**
 * Created by nitinka on 29/10/14.
 */
public class ConsumerConfig {
    private String name;
    public enum DestinationType {
        queue, topic
    }

    private DestinationType type;
    private int clusterConsumerSize;
    private int maxConsumers;
    private int consumers;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DestinationType getType() {
        return type;
    }

    public void setType(DestinationType type) {
        this.type = type;
    }

    public int getClusterConsumerSize() {
        return clusterConsumerSize;
    }

    public void setClusterConsumerSize(int clusterConsumerSize) {
        this.clusterConsumerSize = clusterConsumerSize;
    }

    public int getMaxConsumers() {
        return maxConsumers;
    }

    public void setMaxConsumers(int maxConsumers) {
        this.maxConsumers = maxConsumers;
    }

    public int getConsumers() {
        return consumers;
    }

    public void setConsumers(int consumers) {
        this.consumers = consumers;
        if(this.consumers > maxConsumers)
            this.consumers = maxConsumers;
    }
}
