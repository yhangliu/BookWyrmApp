package learn.book_wyrm.data;

import learn.book_wyrm.models.Book;
import learn.book_wyrm.models.BookClubMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookClubMemberJdbcTemplateRepositoryTest {

    @Autowired
    BookClubMemberJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void findAll() {
        List<BookClubMember> all = repository.findAll();
        assertFalse(all.isEmpty());
    }

    @Test
    void shouldFindByBookClubId() {

        List<BookClubMember> member = repository.findByBookClubId(3);
        assertFalse(member.isEmpty());
    }@Test
    void shouldNotFindByBookClubId() {

        List<BookClubMember> member = repository.findByBookClubId(999);
        assertTrue(member.isEmpty());
    }

    @Test
    void add() {
        BookClubMember newMember = new BookClubMember();
        newMember.setBookClubId(3);
        newMember.setUserId(2);
        newMember.setDate(LocalDate.now());
        // Implement your add test here
        List<BookClubMember> all = repository.findAll();
        int current = all.size();
        repository.add(newMember);
        List<BookClubMember> expected = repository.findAll();
        System.out.println(expected.size() + " " + all.size());
        assertTrue(expected.size() > current);
    }


    @Test
    void deleteMember() {

        assertTrue(repository.deleteMember(2, 4));
        assertFalse(repository.deleteMember(2, 4));

    }

    @Test
    void shouldNotDeleteNonExisting() {

        assertFalse(repository.deleteMember(12, 12));
    }
}
