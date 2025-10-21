package gait.api.integration_tests;

import gait.api.controller.MessageController;
import gait.api.models.GaitMessage;
import gait.api.repositories.GaitMessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"gait-posture-topic"}, brokerProperties = {
    "listeners=PLAINTEXT://localhost:9093",
    "port=9093"
})
@DirtiesContext
@Import(TestKafkaConfig.class)
public class KafkaIntegrationTest {

    @Autowired
    private MessageController messageController;

    @Autowired
    private GaitMessageRepository gaitMessageRepository;

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @BeforeEach
    void setUp() {
        // Clean the repository before each test
        gaitMessageRepository.deleteAll();
        
        // Wait for Kafka listeners to be assigned and ready
        for (MessageListenerContainer messageListenerContainer : kafkaListenerEndpointRegistry.getListenerContainers()) {
            ContainerTestUtils.waitForAssignment(messageListenerContainer, embeddedKafkaBroker.getPartitionsPerTopic());
        }
    }

    @Test
    void shouldSendAndConsumeMessage() {
        String testMessage = "integration_test_message";

        // Send message through controller
        String response = messageController.sendMessage(testMessage);
        assertThat(response).contains(testMessage);

        // Wait for the message to be consumed and persisted
        Awaitility.await()
                .atMost(Duration.ofSeconds(10))
                .pollInterval(Duration.ofMillis(500))
                .until(() -> {
                    List<GaitMessage> messages = gaitMessageRepository.findAll();
                    return messages.stream()
                            .anyMatch(gaitMessage -> gaitMessage.getMessage().equals(testMessage));
                });

        // Verify the message was persisted
        List<GaitMessage> messages = gaitMessageRepository.findAll();
        assertThat(messages)
                .extracting(GaitMessage::getMessage)
                .contains(testMessage);
        
        // Verify only one message was persisted for this test
        assertThat(messages).hasSize(1);
    }

}

