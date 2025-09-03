package learn.book_wyrm.data;

import learn.book_wyrm.models.DiscussionThread;

import java.util.List;

public interface DiscussionThreadRepository {

    List<DiscussionThread> findAll();

    DiscussionThread findById(int id);

    List<DiscussionThread> findByBookclubId(int bookclubId);

    List<DiscussionThread> findByCreatedBy(int createdBy);

    DiscussionThread add(DiscussionThread thread);

    boolean update(DiscussionThread thread);

    boolean deleteById(int id);
}