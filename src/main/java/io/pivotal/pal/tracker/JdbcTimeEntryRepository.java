package io.pivotal.pal.tracker;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class JdbcTimeEntryRepository implements TimeEntryRepository {
    private MysqlDataSource dataSource=null;
    private JdbcTemplate jdbcTemplate;
    private String insertSql = "INSERT INTO time_entries (project_id, user_id, date, hours) VALUES (?, ?, ?, ?)";
    private String findSql = "select * from time_entries where id = ?";
    private String updateSql="update time_entries set project_id=?, user_id=? , date=?, hours=? where id=? ";
    private String deleteSql = "delete from time_entries where id = ?";
    private String selectAllSql = "select * from time_entries";

    public JdbcTimeEntryRepository(MysqlDataSource dataSource) {
        this.dataSource=dataSource;
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeentry) {
        jdbcTemplate.update(insertSql, new Object[]{timeentry.getProjectId(),
        timeentry.getUserId(), Date.valueOf(timeentry.getDate()), timeentry.getHours()});
        int id = getMaxId();
        timeentry.setId(id);
        return timeentry;
    }

    @Override
    public TimeEntry find(long id) {
        try {
            Map<String, Object> foundEntry = jdbcTemplate.queryForMap(findSql, id);
            return new TimeEntry((Long) foundEntry.get("id"),
                    (Long) foundEntry.get("project_id"),
                    (Long) foundEntry.get("user_id"),
                    ((java.sql.Date) foundEntry.get("date")).toLocalDate(),
                    (Integer) foundEntry.get("hours"));

        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeentry) {
        jdbcTemplate.update(updateSql,  new Object[]{timeentry.getProjectId(),
                timeentry.getUserId(), timeentry.getDate(), timeentry.getHours(), id});
        timeentry.setId(id);
        return timeentry;
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update(deleteSql, new Object[]{ id});
    }

    @Override
    public List<TimeEntry> list()  {
        List<TimeEntry> timeEntries = jdbcTemplate.query(selectAllSql, new RowMapper<TimeEntry>() {
            @Override
            public TimeEntry mapRow(ResultSet foundEntry, int rowNum) {
                try {
                    return new TimeEntry((Long) foundEntry.getLong("id"),
                            (Long) foundEntry.getLong("project_id"),
                            (Long) foundEntry.getLong("user_id"),
                            ((java.sql.Date) foundEntry.getDate("date")).toLocalDate(),
                            (Integer) foundEntry.getInt("hours"));
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });

        return timeEntries;
    }

    private int getMaxId() {
        int count = jdbcTemplate.queryForObject("select max(id) from time_entries", Integer.class);
        return count;
    }
}
