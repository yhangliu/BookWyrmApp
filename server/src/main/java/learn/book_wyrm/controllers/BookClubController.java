package learn.book_wyrm.controllers;


import learn.book_wyrm.domain.BookClubService;
import learn.book_wyrm.domain.Result;
import learn.book_wyrm.models.BookClub;
import learn.book_wyrm.models.DiscussionThread;
import learn.book_wyrm.models.Review;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/bookClub")
public class BookClubController {

    private final BookClubService service;

    public BookClubController(BookClubService service) {
        this.service = service;
    }


    @GetMapping
    public List<BookClub> findAll() {
        return service.findAll();
    }

    @GetMapping("/location/{location}")
    public List<BookClub> findByLocation(@PathVariable String location){
        return service.findByLocation(location);
    }

    @GetMapping("/genre/{genre}")
    public List<BookClub> findByGenre(@PathVariable String genre){
        return service.findByGenre(genre);
    }

    @GetMapping("/user/{id}")
    public List<BookClub> findByMemberId(@PathVariable int id){
        return service.findByMemberId(id);
    }

    @GetMapping("/{id}")
    public List<BookClub> findByClubId(@PathVariable int id){
        return service.findByClubId(id);
    }



    @PostMapping
    public ResponseEntity<?> add(@RequestBody BookClub bookClub) {

        Result<BookClub> result = service.add(bookClub);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody BookClub bookClub) {
        if (id != bookClub.getId())
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        Result<BookClub> result = service.update(bookClub);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.OK);
        }
        return ErrorResponse.build(result);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable int id) {

        if (service.deleteById(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
