package com.github.tmextremeata.server;

import com.github.tmextremeata.server.domain.Game;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class GameData {

    public Game getPlayersGame(String playerName) {
        PersistenceManager persistenceManager = PersistenceFactory.get().getPersistenceManager();
        Query query;
        try {
            query = persistenceManager.newQuery("select from %s where id == :keys", Game.class.getName());
        } finally {
            persistenceManager.close();
        }
        return(Game) query.execute(query);
    }



}
