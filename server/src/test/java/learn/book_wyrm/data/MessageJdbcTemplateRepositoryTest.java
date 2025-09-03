package learn.book_wyrm.data;

import learn.book_wyrm.models.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
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
        assertTrue(messages.size() > 0);
    }

    @Test
    void shouldFindById() {
        List<Message> all = repository.findAll();
        all.forEach(message -> System.out.println(message.getId()));
        Message message = repository.findById(2);
        assertNotNull(message);
        assertEquals(2, message.getId());
    }

    @Test
    void shouldFindByThreadId() {
        List<Message> messages = repository.findByThreadId(4);
        assertNotNull(messages);
        assertTrue(messages.size() > 0);
    }

    @Test
    void shouldFindByUserId() {
        List<Message> messages = repository.findByUserId(2);
        assertNotNull(messages);
        messages.forEach(m-> System.out.println(m.toString()));
        assertTrue(messages.size() > 0);
    }

    @Test
    void shouldAdd() {
        Message message = makeMessage();
        Message addedMessage = repository.add(message);
        List<Message> all = repository.findAll();
        assertNotNull(addedMessage);
        assertTrue(all.size() > 0);
    }

    @Test
    void shouldUpdate() {
        Message message = repository.findById(3);
        message.setMessage("Updated message.");
        assertTrue(repository.update(message));
        assertEquals("Updated message.", message.getMessage());

    }

    @Test
    void shouldDeleteById() {
        assertTrue(repository.deleteById(4));
        assertFalse(repository.deleteById(4));
    }

    Message makeMessage() {
        Message message = new Message();
        message.setThreadId(4);
        message.setUserId(2);
        message.setMessage("Test message.");
        message.setCreatedAt(LocalDate.of(2023, 8, 1));
        return message;
    }
}