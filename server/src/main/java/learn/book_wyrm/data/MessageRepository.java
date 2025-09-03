package learn.book_wyrm.data;

import learn.book_wyrm.models.Message;

import java.util.List;

public interface MessageRepository {
    List<Message> findAll();
    Message findById(int id);
    List<Message> findByThreadId(int threadId);
    List<Message> findByUserId(int userId);
    Message add(Message message);
    boolean update(Message message);
    boolean deleteById(int id);

}