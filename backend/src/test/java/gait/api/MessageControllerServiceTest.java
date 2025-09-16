package gait.api;

import gait.api.controller.MessageController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
class MessageControllerServiceTest {

	@Mock
	private KafkaTemplate<String, String> kafkaTemplate;

	@InjectMocks
	private MessageController messageController;

	@Test
	void shouldSendMessageToKafka() {
		String testMessage = "Hello, Kafka!";

		// act
		messageController.sendMessage(testMessage);

		// verify
		verify(kafkaTemplate, times(1))
				.send("gait-posture-topic", testMessage);
	}

}
