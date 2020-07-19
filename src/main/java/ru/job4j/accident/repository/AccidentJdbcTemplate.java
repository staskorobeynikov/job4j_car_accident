package ru.job4j.accident.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class AccidentJdbcTemplate {

    private final JdbcTemplate jdbc;

    public AccidentJdbcTemplate(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Rule> ruleRowMapper = ((rs, i) -> {
        Rule rule = Rule.of(
                rs.getInt("id"),
                rs.getString("name")
        );
        return rule;
    });

    private final RowMapper<AccidentType> typeRowMapper = ((rs, i) -> {
        AccidentType type = AccidentType.of(
                rs.getInt("id"),
                rs.getString("name")
        );
        return type;
    });

    private final RowMapper<Accident> accidentRowMapper = ((rs, i) -> {
        Accident accident = Accident.of(
                rs.getString("name"),
                rs.getString("text"),
                rs.getString("address"),
                findTypeById(rs.getInt("type_id")),
                getSetRules(rs.getString("rIds"))
        );
        accident.setId(rs.getInt("id"));
        return accident;
    });

    public void addAccident(Accident accident, String[] rIds) {
        jdbc.update(
                "insert into accident (name, text, address, type_id, rIds) "
                + "values (?, ?, ?, ?, ?)",
                accident.getName(),
                accident.getText(),
                accident.getAddress(),
                accident.getType().getId(),
                rIds
        );
    }

    public void updateAccident(Accident accident) {
        jdbc.update("update accident set name = ?, text = ?, address = ? where id = ?",
                accident.getName(),
                accident.getText(),
                accident.getAddress(),
                accident.getId()
        );
    }

    public List<Accident> getAllAccidents() {
        return jdbc.query(
                "select id, name, text, address, type_id, rIds from accident",
                accidentRowMapper
        );
    }

    public Accident findById(int id) {
        return jdbc.queryForObject(
                "select id, name, text, address, type_id, rIds from accident where id = ?",
                accidentRowMapper,
                id
        );
    }

    public List<AccidentType> getAllTypes() {
        return jdbc.query(
                "select id, name from types",
                typeRowMapper
        );
    }

    public AccidentType findTypeById(int id) {
        return jdbc.queryForObject(
                "select id, name from types where id = ?",
                typeRowMapper,
                id
        );
    }

    public List<Rule> getAllRules() {
        return jdbc.query(
                "select id, name from rules",
                ruleRowMapper
        );
    }

    public Rule findRuleById(int id) {
        return jdbc.queryForObject(
                "select id, name from rules where id = ?",
                ruleRowMapper,
                id);
    }

    public Set<Rule> getSetRules(String ids) {
        Set<Rule> result = new HashSet<>();
        for (String id : getRuleIds(ids)) {
            result.add(findRuleById(Integer.parseInt(id)));
        }
        return result;
    }

    private String[] getRuleIds(String ids) {
        String[] rIds = ids.split("\\s|,|\\{|}");
        String[] rsl = new String[rIds.length - 1];
        System.arraycopy(rIds, 1, rsl, 0, rsl.length);
        return rsl;
    }
}
