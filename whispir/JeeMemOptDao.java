import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Log4j2
@Repository
public class JeeMemOptDao {
    private final JdbcTemplate jdbcTemplate;

    public JeeMemOptDao(MemberHibernateConfig memberHibernateConfig) {
        this.jdbcTemplate = new JdbcTemplate(memberHibernateConfig.dataSource());
    }

    public List<JEEMember> callMemberInfo(String nric, BigDecimal dob) {
        String memberSql = "{call db_cus..sp_get_member_info(?,?)}";
        return jdbcTemplate.query(memberSql, ps -> {
            ps.setString(1, nric);
            ps.setBigDecimal(2, dob);
        }, new JEEMemberRowMapper());
    }

    public List<JEEMember> callMemberInfoMedix(String nric, String dob) {
        String memberSql = "{call db_cus..sp_get_member_info_medix (?,?)}";
        return jdbcTemplate.query(memberSql, ps -> {
            ps.setString(1, nric);
            ps.setString(2, dob);
        }, new JEEMemberRowMapperMedix());
    }

    public List<JEEMember> callMemberByNIRC(String nric) {
        String memberSql = "{call dbo.sp_get_member_info_appt (?)}";
        return jdbcTemplate.query(memberSql, ps -> ps.setString(1, nric), new JEEMemberRowMapperAll());
    }

    private static class BaseJEEMemberRowMapper implements RowMapper<JEEMember> {
        @Override
        public JEEMember mapRow(ResultSet rs, int rowNum) throws SQLException {
            JEEMember member = new JEEMember();
              return member;
        }
    }

    private static class JEEMemberRowMapper extends BaseJEEMemberRowMapper {
        @Override
        public JEEMember mapRow(ResultSet rs, int rowNum) throws SQLException {
            JEEMember member = super.mapRow(rs, rowNum);
            
            return member;
        }
    }

    private static class JEEMemberRowMapperMedix extends BaseJEEMemberRowMapper {
        @Override
        public JEEMember mapRow(ResultSet rs, int rowNum) throws SQLException {
            JEEMember member = super.mapRow(rs, rowNum);
            // Add specific fields for Medix
            return member;
        }
    }

    private static class JEEMemberRowMapperAll extends BaseJEEMemberRowMapper {
        @Override
        public JEEMember mapRow(ResultSet rs, int rowNum) throws SQLException {
            JEEMember member = super.mapRow(rs, rowNum);
            // Add specific fields for All
            return member;
        }
    }
}