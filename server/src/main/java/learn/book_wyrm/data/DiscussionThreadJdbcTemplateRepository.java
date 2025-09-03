package learn.book_wyrm.data;

import learn.book_wyrm.data.mappers.DiscussionThreadMapper;
import learn.book_wyrm.models.DiscussionThread;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class DiscussionThreadJdbcTemplateRepository implements DiscussionThreadRepository {

    private final JdbcTemplate jdbcTemplate;

    public DiscussionThreadJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<DiscussionThread> findAll() {
        final String sql = "SELECT id, bookclub_id, title, created_by, created_at FROM DiscussionThreads;";
        return jdbcTemplate.query(sql, new DiscussionThreadMapper());
    }

    public DiscussionThread findById(int id) {
        final String sql = "SELECT id, bookclub_id, title, created_by, created_at FROM DiscussionThreads WHERE id = ?;";
        return jdbcTemplate.query(sql, new DiscussionThreadMapper(), id).stream()
                .findFirst().orElse(null);
    }

    public List<DiscussionThread> findByBookclubId(int bookclubId) {
        final String sql = "SELECT id, bookclub_id, title, created_by, created_at FROM DiscussionThreads WHERE bookclub_id = ?;";
        return jdbcTemplate.query(sql, new DiscussionThreadMapper(), bookclubId);
    }

    public List<DiscussionThread> findByCreatedBy(int createdBy) {
        final String sql = "SELECT id, bookclub_id, title, created_by, created_at FROM DiscussionThreads WHERE created_by = ?;";
        return jdbcTemplate.query(sql, new DiscussionThreadMapper(), createdBy);
    }

    public DiscussionThread add(DiscussionThread thread) {
        final String sql = "INSERT INTO DiscussionThreads (bookclub_id, title, created_by, created_at) VALUES (?, ?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, thread.getBookclubId());
            ps.setString(2, thread.getTitle());
            ps.setInt(3, thread.getCreatedBy());
            ps.setDate(4, Date.valueOf(thread.getCreatedAt()));
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        thread.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return thread;
    }

    public boolean update(DiscussionThread thread) {
        final String sql = "UPDATE DiscussionThreads SET bookclub_id = ?, title = ?, created_by = ?, created_at = ? WHERE id = ?;";
        return jdbcTemplate.update(sql,
                thread.getBookclubId(),
                thread.getTitle(),
                thread.getCreatedBy(),
                Date.valueOf(thread.getCreatedAt()),
                thread.getId()) > 0;
    }

    @Override
    public boolean deleteById(int id) {
        // Delete messages related to the discussion thread
        String deleteMessagesSql = "DELETE FROM Messages WHERE thread_id = ?";
        jdbcTemplate.update(deleteMessagesSql, id);

        // Delete the discussion thread
        String deleteThreadSql = "DELETE FROM DiscussionThreads WHERE id = ?";
        return jdbcTemplate.update(deleteThreadSql, id) > 0;
    }
}