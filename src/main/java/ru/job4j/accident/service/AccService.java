package ru.job4j.accident.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;
import ru.job4j.accident.repository.AccidentJdbcTemplate;

import java.util.List;

@Service
public class AccService {

    private final AccidentJdbcTemplate store;

    @Autowired
    public AccService(AccidentJdbcTemplate store) {
        this.store = store;
    }

    public List<Accident> getAllAccidents() {
        return store.getAllAccidents();
    }

    public List<AccidentType> getAllTypes() {
        return store.getAllTypes();
    }

    public List<Rule> getAllRules() {
        return store.getAllRules();
    }

    public void addAccident(Accident accident, String[] ids) {
        if (accident.getId() == 0) {
            store.addAccident(accident, ids);
        } else {
            store.updateAccident(accident);
        }
    }

    public Accident findById(int id) {
        return store.findById(id);
    }
}
