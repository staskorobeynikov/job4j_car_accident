package ru.job4j.accident.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;

import java.sql.*;
import java.util.*;

@Repository
public class AccidentJdbcTemplate {

    private final JdbcTemplate jdbc;

    public AccidentJdbcTemplate(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Rule> ruleRowMapper = (
            (rs, i) -> Rule.of(
                rs.getInt("id"),
                rs.getString("name")
            )
    );

    private final RowMapper<AccidentType> typeRowMapper = (
            (rs, i) -> AccidentType.of(
                    rs.getInt("id"),
                    rs.getString("name")
            )
    );

    private final RowMapper<Accident> accidentRowMapper = ((rs, i) -> {
        Accident accident = Accident.of(
                rs.getString("name"),
                rs.getString("text"),
                rs.getString("address"),
                AccidentType.of(
                        rs.getInt("type_id"),
                        rs.getString("t_name")
                ),
                new HashSet<>()
        );
        accident.setId(rs.getInt("id"));
        return accident;
    });

    private final ResultSetExtractor<Map<Integer, Accident>> extractor = (rs) -> {
        Map<Integer, Accident> result = new HashMap<>();
        while (rs.next()) {
            Accident accident = Accident.of(
                    rs.getString("name"),
                    rs.getString("text"),
                    rs.getString("address"),
                    AccidentType.of(
                            rs.getInt("type_id"),
                            rs.getString("t_name")
                    ),
                    new HashSet<>()
            );
            accident.setId(rs.getInt("id"));
            result.putIfAbsent(accident.getId(), accident);
            result.get(accident.getId()).addRule(
                    Rule.of(
                            rs.getInt("r_id"),
                            rs.getString("r_name")
                    )
            );
        }
        return result;
    };

    @Transactional
    public void addAccident(Accident accident, String[] rIds) {
        String query = "insert into accident (name, text, address, type_id) "
                + "values (?, ?, ?, ?)";
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement statement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, accident.getName());
            statement.setString(2, accident.getText());
            statement.setString(3, accident.getAddress());
            statement.setInt(4, accident.getType().getId());
            return statement;
        }, holder);

        Integer idS = (Integer) holder.getKeys().get("id");

        for (String id : rIds) {
            jdbc.update(
                    "insert into accident_rule (fk_accident, fk_rule) values (?, ?)",
                    idS,
                    Integer.parseInt(id)
            );
        }
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
        String query = "select a.id, a.name, text, address, type_id, t.name as t_name, "
                + "r.id as r_id, r.name as r_name from accident as a "
                + "join accident_rule ar on a.id = ar.fk_accident "
                + "join rules r on ar.fk_rule = r.id "
                + "join types t on a.type_id = t.id";
        Collection<Accident> values = jdbc.query(
                query,
                extractor
        ).values();
        return values.size() > 0 ? new ArrayList<>(values) : new ArrayList<>();
    }

    public Accident findById(int id) {
        return jdbc.queryForObject(
                "select a.id, a.name, text, address, type_id, "
                        + "t.name as t_name from accident as a "
                        + "join types as t on a.type_id = t.id "
                        + "where a.id = ?",
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

    public List<Rule> getAllRules() {
        return jdbc.query(
                "select id, name from rules",
                ruleRowMapper
        );
    }
}
