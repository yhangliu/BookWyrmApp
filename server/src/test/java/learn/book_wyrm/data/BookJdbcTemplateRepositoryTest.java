package learn.book_wyrm.data;

import learn.book_wyrm.models.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookJdbcTemplateRepositoryTest {

    @Autowired
    BookJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldAdd() {
        Book book = makeBook();
        Book result = repository.add(book);
        assertNotNull(result);
        assertTrue(result.getId() > 0);
    }
    @Test
    void shouldFindAll() {
        List<Book> books = repository.findAll();
        assertNotNull(books);
        assertFalse(books.isEmpty());
    }

    @Test
    void shouldFindById() {
        Book book = repository.findById(1);
        assertNotNull(book);
        assertEquals(1, book.getId());

        book = repository.findById(999); // assuming there is no book with id 999
        assertNull(book);
    }

    @Test
    void shouldFindByTitle() {
        List<Book> books = repository.findByTitle("Moby");
        assertNotNull(books);
        assertFalse(books.isEmpty());

        books = repository.findByTitle("NonExistentTitle");
        assertNotNull(books);
        assertTrue(books.isEmpty());
    }

    @Test
    void shouldFindByAuthor() {
        List<Book> books = repository.findByAuthor("Jane Austen");
        assertNotNull(books);
        assertFalse(books.isEmpty());

        books = repository.findByAuthor("NonExistentAuthor");
        assertNotNull(books);
        assertTrue(books.isEmpty());
    }

    @Test
    void shouldFindByIsbn() {
        Book book = repository.findByIsbn("9780141040349");
        assertNotNull(book);
        assertEquals("9780141040349", book.getIsbn());

        book = repository.findByIsbn("9999999999999");
        assertNull(book);
    }

    Book makeBook() {
        Book book = new Book();
        book.setTitle("Sample Title");
        book.setAuthor("Sample Author");
        book.setIsbn("1234567890123");
        book.setDescription("Sample Description");
        return book;
    }
}