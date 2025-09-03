package learn.book_wyrm.data.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import learn.book_wyrm.models.Book;
import org.springframework.jdbc.core.RowMapper;

public class BookMapper implements RowMapper<Book> {
    @Override
    public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
        Book book = new Book();
        book.setId(rs.getInt("id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setIsbn(rs.getString("isbn"));
        book.setDescription(rs.getString("description"));
        return book;
    }
}