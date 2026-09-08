package org.mserz_o.springcourse.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.mserz_o.springcourse.model.entity.Player;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class PlayerDao {

    private final SessionFactory sessionFactory;

    public PlayerDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Optional<Player> show(String name) {
        Session session = sessionFactory.getCurrentSession();

        return session.createQuery("FROM Player p WHERE p.name = :name", Player.class)
                .setParameter("name", name)
                .uniqueResultOptional();
    }

    public Player save(String name){
        Session session = sessionFactory.getCurrentSession();
        Player player = new Player(name);
        session.persist(player);

        session.flush();

        return player;
    }
}
