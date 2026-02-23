package com.example.producer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.example.producer.service.dto.BookingCancelledEvent;
import com.example.producer.service.dto.BookingCreatedEvent;

/**
 * @author 誘쇨꼍??
 * @description booking service
 * @since 2026.02.21
 **********************************************************************************************************************/
@Service
public class BookingService {

	@Qualifier("bookingCreatedKafkaTemplate")
	private final KafkaTemplate<String, BookingCreatedEvent> bookingCreatedKafkaTemplate;

	@Qualifier("bookingCancelledKafkaTemplate")
	private final KafkaTemplate<String, BookingCancelledEvent> bookingCancelledKafkaTemplate;

	@Value("${app.kafka.topics.booking-created}")
	private String bookingTopic;

	@Value("${app.kafka.topics.booking-cancelled}")
	private String cancelTopic;

	public BookingService(
			@Qualifier("bookingCreatedKafkaTemplate")
			KafkaTemplate<String, BookingCreatedEvent> bookingCreatedKafkaTemplate,
			@Qualifier("bookingCancelledKafkaTemplate")
			KafkaTemplate<String, BookingCancelledEvent> bookingCancelledKafkaTemplate) {
		this.bookingCreatedKafkaTemplate = bookingCreatedKafkaTemplate;
		this.bookingCancelledKafkaTemplate = bookingCancelledKafkaTemplate;
	}

	public void bookMovie(BookingCreatedEvent payload) {
		validateBookingCreated(payload);
		bookingCreatedKafkaTemplate.send(bookingTopic, payload);
	}

	public void cancelBooking(BookingCancelledEvent payload) {
		validateBookingCancelled(payload);
		bookingCancelledKafkaTemplate.send(cancelTopic, payload);
	}

	private void validateBookingCreated(BookingCreatedEvent payload) {
		if (payload == null) {
			throw new IllegalArgumentException("booking created payload must not be null");
		}
		if (payload.movieId() <= 0) {
			throw new IllegalArgumentException("movieId must be positive");
		}
		if (payload.userId() <= 0) {
			throw new IllegalArgumentException("userId must be positive");
		}
	}

	private void validateBookingCancelled(BookingCancelledEvent payload) {
		if (payload == null) {
			throw new IllegalArgumentException("booking cancelled payload must not be null");
		}
		if (payload.bookingId() <= 0) {
			throw new IllegalArgumentException("bookingId must be positive");
		}
	}
}
