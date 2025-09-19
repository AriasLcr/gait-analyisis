package gait.api;

import gait.api.models.GaitMessage;
import gait.api.repositories.GaitMessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
public class MessageRepositoryTest {

    @Autowired
    private GaitMessageRepository messageRepository;

    @Test
    void shouldSaveAndFindMessage() {
        GaitMessage gaitMessage = new GaitMessage();
        gaitMessage.setMessage("db_test");
        messageRepository.save(gaitMessage);
    }
}
