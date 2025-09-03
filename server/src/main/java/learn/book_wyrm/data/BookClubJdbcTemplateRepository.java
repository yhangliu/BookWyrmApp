package learn.book_wyrm.data;

import learn.book_wyrm.data.mappers.BookClubMapper;
import learn.book_wyrm.models.BookClub;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class BookClubJdbcTemplateRepository implements BookClubRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<BookClub> findAll() {
        final String sql = "select * "
                + "from BookClubs limit 1000;";
        return jdbcTemplate.query(sql, new BookClubMapper());
    }

    @Override
    public List<BookClub> findByGenre(String genre) {
        final String sql = "select * "
                + "from BookClubs "
                + "where genre = ? "
                + "limit 1000;";
        return jdbcTemplate.query(sql, new BookClubMapper(), genre);
    }

    @Override
    public List<BookClub> findByLocation(String location) {
        final String sql = "select * "
                + "from BookClubs "
                + "where location = ? "
                + "limit 1000;";
        return jdbcTemplate.query(sql, new BookClubMapper(), location);
    }

    @Override
    public List<BookClub> findByMemberId(int id) {
        final String sql = "select * "
                + "from BookClubs "
                + "join BookClubMembers on BookClubs.id = BookClubMembers.bookclub_id "
                + "where BookClubMembers.user_id = ? "
                + "limit 1000;";
        return jdbcTemplate.query(sql, new BookClubMapper(), id);
    }

    @Override
    public List<BookClub> findByClubId(int id) {
        final String sql = "select * "
                + "from BookClubs "
                + "where BookClubs.id = ? "
                + "limit 1000;";
        return jdbcTemplate.query(sql, new BookClubMapper(), id);
    }

    public BookClubJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean add(BookClub bookClub) {
        final String sql = "insert into BookClubs (name, description, genre, location,"
                + "online, featured_book, created_at, created_by) values "
                + "(?,?,?,?,?,?,?,?);";

        return jdbcTemplate.update(sql,
                bookClub.getName(),
                bookClub.getDescription(),
                bookClub.getGenre(),
                bookClub.getLocation(),
                bookClub.isOnline(),
                bookClub.getIsbn(),
                LocalDate.now(),
                bookClub.getCreatedById()) > 0;
    }

    @Override
    public boolean update(BookClub bookClub) {
        final String sql = "update BookClubs set " +
                "name = ?, " +
                "description = ?, " +
                "genre = ?, " +
                "location = ?, " +
                "online = ?, " +
                "featured_book = ? " +
                "where id = ?;";


        return jdbcTemplate.update(sql,
                bookClub.getName(),
                bookClub.getDescription(),
                bookClub.getGenre(),
                bookClub.getLocation(),
                bookClub.isOnline(),
                bookClub.getIsbn(),
                bookClub.getId()) > 0;
    }

    @Override
    public boolean deleteById(int id) {
        jdbcTemplate.update("delete from DiscussionThreads where bookclub_id = ?;", id);
        jdbcTemplate.update("delete from BookClubMembers where bookclub_id = ?;", id);
        return jdbcTemplate.update("delete from BookClubs where id = ?;", id) > 0;
    }
}