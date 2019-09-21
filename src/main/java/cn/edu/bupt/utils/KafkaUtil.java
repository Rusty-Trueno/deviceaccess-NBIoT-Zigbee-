package cn.edu.bupt.utils;


import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.clients.producer.*;

import java.util.Properties;

/**
 * Created by Administrator on 2018/4/25.
 */
public class KafkaUtil {
    private static KfkProducer<String,String> kfkProducer = new KfkProducer<>("producerConfigDemo");
    public static void send(String key,String value){
        kfkProducer.send(key,value);
    }
}

class KfkProducer<K, V> {

    @Getter@Setter
    private Producer<K, V> producer;
    @Getter@Setter
    private Properties props;
    @Getter@Setter
    private String producerName;
    @Getter@Setter
    private String topic;

    public KfkProducer() {
    }


    public KfkProducer(String producerName) {
        this.producerName = producerName;
        loadProperties(producerName);
        loadProducer(props);
    }

    public KfkProducer(String yamlPath,String producerName) {
        this.producerName = producerName;
        loadProperties(yamlPath,producerName);
        loadProducer(props);
    }

//    public String getProducerName() {
//        return producerName;
//    }
//
//    public void setProducerName(String producerName) {
//        this.producerName = producerName;
//    }
//
//    public String getTopic() {
//        return topic;
//    }
//
//    public void setTopic(String topic) {
//        this.topic = topic;
//    }
//
//    public Properties getProps() {
//        return props;
//    }
//
//    public void setProps(Properties props) {
//        this.props = props;
//    }
//
//    public Producer<K, V> getProducer() {
//        return producer;
//    }
//
//    public void setProducer(Producer<K, V> producer) {
//        this.producer = producer;
//    }


    public Properties loadProperties(String prodecerName) {
        AnalysisYaml analysisYaml = new AnalysisYaml();
        ProducerProperties producerProperties = analysisYaml.getProducerProperties(producerName);
        if (producerProperties == null) {
            System.out.println("ProducerProperties为空,配置文件装载失败");
            return null;
        }
        topic = producerProperties.getTopic();
        props = new Properties();
        props.put("bootstrap.servers", producerProperties.getBootstrapServers());
        props.put("acks", producerProperties.getAcks());
        props.put("retries", producerProperties.getRetries());
        props.put("batch.size", producerProperties.getBatchSize());
        props.put("linger.ms", producerProperties.getLingerMs());
        props.put("buffer.memory", producerProperties.getBufferMemory());
        props.put("key.serializer", producerProperties.getKeySerializer());
        props.put("value.serializer", producerProperties.getValueSerializer());
        return props;
    }


    public Properties loadProperties(String yamlPath,String prodecerName) {
        AnalysisYaml analysisYaml = new AnalysisYaml(yamlPath);
        ProducerProperties producerProperties = analysisYaml.getProducerProperties(producerName);
        if (producerProperties == null) {
            System.out.println("ProducerProperties为空,配置文件装载失败");
            return null;
        }
        topic = producerProperties.getTopic();
        props = new Properties();
        props.put("bootstrap.servers", producerProperties.getBootstrapServers());
        props.put("acks", producerProperties.getAcks());
        props.put("retries", producerProperties.getRetries());
        props.put("batch.size", producerProperties.getBatchSize());
        props.put("linger.ms", producerProperties.getLingerMs());
        props.put("buffer.memory", producerProperties.getBufferMemory());
        props.put("key.serializer", producerProperties.getKeySerializer());
        props.put("value.serializer", producerProperties.getValueSerializer());
        return props;
    }


    public Producer loadProducer(Properties props) {
        if (props == null) {
            System.out.println("Properties为空");
            return null;
        }
        this.props = props;
        producer = new KafkaProducer<K, V>(props);
        return producer;
    }


    public void send(K key, V value) {
        if (producer == null) {
            System.out.println("Producer为空,Properties未装载");
            return;
        }
        producer.send(new ProducerRecord<K, V>(topic, key, value));
    }


    public void sendWithCallback(K key, V value, Callback callback) {
        if (producer == null) {
            System.out.println("Producer为空,Properties未装载");
            return;
        }
        producer.send(new ProducerRecord<K, V>(topic, key, value), callback);
    }

    /**
     * 关闭生产者
     *
     */
    public void close() {
        if (producer != null) {
            producer.close();
        }
    }

    // 测试
    public static void main(String args[]) {
        KfkProducer<String, String> producerrr = new KfkProducer<String, String>("producerTest");
        producerrr.send("qwer", "asdf");
        producerrr.close();
    }

}
