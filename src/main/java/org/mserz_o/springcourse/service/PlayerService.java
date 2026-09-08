package org.mserz_o.springcourse.service;

import org.mserz_o.springcourse.dao.PlayerDao;
import org.mserz_o.springcourse.model.entity.Player;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PlayerService {
    private final PlayerDao playerDao;

    public PlayerService(PlayerDao playerDao) {
        this.playerDao = playerDao;
    }

    @Transactional
    public Player findOrCreatePlayer(String name){
        Optional<Player> player = playerDao.show(name);
        return player.orElseGet(() -> playerDao.save(name));
    }
}
