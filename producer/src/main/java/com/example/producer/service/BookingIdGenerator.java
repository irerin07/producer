package com.example.producer.service;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;

/**
 * @author 沃섏눊瑗??
 * @description booking id generator
 * @since 2026.02.23
 **********************************************************************************************************************/
@Component
public class BookingIdGenerator {

	private final AtomicLong sequence = new AtomicLong(System.currentTimeMillis());

	public long nextId() {
		return sequence.incrementAndGet();
	}
}
