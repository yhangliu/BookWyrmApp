package learn.book_wyrm.controllers;


import learn.book_wyrm.domain.DiscussionThreadService;
import learn.book_wyrm.domain.Result;
import learn.book_wyrm.models.DiscussionThread;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/threads")
public class DiscussionThreadsController {

    private final DiscussionThreadService service;

    public DiscussionThreadsController(DiscussionThreadService service) {
        this.service = service;
    }

    @GetMapping
    public List<DiscussionThread> findAll() {
        return service.findAll();
    }

    @GetMapping("/club/{clubId}")
    public List<DiscussionThread> findByClubId(@PathVariable int clubId) {
        return service.findByClubId(clubId);
    }

    @GetMapping("/user/{userId}")
    public List<DiscussionThread> findCreatedBy(@PathVariable int userId) {
        return service.findByCreatedBy(userId);
    }

    @PutMapping("/{threadId}")
    public ResponseEntity<?> update(@PathVariable int threadId, @RequestBody DiscussionThread thread) {
        if (threadId != thread.getId())
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        Result<DiscussionThread> result = service.update(thread);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.OK);
        }
        return ErrorResponse.build(result);

    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody DiscussionThread thread) {

        Result<DiscussionThread> result = service.add(thread);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{threadId}")
    public ResponseEntity<?> deleteThread(@PathVariable int threadId) {
        Result<Void> result = service.deleteById(threadId);

        if (result.isSuccess())
            return new ResponseEntity<>(HttpStatus.OK);
        return ErrorResponse.build(result);

    }


}
