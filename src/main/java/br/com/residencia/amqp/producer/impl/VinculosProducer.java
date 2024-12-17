package br.com.residencia.amqp.producer.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import br.com.residencia.amqp.producer.KafkaTemplateAbstract;
import br.com.residencia.dto.VinculoRequestDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class VinculosProducer extends KafkaTemplateAbstract<VinculoRequestDto> {
	
	@Value("${vinculo.topic.name}")
	private String topic;
	
	public void producer(@Payload VinculoRequestDto dto) {
		
		kafkaTemplate.send(topic, dto).addCallback(
				success -> log.info("Mensagem publicada: {}", success.getProducerRecord().value()),
				failure -> log.info("Erro ao publicar mensagem: {}", failure.getMessage())
		);	
		
	}

	@Async("asyncKafka")
	public void producerAsync(@Payload VinculoRequestDto dto) {
		
		Runnable runnable = () -> kafkaTemplate.send(topic, dto).addCallback(new ListenableFutureCallback<>() {

			@Override
			public void onSuccess(SendResult<String, VinculoRequestDto> result) {
				
				log.info("Mensagem publicada: {}", result.getProducerRecord().value());
				
			}

			@Override
			public void onFailure(Throwable ex) {
				
				if(ex != null)
					log.error("Erro ao publicar mensagem: {}", ex.getMessage());
				
			}

        });
	    new Thread(runnable).start();
		
	}
	
}

