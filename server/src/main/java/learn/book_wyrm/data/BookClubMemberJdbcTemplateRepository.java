package learn.book_wyrm.data;

import learn.book_wyrm.data.mappers.BookClubMemberMapper;
import learn.book_wyrm.models.BookClubMember;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
@Repository
public class BookClubMemberJdbcTemplateRepository implements BookClubMemberRepository {

    final JdbcTemplate jdbcTemplate;

    public BookClubMemberJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<BookClubMember> findAll() {
        final String sql = "SELECT id, user_id, bookclub_id, joined_at FROM BookClubMembers;";
        return jdbcTemplate.query(sql, new BookClubMemberMapper());
    }
    @Override
    public List<BookClubMember> findByBookClubId(int bookClubId) {
        final String sql = "SELECT id, user_id, bookclub_id, joined_at FROM BookClubMembers WHERE bookclub_id = ? ORDER BY user_id;";
        return jdbcTemplate.query(sql, new BookClubMemberMapper(), bookClubId);
    }

//    @Override
//    public List<BookClubMember> findClubsByUserId(int userId) {
//        final String sql = "SELECT id, user_id, bookclub_id, joined_at FROM BookClubMembers WHERE bookclub_id = ? ORDER BY user_id;";
//        return jdbcTemplate.query(sql, new BookClubMemberMapper(), bookClubId);
//    }

    @Override
    public BookClubMember add(BookClubMember member) {
        // Check if the member already exists
        final String checkSql = "SELECT COUNT(*) FROM BookClubMembers WHERE user_id = ? AND bookclub_id = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, member.getUserId(), member.getBookClubId());

        if (count != null && count > 0) {
            return null; // Member already exists
        }

        // Insert the new member
        final String sql = "INSERT INTO BookClubMembers (user_id, bookclub_id, joined_at) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, member.getUserId());
            ps.setInt(2, member.getBookClubId());
            ps.setDate(3, member.getDate() == null ? null : Date.valueOf(member.getDate()));
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }
        return member;
    }



    @Override
    @Transactional
    public boolean deleteMember(int userId, int bookClubId) {
        final String sql = "DELETE FROM BookClubMembers WHERE user_id = ? AND bookclub_id = ?;";
        int rowsAffected = jdbcTemplate.update(sql, userId, bookClubId);
        return rowsAffected > 0;
    }

}
