

package learn.book_wyrm.data;

import learn.book_wyrm.data.mappers.AppUserMapper;
import learn.book_wyrm.models.AppUser;
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
public class AppUserJdbcTemplateRepository implements AppUserRepository {

    private final JdbcTemplate jdbcTemplate;

    public AppUserJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<AppUser> findAll() {
        String sql = "SELECT id, username, password, email, is_admin, created_at FROM Users";
        return jdbcTemplate.query(sql, new AppUserMapper());
    }

    @Override
    public AppUser findById(int id) {
        String sql = "SELECT id, username, password, email, is_admin, created_at FROM Users WHERE id = ?";
        return jdbcTemplate.query(sql, new AppUserMapper(), id).stream().findFirst().orElse(null);
    }

    @Override
    public AppUser findByUsername(String username) {
        String sql = "SELECT id, username, password, email, is_admin, created_at FROM Users WHERE username = ?";
        return jdbcTemplate.query(sql, new AppUserMapper(), username).stream().findFirst().orElse(null);
    }

    @Override
    public List<AppUser> findByAdminStatus(boolean isAdmin) {
        String sql = "SELECT id, username, password, email, is_admin, created_at FROM Users WHERE is_admin = ?";
        return jdbcTemplate.query(sql, new AppUserMapper(), isAdmin);
    }
    @Override
    public AppUser add(AppUser appUser) {
        final String sql = "INSERT INTO Users (username, password, email, is_admin, created_at) VALUES (?, ?, ?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, appUser.getUsername());
            ps.setString(2, appUser.getEmail());
            ps.setString(3, appUser.getPassword());
            ps.setBoolean(4, appUser.getIsAdmin());
            ps.setDate(5, appUser.getCreatedDate() != null ? Date.valueOf(appUser.getCreatedDate()) : new Date(System.currentTimeMillis()));
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        appUser.setAppUserId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return appUser;
    }

    @Override
    public boolean update(AppUser appUser) {
        String sql = "UPDATE Users SET username = ?, email = ?, password = ?, is_admin = ?, created_at = ? WHERE id = ?";
        return jdbcTemplate.update(sql, appUser.getUsername(), appUser.getEmail(), appUser.getPassword(), appUser.getIsAdmin(), appUser.getCreatedDate(), appUser.getAppUserId()) > 0;
    }

    @Override
    public boolean deleteById(int id) {
        String sqlBookClubs = "UPDATE BookClubs SET created_by = NULL WHERE created_by = ?";
        jdbcTemplate.update(sqlBookClubs, id);

        String sqlDiscussion = "UPDATE DiscussionThreads SET created_by = NULL WHERE created_by = ?";
        jdbcTemplate.update(sqlDiscussion, id);

        String sqlMessages = "UPDATE Messages SET user_id = NULL WHERE user_id = ?";
        jdbcTemplate.update(sqlMessages, id);

        String sqlBookClubMembers = "DELETE FROM BookClubMembers WHERE user_id = ?";
        jdbcTemplate.update(sqlBookClubMembers, id);

        String sqlUsers = "DELETE FROM Users WHERE id = ?";
        return jdbcTemplate.update(sqlUsers, id) > 0;
    }
}