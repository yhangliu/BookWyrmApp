package learn.book_wyrm.controllers;

import learn.book_wyrm.domain.BookClubMemberService;
import learn.book_wyrm.domain.Result;
import learn.book_wyrm.models.BookClubMember;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/clubMember")
public class BookClubMemberController {

    private final BookClubMemberService service;


    public BookClubMemberController(BookClubMemberService service) {
        this.service = service;
    }

    @GetMapping
    public List<BookClubMember> findAll() {
        return service.findAll();
    }

    @GetMapping("/{clubId}")
    public List<BookClubMember> findByClub(@PathVariable int clubId) {
        return service.findByClub(clubId);
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody BookClubMember member) {

        Result<BookClubMember> result = service.add(member);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);


    }

    @DeleteMapping("/{userId}/{clubId}")
    public ResponseEntity<?> deleteMember(@PathVariable int userId,@PathVariable int clubId) {

        Result<Void> result = service.deleteMember(userId, clubId);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.ACCEPTED);
        }
        return ErrorResponse.build(result);

    }

}
