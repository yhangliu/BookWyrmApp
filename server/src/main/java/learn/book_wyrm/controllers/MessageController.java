package learn.book_wyrm.controllers;

import learn.book_wyrm.domain.MessageService;
import learn.book_wyrm.domain.Result;
import learn.book_wyrm.models.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService service;

    public MessageController(MessageService service) {
        this.service = service;
    }

    @GetMapping
    public List<Message> findAll() {
        return service.findAll();
    }

    @GetMapping("/{messageId}")
    public ResponseEntity<Message> findById(@PathVariable int messageId) {
        Message message = service.findById(messageId);
        if (message != null) {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/thread/{threadId}")
    public List<Message> findByThreadId(@PathVariable int threadId) {
        return service.findByThreadId(threadId);
    }

    @GetMapping("/user/{userId}")
    public List<Message> findByUserId(@PathVariable int userId) {
        return service.findByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Message message) {
        Result<Message> result = service.add(message);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping("/{messageId}")
    public ResponseEntity<Object> update(@PathVariable int messageId, @RequestBody Message message) {
        if (messageId != message.getId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Message> result = service.update(message);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteById(@PathVariable int messageId) {
        if (service.deleteById(messageId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}