package ru.job4j.accident.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;
import ru.job4j.accident.repository.AccidentRepository;
import ru.job4j.accident.repository.AccidentTypeRepository;
import ru.job4j.accident.repository.RuleRepository;

import java.util.List;

@Service
public class AccidentService {

    private final AccidentRepository acRep;

    private final AccidentTypeRepository  typeRep;

    private final RuleRepository rRep;

    @Autowired
    public AccidentService(AccidentRepository acRep,
                           AccidentTypeRepository typeRep,
                           RuleRepository rRep) {
        this.acRep = acRep;
        this.typeRep = typeRep;
        this.rRep = rRep;
    }

    public List<Accident> getAllAccidents() {
        return acRep.findAllAccidents();
    }

    @Transactional
    public void addAccident(Accident accident, String[] ids) {
        if (accident.getId() == 0) {
            for (String id : ids) {
                accident.addRule(rRep.findById(Integer.parseInt(id)).get());
            }
            acRep.save(accident);
        } else {
            acRep.updateAccident(
                    accident.getName(),
                    accident.getText(),
                    accident.getAddress(),
                    accident.getId()
            );
        }
    }

    public Accident findById(int id) {
        return acRep.findById(id).get();
    }

    public List<AccidentType> getAllTypes() {
        return (List<AccidentType>) typeRep.findAll();
    }

    public List<Rule> getAllRules() {
        return (List<Rule>) rRep.findAll();
    }
}
