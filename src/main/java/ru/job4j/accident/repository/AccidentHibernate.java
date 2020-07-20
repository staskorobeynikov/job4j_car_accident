package ru.job4j.accident.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.job4j.accident.model.Accident;
import ru.job4j.accident.model.AccidentType;
import ru.job4j.accident.model.Rule;

import javax.persistence.EntityManager;
import java.util.List;

//@Repository
public class AccidentHibernate {

    private final SessionFactory sf;

    public AccidentHibernate(SessionFactory sf) {
        this.sf = sf;
    }


    public List<Accident> getAllAccidents() {
        try (Session session = sf.openSession()) {
            return session.createQuery(
                    "select distinct a from Accident a join fetch a.rules", Accident.class).list();
        }
    }

    public void addAccident(Accident accident, String[] ids) {
        try (Session session = sf.openSession()) {
            EntityManager em = session.getEntityManagerFactory().createEntityManager();
            em.getTransaction().begin();

            for (String id : ids) {
                Rule r = em.find(Rule.class, Integer.parseInt(id));
                accident.addRule(r);
            }

            em.persist(accident);
            em.getTransaction().commit();
            em.close();
        }
    }

    public void updateAccident(Accident accident) {
        try (Session session = sf.openSession()) {
            session.getTransaction().begin();

            session.createQuery(
                    "update Accident "
                            + "set name = :name, text = :text, address = :address "
                            + "where id = :id")
                    .setParameter("name", accident.getName())
                    .setParameter("text", accident.getText())
                    .setParameter("address", accident.getAddress())
                    .setParameter("id", accident.getId())
                    .executeUpdate();

            session.getTransaction().commit();
        }
    }



    public List<AccidentType> getAllTypes() {
        try (Session session = sf.openSession()) {
            return session.createQuery("from AccidentType", AccidentType.class).list();
        }
    }

    public List<Rule> getAllRules() {
        try (Session session = sf.openSession()) {
            return session.createQuery("from Rule", Rule.class).list();
        }
    }

    public Accident findById(int id) {
        try (Session session = sf.openSession()) {
            return session.createQuery(
                    "select distinct a from Accident a join fetch a.rules where a.id = :id",
                    Accident.class
            ).setParameter("id", id).uniqueResult();
        }
    }
}
