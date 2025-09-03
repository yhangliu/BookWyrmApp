package learn.book_wyrm.domain;

import learn.book_wyrm.data.DiscussionThreadRepository;
import learn.book_wyrm.models.DiscussionThread;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class DiscussionThreadServiceTest {

    @Autowired
    DiscussionThreadService service;

    @MockBean
    DiscussionThreadRepository repository;

    @Test
    void shouldFindAll() {
        DiscussionThread thread = new DiscussionThread();
        thread.setBookclubId(1);
        thread.setCreatedAt(LocalDate.now());
        thread.setTitle("Test title");
        thread.setCreatedBy(1);

        when(repository.findAll()).thenReturn(List.of(thread));

        List<DiscussionThread> threads = service.findAll();
        assertNotNull(threads);
        assertEquals(1, threads.size());
        assertEquals("Test title", threads.get(0).getTitle());
    }

    @Test
    void shouldFindByClubId() {
        DiscussionThread thread = new DiscussionThread();
        thread.setBookclubId(1);
        thread.setCreatedAt(LocalDate.now());
        thread.setTitle("Test title");
        thread.setCreatedBy(1);

        when(repository.findByBookclubId(1)).thenReturn(List.of(thread));

        List<DiscussionThread> threads = service.findByClubId(1);
        assertNotNull(threads);
        assertEquals(1, threads.size());
        assertEquals("Test title", threads.get(0).getTitle());
    }

    @Test
    void shouldFindByCreatedBy() {
        DiscussionThread thread = new DiscussionThread();
        thread.setBookclubId(1);
        thread.setCreatedAt(LocalDate.now());
        thread.setTitle("Test title");
        thread.setCreatedBy(1);

        when(repository.findByCreatedBy(1)).thenReturn(List.of(thread));

        List<DiscussionThread> threads = service.findByCreatedBy(1);
        assertNotNull(threads);
        assertEquals(1, threads.size());
        assertEquals("Test title", threads.get(0).getTitle());
    }

    @Test
    void shouldAddValidThread() {
        DiscussionThread thread = new DiscussionThread();
        thread.setBookclubId(1);
        thread.setCreatedAt(LocalDate.now());
        thread.setTitle("Test title");
        thread.setCreatedBy(1);

        when(repository.findAll()).thenReturn(List.of());

        Result<DiscussionThread> result = service.add(thread);
        assertTrue(result.isSuccess());
        assertEquals(thread, result.getPayload());
        verify(repository, times(1)).add(thread);
    }

    @Test
    void shouldFailToAddInvalidThread() {
        DiscussionThread thread = new DiscussionThread();
        thread.setBookclubId(1);
        thread.setCreatedAt(LocalDate.now().minusDays(1)); // Invalid date
        thread.setTitle(""); // Invalid title
        thread.setCreatedBy(1);

        Result<DiscussionThread> result = service.add(thread);
        System.out.println(result.getMessages());
        assertFalse(result.isSuccess());
        verify(repository, times(0)).add(any());
    }

    @Test
    void shouldUpdateValidThread() {
        DiscussionThread thread = new DiscussionThread();
        thread.setId(1);
        thread.setBookclubId(1);
        thread.setCreatedAt(LocalDate.now());
        thread.setTitle("Updated title");
        thread.setCreatedBy(1);

        // Mock the repository to return an existing thread with the same ID
        DiscussionThread existingThread = new DiscussionThread();
        existingThread.setId(1);
        existingThread.setBookclubId(1);
        existingThread.setCreatedAt(LocalDate.now());
        existingThread.setTitle("Old title");
        existingThread.setCreatedBy(1);

        when(repository.findById(1)).thenReturn(existingThread);

        Result<DiscussionThread> result = service.update(thread);
        System.out.println(result.getMessages());
        assertTrue(result.isSuccess());
        assertEquals(thread, result.getPayload());
        verify(repository, times(1)).update(thread);
    }

    @Test
    void shouldFailToUpdateInvalidThread() {
        DiscussionThread thread = new DiscussionThread();
        thread.setId(1);
        thread.setBookclubId(1);
        thread.setCreatedAt(LocalDate.now().minusDays(1)); // Invalid date
        thread.setTitle(""); // Invalid title
        thread.setCreatedBy(1);

        // Mock the repository to return an existing thread with the same ID
        DiscussionThread existingThread = new DiscussionThread();
        existingThread.setId(1);
        existingThread.setBookclubId(1);
        existingThread.setCreatedAt(LocalDate.now()); // Valid date
        existingThread.setTitle("Valid Title");
        existingThread.setCreatedBy(1);

        when(repository.findById(1)).thenReturn(existingThread);

        Result<DiscussionThread> result = service.update(thread);

        System.out.println(result.getMessages());
        assertFalse(result.isSuccess());
        verify(repository, times(0)).update(any());
    }

    @Test
    void shouldDeleteById() {
        DiscussionThread thread = new DiscussionThread();
        thread.setId(1);
        thread.setBookclubId(1);
        thread.setCreatedAt(LocalDate.now());
        thread.setTitle("Test title");
        thread.setCreatedBy(1);

        when(repository.findById(1)).thenReturn(thread);

        Result<Void> result = service.deleteById(1);
        assertTrue(result.isSuccess());
        verify(repository, times(1)).deleteById(1);
    }

    @Test
    void shouldFailToDeleteNonExistentThread() {
        when(repository.findById(1)).thenReturn(null);

        Result<Void> result = service.deleteById(1);
        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
        assertTrue(result.getMessages().contains("Discussion Thread not found with id 1"));
        verify(repository, times(0)).deleteById(1);
    }
}
