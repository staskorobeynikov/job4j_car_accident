package ru.job4j.accident.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;
import ru.job4j.accident.repository.AccidentHibernate;

import java.util.List;

@Service
public class AccidentService {

    private final AccidentHibernate store;

    @Autowired
    public AccidentService(AccidentHibernate store) {
        this.store = store;
    }

    public List<Accident> getAllAccidents() {
        return store.getAllAccidents();
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

    public List<AccidentType> getAllTypes() {
        return store.getAllTypes();
    }

    public List<Rule> getAllRules() {
        return store.getAllRules();
    }
}
