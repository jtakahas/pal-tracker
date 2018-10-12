package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(DataSource dataSource){

        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {

        final String INSERT_SQL = "INSERT INTO time_entries (project_id, user_id, date, hours) VALUES (?, ?,?,?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                        PreparedStatement ps =
                                con.prepareStatement(INSERT_SQL, new String[] {"id"});
                        ps.setLong(1, timeEntry.getProjectId());
                        ps.setLong(2, timeEntry.getUserId());
                        ps.setDate(3,Date.valueOf(timeEntry.getDate()));
                        ps.setInt(4, timeEntry.getHours());
                        return ps;
                    }
                },
                keyHolder);

        return find(keyHolder.getKey().longValue());
    }

    @Override
    public TimeEntry find(long id) {
        Object[] objects = new Object[]{id};

        List<TimeEntry> timeEntryList = jdbcTemplate.query("SELECT id, project_id, user_id, date, hours FROM time_entries WHERE id = ?",
                objects, timeEntryRowMapper);

        if(timeEntryList.size() == 0) {
            return null;
        }

        return timeEntryList.get(0);
    }

    private RowMapper<TimeEntry> timeEntryRowMapper = new RowMapper<TimeEntry>() {
        @Override
        public TimeEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new TimeEntry(
                    rs.getLong("id"),
                    rs.getLong("project_id"),
                    rs.getLong("user_id"),
                    rs.getDate("date").toLocalDate(),
                    rs.getInt("hours")
                    );
        }
    };

    @Override
    public List<TimeEntry> list() {

        return jdbcTemplate.query("SELECT id, project_id, user_id, date, hours FROM time_entries", timeEntryRowMapper);
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {

        jdbcTemplate.update("UPDATE time_entries SET project_id = ?, user_id =?, date=?, hours=? where id = ?",
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                Date.valueOf(timeEntry.getDate()),
                timeEntry.getHours(),
                id
        );


        return find(id);
    }

    @Override
    public void delete(long id) {

        jdbcTemplate.update("DELETE FROM time_entries WHERE id = ?", id);

    }
}
