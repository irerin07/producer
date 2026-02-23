package com.example.producer.config;

import com.example.producer.service.dto.BookingCancelledEvent;
import com.example.producer.service.dto.BookingCreatedEvent;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;


import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;


/**
 * @author 沃섏눊瑗??
 * @description kafka producer config
 * @since 2026.02.23
 **********************************************************************************************************************/
@Configuration
public class KafkaProducerConfig {

	@Bean
	public ProducerFactory<String, BookingCreatedEvent> bookingCreatedProducerFactory(KafkaProperties properties) {
		Map<String, Object> config = properties.buildProducerProperties();
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);
		return new DefaultKafkaProducerFactory<>(config);
	}

	@Bean
	public ProducerFactory<String, BookingCancelledEvent> bookingCancelledProducerFactory(KafkaProperties properties) {
		Map<String, Object> config = properties.buildProducerProperties();
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);
		return new DefaultKafkaProducerFactory<>(config);
	}

	@Bean
	public KafkaTemplate<String, BookingCreatedEvent> bookingCreatedKafkaTemplate(
			ProducerFactory<String, BookingCreatedEvent> bookingCreatedProducerFactory) {
		return new KafkaTemplate<>(bookingCreatedProducerFactory);
	}

	@Bean
	public KafkaTemplate<String, BookingCancelledEvent> bookingCancelledKafkaTemplate(
			ProducerFactory<String, BookingCancelledEvent> bookingCancelledProducerFactory) {
		return new KafkaTemplate<>(bookingCancelledProducerFactory);
	}
}
