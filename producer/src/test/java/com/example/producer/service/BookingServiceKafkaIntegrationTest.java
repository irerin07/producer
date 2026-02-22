package com.example.producer.service;

import java.time.Duration;
import java.util.Map;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(
		partitions = 1,
		topics = {"booking.created", "booking.cancelled"},
		brokerProperties = {"listeners=PLAINTEXT://localhost:0", "port=0"}
)
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}")
class BookingServiceKafkaIntegrationTest {

	@Autowired
	private BookingService bookingService;

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	private Consumer<String, String> consumer;

	@BeforeEach
	void setUp() {
		Map<String, Object> props = KafkaTestUtils.consumerProps("booking-test", "false");
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		consumer = new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new StringDeserializer())
				.createConsumer();
		consumer.subscribe(List.of("booking.created", "booking.cancelled"));
	}

	@AfterEach
	void tearDown() {
		if (consumer != null) {
			consumer.close(Duration.ofSeconds(5));
		}
	}

	@Test
	void bookMoviePublishesMessage() {
		bookingService.bookMovie(12L, 34L);

		ConsumerRecord<String, String> record = KafkaTestUtils.getSingleRecord(consumer, "booking.created");
		assertThat(record.value()).isEqualTo("{\"movieId\":12,\"userId\":34}");
	}

	@Test
	void cancelBookingPublishesMessage() {
		bookingService.cancelBooking(99L);

		ConsumerRecord<String, String> record = KafkaTestUtils.getSingleRecord(consumer, "booking.cancelled");
		assertThat(record.value()).isEqualTo("{\"bookingId\":99}");
	}
}
