package com.example.producer.service;

import com.example.producer.service.dto.BookingCancelledEvent;
import com.example.producer.service.dto.BookingCreatedEvent;
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

	private final KafkaTemplate<String, Object> kafkaTemplate;

	@Value("${app.kafka.topics.booking-created}")
	private String bookingTopic;

	@Value("${app.kafka.topics.booking-cancelled}")
	private String cancelTopic;

	public void bookMovie(long movieId, long userId) {
		BookingCreatedEvent payload = new BookingCreatedEvent(movieId, userId);
		kafkaTemplate.send(bookingTopic, payload);
	}

	public void cancelBooking(long bookingId) {
		BookingCancelledEvent payload = new BookingCancelledEvent(bookingId);
		kafkaTemplate.send(cancelTopic, payload);
	}
}
