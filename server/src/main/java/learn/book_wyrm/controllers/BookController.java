package learn.book_wyrm.controllers;

import learn.book_wyrm.domain.BookService;
import learn.book_wyrm.domain.Result;
import learn.book_wyrm.models.Book;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/book")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @GetMapping
    public List<Book> findAll() {
        return service.findAll();
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<Book> findById(@PathVariable int bookId) {
        Book book = service.findById(bookId);
        if (book != null) {
            return new ResponseEntity<>(book, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Book> findByIsbn(@PathVariable String isbn) {
        Book book = service.findByIsbn(isbn);
        if (book != null) {
            return new ResponseEntity<>(book, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/title/{title}")
    public List<Book> findByTitle(@PathVariable String title) {
        return service.findByTitle(title);
    }

    @GetMapping("/author/{author}")
    public List<Book> findByAuthor(@PathVariable String author) {
        return service.findByAuthor(author);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Book book) {
        Result<Book> result = service.add(book);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

}