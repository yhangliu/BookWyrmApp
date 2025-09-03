package learn.book_wyrm.data.mappers;

import learn.book_wyrm.models.DiscussionThread;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DiscussionThreadMapper implements RowMapper<DiscussionThread> {
    @Override
    public DiscussionThread mapRow(ResultSet rs, int rowNum) throws SQLException {
        DiscussionThread thread = new DiscussionThread();
        thread.setId(rs.getInt("id"));
        thread.setBookclubId(rs.getInt("bookclub_id"));
        thread.setTitle(rs.getString("title"));
        thread.setCreatedBy(rs.getInt("created_by"));
        thread.setCreatedAt(rs.getDate("created_at").toLocalDate());
        return thread;
    }
}