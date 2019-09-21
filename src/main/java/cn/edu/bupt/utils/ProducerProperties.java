package cn.edu.bupt.utils;

public class ProducerProperties {

	private String bootstrapServers;
	private String topic;
	private String acks = "1";
	private int retries = 0;
	private int batchSize = 16384;
	private int lingerMs = 1;
	private int bufferMemory = 33554432;
	private String keySerializer = "org.apache.kafka.common.serialization.StringSerializer";
	private String valueSerializer = "org.apache.kafka.common.serialization.StringSerializer";

	public ProducerProperties() {

	}

	public String getBootstrapServers() {
		return bootstrapServers;
	}

	public void setBootstrapServers(String bootstrapServers) {
		this.bootstrapServers = bootstrapServers;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getAcks() {
		return acks;
	}

	public void setAcks(String acks) {
		this.acks = acks;
	}

	public int getRetries() {
		return retries;
	}

	public void setRetries(int retries) {
		this.retries = retries;
	}

	public int getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}

	public int getLingerMs() {
		return lingerMs;
	}

	public void setLingerMs(int lingerMs) {
		this.lingerMs = lingerMs;
	}

	public int getBufferMemory() {
		return bufferMemory;
	}

	public void setBufferMemory(int bufferMemory) {
		this.bufferMemory = bufferMemory;
	}

	public String getKeySerializer() {
		return keySerializer;
	}

	public void setKeySerializer(String keySerializer) {
		this.keySerializer = keySerializer;
	}

	public String getValueSerializer() {
		return valueSerializer;
	}

	public void setValueSerializer(String valueSerializer) {
		this.valueSerializer = valueSerializer;
	}

}