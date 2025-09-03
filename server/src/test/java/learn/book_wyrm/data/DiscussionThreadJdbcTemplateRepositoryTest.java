package learn.book_wyrm.data;

import learn.book_wyrm.models.DiscussionThread;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DiscussionThreadJdbcTemplateRepositoryTest {

    @Autowired
    KnownGoodState knownGoodState;

    @Autowired
    DiscussionThreadJdbcTemplateRepository repository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindAll() {
        List<DiscussionThread> threads = repository.findAll();
        assertNotNull(threads);
        assertFalse(threads.isEmpty());
    }

    @Test
    void shouldFindById() {
        DiscussionThread thread = repository.findById(4);
        assertNotNull(thread);
        assertNull(repository.findById(9999));
        assertEquals(4, thread.getId());
        assertEquals("Best Poems of the 20th Century", thread.getTitle());
    }

    @Test
    void shouldFindByBookclubId() {
        List<DiscussionThread> threads = repository.findByBookclubId(4);
        assertNotNull(threads);
        assertFalse(threads.isEmpty());

    }

    @Test
    void shouldFindByCreatedBy() {
        List<DiscussionThread> threads = repository.findByCreatedBy(1);
        assertNotNull(threads);
        assertFalse(threads.isEmpty());
    }

    @Test
    void shouldAdd() {
        DiscussionThread thread = new DiscussionThread();
        thread.setBookclubId(5);
        thread.setTitle("New Thread");
        thread.setCreatedBy(1);
        thread.setCreatedAt(LocalDate.now());

        DiscussionThread result = repository.add(thread);
        assertNotNull(result);
        assertEquals("New Thread", result.getTitle());
    }


    @Test
    void shouldDeleteById() {
        assertTrue(repository.deleteById(3));
        assertNull(repository.findById(3));
    }

    @Test
    void shouldDeleteMessagesWhenThreadDeleted() {
        jdbcTemplate.update("INSERT INTO Messages (thread_id, user_id, message, created_at) VALUES (2, 1, 'Test Message', '2023-08-01')");

        assertTrue(repository.deleteById(2));
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Messages WHERE thread_id = 2", Integer.class);
        assertEquals(0, count);
    }
}