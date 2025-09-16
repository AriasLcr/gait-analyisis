package gait.api.services;

import gait.api.models.GaitMessage;
import gait.api.repositories.GaitMessageRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumer {

    private final GaitMessageRepository repository;

    public MessageConsumer(GaitMessageRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics="gait-posture-topic", groupId = "gait-analysis-group")
    public void listen(String message) {
        System.out.println("Received message: " + message);

        GaitMessage gaitMessage = new GaitMessage();
        gaitMessage.setMessage(message);
        repository.save(gaitMessage);
    }
}
