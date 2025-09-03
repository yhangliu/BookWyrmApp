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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
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
        DiscussionThread thread = repository.findById(1);
        assertNotNull(thread);
        assertEquals(1, thread.getId());
        assertEquals("Fantasy Book Recommendations", thread.getTitle());
    }

    @Test
    void shouldFindByBookclubId() {
        List<DiscussionThread> threads = repository.findByBookclubId(1);
        assertNotNull(threads);
        assertEquals(2, threads.size());
        assertEquals("Fantasy Book Recommendations", threads.get(0).getTitle());
    }

    @Test
    void shouldFindByCreatedBy() {
        List<DiscussionThread> threads = repository.findByCreatedBy(1);
        assertNotNull(threads);
        assertEquals(2, threads.size());
        assertEquals("Favorite Poems and Poets", threads.get(0).getTitle());
    }

    @Test
    void shouldAdd() {
        DiscussionThread thread = new DiscussionThread();
        thread.setBookclubId(1);
        thread.setTitle("New Thread");
        thread.setCreatedBy(1);
        thread.setCreatedAt(LocalDate.now());

        DiscussionThread result = repository.add(thread);
        assertNotNull(result);
        assertEquals("New Thread", result.getTitle());
    }

    @Test
    void shouldUpdate() {
        DiscussionThread thread = repository.findById(1);
        assertNotNull(thread);

        thread.setTitle("Updated Thread");
        assertTrue(repository.update(thread));

        DiscussionThread updatedThread = repository.findById(1);
        assertEquals("Updated Thread", updatedThread.getTitle());
    }

    @Test
    void shouldDeleteById() {
        assertTrue(repository.deleteById(1));
        assertNull(repository.findById(1));
    }

    @Test
    void shouldDeleteMessagesWhenThreadDeleted() {
        jdbcTemplate.update("INSERT INTO Messages (thread_id, user_id, message, created_at) VALUES (2, 1, 'Test Message', '2023-08-01')");

        assertTrue(repository.deleteById(2));
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Messages WHERE thread_id = 2", Integer.class);
        assertEquals(0, count);
    }
}
