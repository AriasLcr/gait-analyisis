package gait.api;

import gait.api.models.GaitMessage;
import gait.api.repositories.GaitMessageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
public class MessageRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    @Autowired
    private GaitMessageRepository messageRepository;

    @DynamicPropertySource
    static void configureTestDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }

    @Test
    void shouldSaveAndFindMessage() {
        GaitMessage gaitMessage = new GaitMessage();
        gaitMessage.setMessage("db_test");
        messageRepository.save(gaitMessage);

        assertThat(messageRepository.findAll())
                .extracting(GaitMessage::getMessage)
                .containsExactly("db_test");
    }

    @Test
    void shouldFindMessageById() {
        GaitMessage gaitMessage = new GaitMessage();
        gaitMessage.setMessage("find_by_id_test");
        
        GaitMessage saved = messageRepository.save(gaitMessage);
        Optional<GaitMessage> found = messageRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getMessage()).isEqualTo("find_by_id_test");
    }
}
