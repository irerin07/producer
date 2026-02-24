package com.example.producer.controller;

import com.example.producer.controller.dto.request.BookingRequest;
import com.example.producer.service.BookingIdGenerator;
import com.example.producer.service.BookingService;
import com.example.producer.service.dto.BookingCancelledEvent;
import com.example.producer.service.dto.BookingCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 誘쇨꼍??
 * @description booking controller
 * @since 2026.02.21
 **********************************************************************************************************************/
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

	private final BookingService bookingService;
	private final BookingIdGenerator bookingIdGenerator;

	@PostMapping
	public ResponseEntity<String> bookMovie(@RequestBody BookingRequest request) {
		long bookingId = bookingIdGenerator.nextId();
		BookingCreatedEvent payload = new BookingCreatedEvent(bookingId, request.movieId(), request.userId());
		bookingService.bookMovie(payload);
		return ResponseEntity.status(HttpStatus.CREATED).body("booking submitted");
	}

	@DeleteMapping("/{bookingId}")
	public ResponseEntity<Void> cancelBooking(@PathVariable long bookingId) {
		BookingCancelledEvent payload = new BookingCancelledEvent(bookingId);
		bookingService.cancelBooking(payload);
		return ResponseEntity.noContent().build();
	}

}
