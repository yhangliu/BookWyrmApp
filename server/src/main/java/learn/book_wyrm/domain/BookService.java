package learn.book_wyrm.domain;

import learn.book_wyrm.data.BookRepository;
import learn.book_wyrm.models.Book;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll() { return bookRepository.findAll();}

    public Book findById(int bookId) { return bookRepository.findById(bookId);}

    public Book findByIsbn(String isbn) { return bookRepository.findByIsbn(isbn);}

    public List<Book> findByTitle(String title) { return bookRepository.findByTitle(title);}

    public List<Book> findByAuthor(String author) { return bookRepository.findByAuthor(author);}

    public Result<Book> add(Book book) {
        Result<Book> result = validate(book);
        if (!result.isSuccess()) {
            return result;
        }

        if (book.getBookId() != 0) {
            result.addMessage("bookId cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }

        book = bookRepository.add(book);
        result.setPayload(book);
        return result;
    }

    /**
     * CREATE TABLE Books (
     *     id INT AUTO_INCREMENT PRIMARY KEY,
     *     title VARCHAR(255) NOT NULL,
     *     author VARCHAR(255) NOT NULL,
     *     isbn VARCHAR(13) NOT NULL UNIQUE,
     *     description TEXT
     * );
     */
    private Result<Book> validate(Book book) {
        Result<Book> result = new Result<>();
        if (book == null) {
            result.addMessage("book cannot be null", ResultType.INVALID);
            return result;
        }
        if (Validations.isNullOrBlank(book.getTitle())) {
            result.addMessage("title is required", ResultType.INVALID);
        }
        if (Validations.isNullOrBlank(book.getAuthor())) {
            result.addMessage("author is required", ResultType.INVALID);
        }
        if (Validations.isNullOrBlank(book.getIsbn())) {
            result.addMessage("isbn is required", ResultType.INVALID);
            return result;
        }
        Book bookWithIsbn = findByIsbn(book.getIsbn());
        if (bookWithIsbn != null) {
            result.addMessage("isbn is not unique", ResultType.INVALID);
        }
        return result;
    }
}
