package learn.book_wyrm.data;

import learn.book_wyrm.models.BookClub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookClubJdbcTemplateRepositoryTest {

    @Autowired
    BookClubJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindAll() {
        List<BookClub> bookClubs = repository.findAll();
        assertNotNull(bookClubs);

        assertTrue(bookClubs.size()>2);
    }

    @Test
    void findByGenre() {
        List<BookClub> bookClubs = repository.findByGenre("Fantasy");
        assertNotNull(bookClubs);

        assertTrue(bookClubs.size()>0);
    }

    @Test
    void findByLocation() {

        List<BookClub> bookClubs = repository.findByLocation("Seattle, WA");
        assertNotNull(bookClubs);

        assertTrue(bookClubs.size()>0);
    }

    @Test
    void add() {
        BookClub bookClub = new BookClub();
        bookClub.setName("Twilight Lovers");
        bookClub.setIsbn("9780316769488");
        bookClub.setCreatedById(1);
        repository.add(bookClub);
        List<BookClub> bookClubs = repository.findAll();
        assertTrue(bookClubs.size()>4);
    }

    @Test
    void update() {
        BookClub bookClub = new BookClub();
        bookClub.setName("Twilight Lovers");
        bookClub.setIsbn("9780316769488");
        bookClub.setCreatedById(1);
        bookClub.setId(2);
        repository.update(bookClub);
        List<BookClub> bookClubs = repository.findAll();
        assertTrue(bookClubs.get(1).getName().equals("Twilight Lovers"));
    }

    @Test
    void deleteById() {

        assertTrue(repository.deleteById(1));

    }
}