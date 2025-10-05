package gait.api.controller;

import gait.api.utils.HtmlSanitizer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public MessageController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public String sendMessage(@RequestParam("message") String message) {
        String topic = "gait-posture-topic";
        String cleanMessage = HtmlSanitizer.sanitizeHtml(message);
        kafkaTemplate.send(topic, cleanMessage);
        return "Message sent to kafka:" + cleanMessage;
    }
}
