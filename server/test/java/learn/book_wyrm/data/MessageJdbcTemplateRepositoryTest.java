package learn.book_wyrm.data;

import learn.book_wyrm.models.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class MessageJdbcTemplateRepositoryTest {

    @Autowired
    MessageJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindAll() {
        List<Message> messages = repository.findAll();
        assertNotNull(messages);
        assertTrue(messages.size() >= 8);
    }

    @Test
    void shouldFindById() {
        Message message = repository.findById(1);
        assertNotNull(message);
        assertEquals(1, message.getId());
    }

    @Test
    void shouldFindByThreadId() {
        List<Message> messages = repository.findByThreadId(1);
        assertNotNull(messages);
        assertTrue(messages.size() >= 2);
    }

    @Test
    void shouldFindByUserId() {
        List<Message> messages = repository.findByUserId(1);
        assertNotNull(messages);
        assertTrue(messages.size() >= 2);
    }

    @Test
    void shouldAdd() {
        Message message = makeMessage();
        Message addedMessage = repository.add(message);
        assertNotNull(addedMessage);
        assertEquals(9, addedMessage.getId());
    }

    @Test
    void shouldUpdate() {
        Message message = makeMessage();
        message.setId(1);
        message.setMessage("Updated message.");
        assertTrue(repository.update(message));
        Message updatedMessage = repository.findById(1);
        assertEquals("Updated message.", updatedMessage.getMessage());
    }

    @Test
    void shouldDeleteById() {
        assertTrue(repository.deleteById(1));
        assertFalse(repository.deleteById(1));
    }

    Message makeMessage() {
        Message message = new Message();
        message.setThreadId(1);
        message.setUserId(1);
        message.setMessage("Test message.");
        message.setCreatedAt(LocalDate.of(2023, 8, 1));
        return message;
    }
}