package learn.book_wyrm.data.mappers;

import learn.book_wyrm.models.AppUser;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class AppUserMapper implements RowMapper<AppUser> {
    @Override
    public AppUser mapRow(ResultSet rs, int rowNum) throws SQLException {
        Date date = rs.getDate("created_at");
        LocalDate localDate = null;
        if (date != null) {
            localDate = date.toLocalDate();
        }

        return new AppUser(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("email"),
                rs.getBoolean("is_admin"),
                localDate
        );
    }
}

