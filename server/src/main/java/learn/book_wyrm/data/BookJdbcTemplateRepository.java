
package learn.book_wyrm.data;

import learn.book_wyrm.data.mappers.BookMapper;
import learn.book_wyrm.models.Book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class BookJdbcTemplateRepository implements BookRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Book> findAll() {
        String sql = "SELECT * FROM Books";
        return jdbcTemplate.query(sql, new BookMapper());
    }

    @Override
    public List<Book> findByTitle(String title) {
        String sql = "SELECT * FROM Books WHERE title LIKE ?";
        String searchPattern = "%" + title + "%";
        return jdbcTemplate.query(sql, new BookMapper(), searchPattern);
    }

    @Override
    public List<Book> findByAuthor(String author) {
        String sql = "SELECT * FROM Books WHERE author LIKE ?";
        String searchPattern = "%" + author + "%";
        return jdbcTemplate.query(sql, new BookMapper(), searchPattern);
    }

    @Override
    public Book findById(int id) {
        String sql = "SELECT * FROM Books WHERE id = ?";
        return jdbcTemplate.query(sql, new BookMapper(), id).stream().findFirst().orElse(null);
    }

    @Override
    public Book findByIsbn(String isbn) {
        String sql = "SELECT * FROM Books WHERE isbn = ?";
        return jdbcTemplate.query(sql, new BookMapper(), isbn).stream().findFirst().orElse(null);
    }

    public Book add(Book book) {
        final String sql = "insert into Books (title, author, isbn, description) values (?,?,?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setString(4, book.getDescription());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        book.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return book;
    }
}