package learn.book_wyrm.domain;

import learn.book_wyrm.data.BookRepository;
import learn.book_wyrm.models.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class BookServiceTest {

    @Autowired
    BookService service;

    @MockBean
    BookRepository bookRepository;

    @Test
    void shouldAddBook() {
        Book book = new Book(0, "New Book", "Author", "1234567890123", "A description");
        Book mockOut = new Book(1, "New Book", "Author", "1234567890123", "A description");

        when(bookRepository.add(book)).thenReturn(mockOut);

        Result<Book> actual = service.add(book);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mockOut, actual.getPayload());
    }

    @Test
    void shouldNotAddWhenInvalid() {
        Book book = new Book(1, "New Book", "Author", "1234567890123", "A description");

        Result<Book> actual = service.add(book);
        assertEquals(ResultType.INVALID, actual.getType());

        book.setBookId(0);
        book.setTitle(null);
        actual = service.add(book);
        assertEquals(ResultType.INVALID, actual.getType());

        book.setTitle("New Book");
        book.setAuthor(null);
        actual = service.add(book);
        assertEquals(ResultType.INVALID, actual.getType());

        book.setAuthor("Author");
        book.setIsbn(null);
        actual = service.add(book);
        assertEquals(ResultType.INVALID, actual.getType());

        book.setIsbn("1234567890123");
        when(bookRepository.findByIsbn(book.getIsbn())).thenReturn(new Book());
        actual = service.add(book);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldFindAll() {
        List<Book> books = Arrays.asList(
                new Book(1, "Book 1", "Author 1", "1111111111111", "Description 1"),
                new Book(2, "Book 2", "Author 2", "2222222222222", "Description 2")
        );

        when(bookRepository.findAll()).thenReturn(books);

        List<Book> actual = service.findAll();
        assertEquals(books, actual);
    }

    @Test
    void shouldFindById() {
        Book book = new Book(1, "Book 1", "Author 1", "1111111111111", "Description 1");

        when(bookRepository.findById(1)).thenReturn(book);

        Book actual = service.findById(1);
        assertEquals(book, actual);
    }

    @Test
    void shouldFindByIsbn() {
        Book book = new Book(1, "Book 1", "Author 1", "1111111111111", "Description 1");

        when(bookRepository.findByIsbn("1111111111111")).thenReturn(book);

        Book actual = service.findByIsbn("1111111111111");
        assertEquals(book, actual);
    }

    @Test
    void shouldFindByTitle() {
        List<Book> books = Arrays.asList(
                new Book(1, "Book 1", "Author 1", "1111111111111", "Description 1"),
                new Book(2, "Book 1", "Author 2", "2222222222222", "Description 2")
        );

        when(bookRepository.findByTitle("Book 1")).thenReturn(books);

        List<Book> actual = service.findByTitle("Book 1");
        assertEquals(books, actual);
    }

    @Test
    void shouldFindByAuthor() {
        List<Book> books = Arrays.asList(
                new Book(1, "Book 1", "Author 1", "1111111111111", "Description 1"),
                new Book(2, "Book 2", "Author 1", "2222222222222", "Description 2")
        );

        when(bookRepository.findByAuthor("Author 1")).thenReturn(books);

        List<Book> actual = service.findByAuthor("Author 1");
        assertEquals(books, actual);
    }

}
