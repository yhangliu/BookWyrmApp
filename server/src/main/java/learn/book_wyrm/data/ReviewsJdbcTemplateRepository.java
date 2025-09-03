package learn.book_wyrm.data;

import learn.book_wyrm.data.mappers.BookClubMapper;
import learn.book_wyrm.data.mappers.ReviewMapper;
import learn.book_wyrm.models.Review;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class ReviewsJdbcTemplateRepository implements ReviewRepository{

    private final JdbcTemplate jdbcTemplate;

    public ReviewsJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Review> findAll() {
        final String sql = "select * "
                + "from Reviews limit 1000;";
        return jdbcTemplate.query(sql, new ReviewMapper());
    }

    @Override
    public List<Review> findByISBN(String isbn) {
        final String sql = "select * "
                + "from Reviews "
                + "where isbn = ? "
                + "limit 1000;";
        return jdbcTemplate.query(sql, new ReviewMapper(), isbn);
    }

    @Override
    public boolean add(Review review) {
        final String sql = "insert into Reviews (user_id,isbn, rating, review_text, created_at) values "
                + "(?,?,?,?,?);";
        return jdbcTemplate.update(sql,
                review.getCreatedById(),
                review.getIsbn(),
                review.getRating(),
                review.getReviewText(),
                LocalDate.now()) > 0;
    }

    @Override
    public boolean deleteById(int id) {
        return jdbcTemplate.update("delete from Reviews where id = ?;", id) > 0;
    }
}