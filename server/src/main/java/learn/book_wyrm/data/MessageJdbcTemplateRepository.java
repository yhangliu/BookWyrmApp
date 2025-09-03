package learn.book_wyrm.data;

import learn.book_wyrm.data.MessageRepository;
import learn.book_wyrm.data.mappers.MessageMapper;
import learn.book_wyrm.models.Message;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class MessageJdbcTemplateRepository implements MessageRepository {

    private final JdbcTemplate jdbcTemplate;

    public MessageJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Message> findAll() {
        final String sql = "SELECT id, thread_id, user_id, message, created_at FROM Messages;";
        return jdbcTemplate.query(sql, new MessageMapper());
    }

    @Override
    public Message findById(int id) {
        final String sql = "SELECT id, thread_id, user_id, message, created_at FROM Messages WHERE id = ?;";
        return jdbcTemplate.query(sql, new MessageMapper(), id).stream().findFirst().orElse(null);
    }

    @Override
    public List<Message> findByThreadId(int threadId) {
        final String sql = "SELECT id, thread_id, user_id, message, created_at FROM Messages WHERE thread_id = ?;";
        return jdbcTemplate.query(sql, new MessageMapper(), threadId);
    }

    @Override
    public List<Message> findByUserId(int userId) {
        final String sql = "SELECT id, thread_id, user_id, message, created_at FROM Messages WHERE user_id = ?;";
        return jdbcTemplate.query(sql, new MessageMapper(), userId);
    }

    @Override
    public Message add(Message message) {
        final String sql = "INSERT INTO Messages (thread_id, user_id, message, created_at) VALUES (?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, message.getThreadId());
            ps.setInt(2, message.getUserId());
            ps.setString(3, message.getMessage());
            ps.setDate(4, java.sql.Date.valueOf(message.getCreatedAt()));
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        message.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return message;
    }

    @Override
    public boolean update(Message message) {
        final String sql = "UPDATE Messages SET thread_id = ?, user_id = ?, message = ?, created_at = ? WHERE id = ?;";
        return jdbcTemplate.update(sql,
                message.getThreadId(),
                message.getUserId(),
                message.getMessage(),
                java.sql.Date.valueOf(message.getCreatedAt()),
                message.getId()) > 0;
    }

    @Override
    public boolean deleteById(int id) {
        final String sql = "DELETE FROM Messages WHERE id = ?;";
        return jdbcTemplate.update(sql, id) > 0;
    }
}