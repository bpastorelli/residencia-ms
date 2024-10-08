package br.com.residencia.amqp.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

public abstract class KafkaTemplateAbstract<T> {

	@Autowired
	protected KafkaTemplate<String, T> kafkaTemplate;
	
}
