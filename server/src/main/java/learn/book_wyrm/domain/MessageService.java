package learn.book_wyrm.domain;


import learn.book_wyrm.data.AppUserRepository;
import learn.book_wyrm.data.DiscussionThreadRepository;
import learn.book_wyrm.data.MessageRepository;
import learn.book_wyrm.models.AppUser;
import learn.book_wyrm.models.DiscussionThread;
import learn.book_wyrm.models.Message;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository repository;
    private final AppUserRepository userRepository;
    private final DiscussionThreadRepository threadRepository;

    public MessageService(MessageRepository repository, AppUserRepository userRepository, DiscussionThreadRepository threadRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.threadRepository = threadRepository;
    }

    public List<Message> findAll(){
        return repository.findAll();
    }

    public Message findById(int id){
        return repository.findById(id);
    }

    public List<Message> findByThreadId(int id){
        return repository.findByThreadId(id);
    }

    public List<Message> findByUserId(int id){
        return repository.findByUserId(id);
    }

    public boolean deleteById(int id){
        return repository.deleteById(id);
    }

    public Result<Message> add(Message message){
        Result<Message> result = validate(message);

        if(!result.isSuccess()) {
            return result;
        }

        if (message.getId() != 0) {
            result.addMessage("Id cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }

        message = repository.add(message);
        result.setPayload(message);

        return result;
    }

    public Result<Message> update(Message message){
        Result<Message> result = validate(message);

        if(!result.isSuccess()) {
            return result;
        }

        if (message.getId() <= 0) {
            result.addMessage("Id must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        if(!repository.update(message)){
            String msg = String.format("Message not found");
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    private Result<Message> validate(Message message) {
        Result<Message> result = new Result<>();
        if (message == null) {
            result.addMessage("Message cannot be null", ResultType.INVALID);
            return result;
        }

        if (Validations.isNullOrBlank(message.getMessage())) {
            result.addMessage("Message is required", ResultType.INVALID);
        }

        List<AppUser> users = userRepository.findAll();
        boolean userExists = users.stream().anyMatch(user -> user.getAppUserId() == message.getUserId());
        if (!userExists) {
            result.addMessage("Invalid user ID", ResultType.INVALID);
        }

        List<DiscussionThread> threads = threadRepository.findAll();
        boolean threadExists = threads.stream().anyMatch(thread -> thread.getId() == message.getThreadId());
        if (!threadExists) {
            result.addMessage("Invalid thread ID", ResultType.INVALID);
        }

        return result;
    }
}