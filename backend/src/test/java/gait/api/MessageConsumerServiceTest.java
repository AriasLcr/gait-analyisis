package gait.api;

import gait.api.repositories.GaitMessageRepository;
import gait.api.services.MessageConsumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MessageConsumerServiceTest {

    @Mock
    private GaitMessageRepository repository;

    @InjectMocks
    private MessageConsumer messageConsumer;

    @Test
    void shouldSaveMessageToRepository() {
        String testMessage = "walking_test";

        // act
        messageConsumer.listen(testMessage);

        // verify
        verify(repository, times(1))
                .save(argThat(entity -> entity.getMessage().equals(testMessage)));
    }
}
