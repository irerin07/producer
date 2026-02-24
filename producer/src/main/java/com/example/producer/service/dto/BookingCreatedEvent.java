package com.example.producer.service.dto;

/**
 * @author 沃섏눊瑗??
 * @description booking created event
 * @since 2026.02.23
 **********************************************************************************************************************/
public record BookingCreatedEvent(long bookingId, long movieId, long userId) {
}
