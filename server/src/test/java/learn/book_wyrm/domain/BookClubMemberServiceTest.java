package learn.book_wyrm.domain;

import learn.book_wyrm.data.BookClubMemberRepository;
import learn.book_wyrm.models.BookClubMember;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

@SpringBootTest
class BookClubMemberServiceTest {

    @Autowired
    BookClubMemberService service;

    @MockBean
    BookClubMemberRepository repository;


    @Test
    void shouldFindAll() {
        BookClubMember member = new BookClubMember();
        member.setUserId(1);
        member.setBookClubId(1);
        member.setDate(LocalDate.now());

        when(repository.findAll()).thenReturn(List.of(member));

        List<BookClubMember> result = service.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getUserId());
    }
    @Test
    void shouldFindByBookClubId(){
        BookClubMember member= new BookClubMember();
        member.setUserId(1);
        member.setBookClubId(1);
        member.setDate(LocalDate.now());

        when(repository.findByBookClubId(1)).thenReturn(List.of(member));

        List<BookClubMember> membersByClub = service.findByClub(1);
        assertNotNull(membersByClub);
        assertFalse(membersByClub.isEmpty());

    }

    @Test
    void shouldAddWhenValid() {
        BookClubMember member = new BookClubMember();
        member.setUserId(1);
        member.setBookClubId(1);
        member.setDate(LocalDate.of(2023,3,15));

        when(repository.add(any(BookClubMember.class))).thenReturn(member);

        Result<BookClubMember> result = service.add(member);
        System.out.println(result.getMessages());
        assertTrue(result.isSuccess());
        assertNotNull(result.getPayload());
        assertEquals(1, result.getPayload().getUserId());

        verify(repository).add(any(BookClubMember.class));
    }

    @Test
    void shouldNotAddWhenInvalid() {
        BookClubMember member = new BookClubMember();
        member.setUserId(0); // Invalid user ID
        member.setBookClubId(1);
        member.setDate(LocalDate.now());
        Result<BookClubMember> result = service.add(member);

        assertFalse(result.isSuccess());
        assertNull(result.getPayload());
        assertEquals(1, result.getMessages().size());
        assertEquals("User ID must be valid.", result.getMessages().get(0));
    }

    @Test
    void shouldDeleteWhenValid() {
        when(repository.findAll()).thenReturn(List.of(
                createBookClubMember(1, 1,LocalDate.now()),
                createBookClubMember(2, 2,LocalDate.now())
        ));

        when(repository.deleteMember(1, 1)).thenReturn(true);

        Result<Void> result = service.deleteMember(1, 1);

        assertTrue(result.isSuccess());
        verify(repository).deleteMember(1, 1);
    }

    @Test
    void shouldNotDeleteWhenInvalid() {
        when(repository.findAll()).thenReturn(List.of(
                createBookClubMember(1, 1,LocalDate.now()),
                createBookClubMember(2, 2,LocalDate.now())
        ));

        Result<Void> result = service.deleteMember(3, 3); // Member does not exist

        assertFalse(result.isSuccess());
        assertEquals(1, result.getMessages().size());
        assertEquals("Member does not exist.", result.getMessages().get(0));

        verify(repository, never()).deleteMember(anyInt(), anyInt());
    }

    private BookClubMember createBookClubMember(int userId, int bookClubId, LocalDate date) {
        BookClubMember member = new BookClubMember();
        member.setUserId(userId);
        member.setBookClubId(bookClubId);
        member.setDate(date);
        return member;
    }
}
