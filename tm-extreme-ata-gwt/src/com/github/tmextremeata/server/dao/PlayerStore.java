package com.github.tmextremeata.server.dao;

import com.github.tmextremeata.shared.Player;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import org.joda.time.DateTime;

public class PlayerStore {

    public static final String PLAYER = "Player";


    public Player getPlayer(String name) {
//        @TODO throw missing player exception

//        query.addFilter("sessionId", Query.FilterOperator.EQUAL, sessionId);
//        Entity entity = ds.prepare(query).asSingleEntity();
//        if (entity != null) {
//            return new Player((String) entity.getProperty("name"));
//        } else {
//            return null;
//        }
        Entity entity = getPlayerEntity(name);
        if (entity == null) {
            throw new IllegalArgumentException("Player was not found");
        }
        Player player = new Player();
        player.setName((String) entity.getProperty("name"));
        player.setLoggedIn((Boolean) entity.getProperty("loggedIn"));

        return player;
    }

    private Entity getPlayerEntity(String name) {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query(PLAYER);
        query.addFilter("name", Query.FilterOperator.EQUAL, name);
        return ds.prepare(query).asSingleEntity();
    }

    public Player makeNewPlayer(String sessionId, String name) {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Entity entity = new Entity(PLAYER);
        entity.setProperty("name", name);
        entity.setProperty("sessionId", sessionId);
        ds.put(entity);
        Player player = new Player();
//        player.setSessionId(sessionId);
        player.setName(name);
        return player;
    }

    public void updatePlayer(Player player) {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Entity playerEntity = getPlayerEntity(player.getName());
        playerEntity.setProperty("loggedIn", player.isLoggedIn());
        ds.put(playerEntity);
    }

    public String getHashPassword(String name) {
        return (String) getPlayerEntity(name).getProperty("hashPassword");
    }
}
