package ru.job4j.accident.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.accident.model.Accident;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class AccidentMem {

    private final Map<Integer, Accident> accidents = new ConcurrentHashMap<>();

    private static final AtomicInteger ACC_ID = new AtomicInteger(4);

    private AccidentMem() {
        Accident first = Accident.of("First", "Поцарапан бампер", "Minsk");
        first.setId(1);
        Accident second = Accident.of("Second", "Поцарапано крыло", "Gomel");
        second.setId(2);
        Accident third = Accident.of("Third", "Разбито стекло", "Grodno");
        third.setId(3);
        accidents.put(1, first);
        accidents.put(2, second);
        accidents.put(3, third);
    }

    public void addAccident(Accident accident) {
        if (accident.getId() == 0) {
            accident.setId(ACC_ID.incrementAndGet());
        }
        accidents.put(accident.getId(), accident);
    }

    public List<Accident> getAllAccidents() {
        return new ArrayList<>(accidents.values());
    }
}
