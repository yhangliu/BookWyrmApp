package learn.book_wyrm.data;

import learn.book_wyrm.models.BookClubMember;
import java.util.List;
public interface BookClubMemberRepository {
    List<BookClubMember> findAll();

    List<BookClubMember> findByBookClubId(int bookClubId);

    BookClubMember add(BookClubMember member);
    boolean deleteMember(int userId, int bookClubId);
}
