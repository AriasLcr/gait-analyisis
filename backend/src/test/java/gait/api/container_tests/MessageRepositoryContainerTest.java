package gait.api.container_tests;

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
public class MessageRepositoryContainerTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    @Autowired
    private GaitMessageRepository gaitMessageRepository;

    @DynamicPropertySource
    static void configureTestDatabas(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }

    @Test
    void shouldSaveAndFindMessage() {
        GaitMessage gaitMessage = new GaitMessage();
        gaitMessage.setMessage("db_test");

        gaitMessageRepository.save(gaitMessage);

        Optional<GaitMessage> found = gaitMessageRepository.findById(gaitMessage.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getMessage().equals("db_test"));
    }
}
