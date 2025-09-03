package learn.book_wyrm.data.mappers;

import learn.book_wyrm.models.BookClub;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookClubMapper implements RowMapper<BookClub> {

    @Override
    public BookClub mapRow(ResultSet resultSet, int i) throws SQLException {
        BookClub bookClub = new BookClub();
        bookClub.setId(resultSet.getInt("id"));
        bookClub.setName(resultSet.getString("name"));
        bookClub.setDescription(resultSet.getString("description"));
        bookClub.setGenre(resultSet.getString("genre"));
        bookClub.setOnline(resultSet.getBoolean("online"));
        bookClub.setLocation(resultSet.getString("location"));
        bookClub.setIsbn(resultSet.getString("featured_book"));
        bookClub.setDate(resultSet.getDate("created_at"));
        bookClub.setCreatedById(resultSet.getInt("created_by"));
        return bookClub;
    }

}
