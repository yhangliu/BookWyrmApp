package learn.book_wyrm.domain;

import learn.book_wyrm.data.DiscussionThreadRepository;
import learn.book_wyrm.models.DiscussionThread;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DiscussionThreadService {

    private final DiscussionThreadRepository repository;

    public DiscussionThreadService(DiscussionThreadRepository repository) {
        this.repository = repository;
    }

    public List<DiscussionThread> findAll() {
        return repository.findAll();
    }

    public List<DiscussionThread> findByClubId(int clubId) {
        return repository.findByBookclubId(clubId);
    }

    public List<DiscussionThread> findByCreatedBy(int userId) {
        return repository.findByCreatedBy(userId);
    }

    public Result<DiscussionThread> add(DiscussionThread thread) {
        Result<DiscussionThread> result = validate(thread);
        List<DiscussionThread> threads = repository.findAll();

        if (!result.isSuccess()) {
            return result;
        }
        result.setPayload(thread);
        repository.add(thread);  // Assuming there's an add method in the repository
        return result;
    }

    public Result<DiscussionThread> update(DiscussionThread thread) {
        Result<DiscussionThread> result = new Result<>();

        // Fetch the existing thread from the repository
        DiscussionThread existingThread = repository.findById(thread.getId());
        if (existingThread == null) {
            result.addMessage("Discussion Thread not found with id " + thread.getId(), ResultType.NOT_FOUND);
            return result;
        }

        // Check if the createdAt date is being edited
        if (!thread.getCreatedAt().isEqual(existingThread.getCreatedAt())) {
            result.addMessage("Cannot edit the creation date.", ResultType.INVALID);
        }

        // Validate the title
        if (thread.getTitle() == null || thread.getTitle().isBlank()) {
            result.addMessage("Title is required", ResultType.INVALID);
        }

        // Additional future date check
        if (thread.getCreatedAt().isAfter(LocalDate.now())) {
            result.addMessage("Cannot edit date to the future.", ResultType.INVALID);
        }

        if (!result.isSuccess()) {
            return result;
        }

        repository.update(thread);
        result.setPayload(thread);
        return result;
    }



    public Result<Void> deleteById(int id) {
        Result<Void> result = new Result<>();
        DiscussionThread thread = repository.findById(id);
        if (thread == null) {
            result.addMessage("Discussion Thread not found with id " + id, ResultType.NOT_FOUND);
            return result;
        }
        repository.deleteById(id);  // Assuming there's a deleteById method in the repository
        return result;
    }

    private Result<DiscussionThread> validate(DiscussionThread thread) {
        Result<DiscussionThread> result = new Result<>();
        if (thread.getTitle() == null || thread.getTitle().isBlank()) {
            result.addMessage("Title is required", ResultType.INVALID);
        }
        if (!thread.getCreatedAt().isEqual(LocalDate.now())) {
            result.addMessage("Creation is only permitted on the current date.", ResultType.INVALID);
        }

        return result;
    }
}
