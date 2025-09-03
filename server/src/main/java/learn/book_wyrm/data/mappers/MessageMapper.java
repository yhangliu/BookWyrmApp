package learn.book_wyrm.data.mappers;

import learn.book_wyrm.models.Message;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageMapper implements RowMapper<Message> {
    @Override
    public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
        Message message = new Message();
        message.setId(rs.getInt("id"));
        message.setThreadId(rs.getInt("thread_id"));
        message.setUserId(rs.getInt("user_id"));
        message.setMessage(rs.getString("message"));
        message.setCreatedAt(rs.getDate("created_at").toLocalDate());
        return message;
    }
}