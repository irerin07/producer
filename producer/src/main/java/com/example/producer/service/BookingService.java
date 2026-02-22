package com.example.producer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @author 誘쇨꼍??
 * @description booking service
 * @since 2026.02.21
 **********************************************************************************************************************/
@Service
@RequiredArgsConstructor
public class BookingService {

	private final KafkaTemplate<String, String> kafkaTemplate;

	@Value("${app.kafka.topics.booking-created}")
	private String bookingTopic;

	@Value("${app.kafka.topics.booking-cancelled}")
	private String cancelTopic;

	public void bookMovie(long movieId, long userId) {
		String payload = "{\"movieId\":" + movieId + ",\"userId\":" + userId + "}";
		kafkaTemplate.send(bookingTopic, payload);
	}

	public void cancelBooking(long bookingId) {
		String payload = "{\"bookingId\":" + bookingId + "}";
		kafkaTemplate.send(cancelTopic, payload);
	}
}
