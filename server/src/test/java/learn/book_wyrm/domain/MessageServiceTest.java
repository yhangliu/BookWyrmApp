package learn.book_wyrm.domain;

import learn.book_wyrm.data.AppUserRepository;
import learn.book_wyrm.data.DiscussionThreadRepository;
import learn.book_wyrm.data.MessageRepository;
import learn.book_wyrm.models.*;
import learn.book_wyrm.models.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@SpringBootTest
class MessageServiceTest {

    @MockBean
    MessageRepository repository;
    @MockBean
    AppUserRepository userRepository;
    @MockBean
    DiscussionThreadRepository threadRepository;

    @Autowired
    MessageService service;

    @Test
    void shouldAdd() {
        Message expected = makeMessage();
        Message msg = makeMessage();

        DiscussionThread thread = new DiscussionThread();
        thread.setId(1);  // Ensure the thread has an ID to match with the message
        thread.setBookclubId(1);
        thread.setCreatedAt(LocalDate.now());
        thread.setTitle("Test title");
        thread.setCreatedBy(1);

        AppUser user = new AppUser(1, "user1", "user1@example.com", "password1", false, LocalDate.now());

        // Mock the user and thread repositories to return valid objects
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(threadRepository.findAll()).thenReturn(List.of(thread));

        // Mock the add operation on the repository
        when(repository.add(msg)).thenReturn(expected);

        // Run the add method on the service and validate the result
        Result<Message> result = service.add(msg);
        assertEquals(ResultType.SUCCESS, result.getType());
        assertEquals(expected, result.getPayload());
    }


    @Test
    void shouldNotAddEmptyMessage() {
        Message expected = makeMessage();
        Message msg = makeMessage();
        msg.setMessage("");

        when(repository.add(msg)).thenReturn(expected);
        Result<Message> result = service.add(msg);
        assertEquals(ResultType.INVALID, result.getType());
    }


    @Test
    void shouldNotAddWithId() {
        Message expected = makeMessage();
        Message msg = makeMessage();
        msg.setId(5);

        when(repository.add(msg)).thenReturn(expected);
        Result<Message> result = service.add(msg);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldUpdate() {
        Message msg = makeMessage();
        msg.setId(5); // Set a valid ID for updating
        DiscussionThread thread = new DiscussionThread();
        thread.setId(1);  // Ensure the thread has an ID to match with the message
        thread.setBookclubId(1);
        thread.setCreatedAt(LocalDate.now());
        thread.setTitle("Test title");
        thread.setCreatedBy(1);

        // Mock the user and thread repositories to return valid objects
        when(userRepository.findAll()).thenReturn(List.of(new AppUser(1, "user1", "user1@example.com", "password1", false, LocalDate.now())));
        when(threadRepository.findAll()).thenReturn(List.of(thread));

        // Mock the update operation on the repository
        when(repository.update(msg)).thenReturn(true);

        // Run the update method on the service and validate the result
        Result<Message> result = service.update(msg);
        assertEquals(ResultType.SUCCESS, result.getType());
    }

    @Test
    void shouldNotUpdateWithoutId() {
        Message msg = makeMessage();
        when(repository.update(msg)).thenReturn(true);
        Result<Message> result = service.update(msg);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotUpdateWithoutMessage() {
        Message msg = makeMessage();
        msg.setMessage("LOL");
        when(repository.update(msg)).thenReturn(true);
        Result<Message> result = service.update(msg);
        assertEquals(ResultType.INVALID, result.getType());
    }

    Message makeMessage(){
        Message message = new Message();
        message.setUserId(1);
        message.setThreadId(1);
        message.setMessage("You're wrong!");
        return message;
    }
}