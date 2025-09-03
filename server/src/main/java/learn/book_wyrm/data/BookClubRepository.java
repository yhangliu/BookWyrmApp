package learn.book_wyrm.data;

import learn.book_wyrm.domain.Result;
import learn.book_wyrm.domain.ResultType;
import learn.book_wyrm.domain.Validations;
import learn.book_wyrm.models.BookClub;
import learn.book_wyrm.models.BookClub;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BookClubRepository {

    List<BookClub> findAll();
    List<BookClub> findByLocation(String location);
    List<BookClub> findByGenre(String genre);
    List<BookClub> findByMemberId(int id);
    List<BookClub> findByClubId(int id);
    boolean add(BookClub bookClub);

    boolean update(BookClub bookClub);

    boolean deleteById(int id);

}
