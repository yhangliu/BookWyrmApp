package learn.book_wyrm.data.mappers;

import learn.book_wyrm.models.BookClub;
import learn.book_wyrm.models.Review;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;


public class ReviewMapper implements RowMapper<Review>{

    @Override
    public Review mapRow(ResultSet resultSet, int i) throws SQLException {
        Review review = new Review();
        review.setId(resultSet.getInt("id"));
        review.setCreatedById(resultSet.getInt("user_id"));
        review.setIsbn(resultSet.getString("isbn"));
        review.setRating(resultSet.getInt("rating"));
        review.setReviewText(resultSet.getString("review_text"));
        review.setDate(resultSet.getDate("created_at"));


        return review;
    }
}