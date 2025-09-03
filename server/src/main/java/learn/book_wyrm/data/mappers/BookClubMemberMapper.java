package learn.book_wyrm.data.mappers;

import learn.book_wyrm.models.BookClubMember;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookClubMemberMapper implements RowMapper<BookClubMember> {
    @Override
    public BookClubMember mapRow(ResultSet resultSet, int i) throws SQLException {
        BookClubMember member = new BookClubMember();
        member.setId(resultSet.getInt("id"));
        member.setUserId(resultSet.getInt("user_id"));
        member.setBookClubId(resultSet.getInt("bookclub_id"));
        if (resultSet.getDate("joined_at") != null) {
            member.setDate(resultSet.getDate("joined_at").toLocalDate());
        }
        return member;
    }
}
