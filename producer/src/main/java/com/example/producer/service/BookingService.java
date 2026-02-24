package com.example.producer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.example.producer.service.dto.BookingCancelledEvent;
import com.example.producer.service.dto.BookingCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 誘쇨꼍??
 * @description booking service
 * @since 2026.02.21
 **********************************************************************************************************************/
@Service
public class BookingService {

	private static final Logger log = LoggerFactory.getLogger(BookingService.class);

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
		bookingCreatedKafkaTemplate.send(bookingTopic, String.valueOf(payload.bookingId()), payload)
				.whenComplete((result, ex) -> {
					if (ex != null) {
						log.error("Failed to send booking created message: {} (key={})", payload, payload.bookingId(), ex);
						return;
					}
					log.info("Sent booking created message: {} (key={}, topic={}, partition={}, offset={})",
							payload,
							payload.bookingId(),
							result.getRecordMetadata().topic(),
							result.getRecordMetadata().partition(),
							result.getRecordMetadata().offset());
				});
	}

	public void cancelBooking(BookingCancelledEvent payload) {
		validateBookingCancelled(payload);
		bookingCancelledKafkaTemplate.send(cancelTopic, String.valueOf(payload.bookingId()), payload)
				.whenComplete((result, ex) -> {
					if (ex != null) {
						log.error("Failed to send booking cancelled message: {} (key={})", payload, payload.bookingId(), ex);
						return;
					}
					log.info("Sent booking cancelled message: {} (key={}, topic={}, partition={}, offset={})",
							payload,
							payload.bookingId(),
							result.getRecordMetadata().topic(),
							result.getRecordMetadata().partition(),
							result.getRecordMetadata().offset());
				});
	}

	private void validateBookingCreated(BookingCreatedEvent payload) {
		if (payload == null) {
			throw new IllegalArgumentException("booking created payload must not be null");
		}
		if (payload.bookingId() <= 0) {
			throw new IllegalArgumentException("bookingId must be positive");
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
