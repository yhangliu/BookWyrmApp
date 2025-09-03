package learn.book_wyrm.domain;

import learn.book_wyrm.data.BookClubRepository;
import learn.book_wyrm.data.ReviewRepository;
import learn.book_wyrm.models.BookClub;
import learn.book_wyrm.models.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class BookClubServiceTest {

    @Autowired
    BookClubService service;

    @MockBean
    BookClubRepository repository;

    @Test
    void shouldAdd() {
        BookClub bookClub = makeClub();
        when(repository.add(bookClub)).thenReturn(true);
        Result<BookClub> result = service.add(bookClub);
        assertEquals(ResultType.SUCCESS, result.getType());
    }

    @Test
    void shouldNotAddNull() {
        BookClub bookClub = null;
        when(repository.add(bookClub)).thenReturn(true);
        Result<BookClub> result = service.add(bookClub);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldAddWithoutName() {
        BookClub bookClub = makeClub();
        bookClub.setName("");
        when(repository.add(bookClub)).thenReturn(true);
        Result<BookClub> result = service.add(bookClub);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldUpdate() {
        BookClub bookClub = makeClub();
        bookClub.setId(5);
        when(repository.update(bookClub)).thenReturn(true);
        Result<BookClub> result = service.update(bookClub);
        assertEquals(ResultType.SUCCESS, result.getType());
    }

    @Test
    void shouldNotUpdateWithoutName() {
        BookClub bookClub = makeClub();
        bookClub.setName("");
        when(repository.update(bookClub)).thenReturn(true);
        Result<BookClub> result = service.update(bookClub);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotUpdateWithoutId() {
        BookClub bookClub = makeClub();
        Result<BookClub> result = service.update(bookClub);
        assertEquals(ResultType.INVALID, result.getType());
    }

    BookClub makeClub(){
        BookClub bookClub = new BookClub();
        bookClub.setName("Harry Potter Pals");
        bookClub.setIsbn("Testing");
        return bookClub;
    }
}