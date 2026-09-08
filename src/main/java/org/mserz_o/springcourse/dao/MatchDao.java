package org.mserz_o.springcourse.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.mserz_o.springcourse.model.entity.Match;
import org.mserz_o.springcourse.model.entity.Player;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MatchDao {

    private final SessionFactory sessionFactory;

    public MatchDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Player firstPlayer, Player secondPlayer, Player winner){
        Session session = sessionFactory.getCurrentSession();
        session.persist(new Match(firstPlayer, secondPlayer, winner));
    }

    private Long showMatchQuantityWithoutName(Session session){
        return session.createQuery("select count(*) from Match m", Long.class)
                .getSingleResult();
    }

    private Long showMatchQuantityWithName(Session session, String name){
        return session.createQuery("select count(*) from Match m where m.firstPlayer.name like :name or m.secondPlayer.name like :name", Long.class)
                .setParameter("name", normalizedNameFilter(name))
                .getSingleResult();
    }

    public Long showMatchQuantity(String name){
        Session session = sessionFactory.getCurrentSession();
        if(name != null){
            return showMatchQuantityWithName(session, name);
        }else {
            return showMatchQuantityWithoutName(session);
        }
    }

    public List<Match> show(int offset, int limit){
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("select m from Match m order by m.id desc", Match.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<Match> show(String name, int offset, int limit) {
        Session session = sessionFactory.getCurrentSession();

        return session.createQuery("select m FROM Match m WHERE m.firstPlayer.name like :name or m.secondPlayer.name like :name order by m.id desc", Match.class)
                .setParameter("name", normalizedNameFilter(name))
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    private static String normalizedNameFilter(String nameFilter) {
        return "%" + nameFilter.trim() + "%";
    }
}
