package nitinka.hazelcast.recipes.activemq;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Member;

import java.util.List;
import java.util.Set;

/**
 * Created by nitinka on 30/10/14.
 */
public class RandomConfigChanger extends Thread{
    private final List<ConsumerConfig> consumerConfigs;
    private HazelcastInstance hazelcastInstance;

    public RandomConfigChanger(List<ConsumerConfig> consumerConfigs) {
        this.consumerConfigs = consumerConfigs;
        Config config = new Config();
        this.hazelcastInstance = Hazelcast.newHazelcastInstance(config);
        this.hazelcastInstance.getCluster().getLocalMember().setStringAttribute("ClusteredQueues","Test");
    }

    public void run() {
        while(true) {
            try {
                for(ConsumerConfig consumerConfig : this.consumerConfigs) {
                    synchronized (consumerConfig) {
                        Set<Member> members = this.hazelcastInstance.getCluster().getMembers();
                        long hosts = 0;
                        for(Member member : members) {
//                            ObjectMapperUtil.prettyPrint(member);
                            String[] queues = member.getStringAttribute("ClusteredQueues").split(",");
                            for(String queue : queues) {
                                if(queue.equals(consumerConfig.getName())) {
                                    hosts += 1;
                                }
                            }
                        }

                        System.out.println("Hosts for Queue : " + hosts);
                        int equalSplit = (int) (consumerConfig.getClusterConsumerSize() / hosts);
                        System.out.println("Equal Split: " + equalSplit);
                        System.out.println("Max Consumers: " + consumerConfig.getMaxConsumers());
                        consumerConfig.setConsumers(equalSplit);
                    }
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            finally {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

//    public static void main(String[] args) {
//        RandomConfigChanger rcc = new RandomConfigChanger(null);
//        rcc.hazelcastInstance.getCluster().getLocalMember().getAttributes()
//    }

}
