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
        Entity entity = getPlayerEntity(name);
        if (entity == null) {
            throw new IllegalArgumentException("Player was not found");
        }
        return newPlayer(entity);
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

    public Player getBySessionId(String sessionId) {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query(PLAYER);
        query.addFilter("sessionId", Query.FilterOperator.EQUAL, sessionId);
        Entity entity = ds.prepare(query).asSingleEntity();
        if (entity == null) { return null; }
        return newPlayer(entity);
    }

    public void registerSession(String name, String sessionId) {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query(PLAYER);
        query.addFilter("name", Query.FilterOperator.EQUAL, name);
        Entity entity = ds.prepare(query).asSingleEntity();
        entity.setProperty("sessionId", sessionId);
        ds.put(entity);
    }

    private static Player newPlayer(Entity entity) {
        Player player = new Player();
        player.setLoggedIn((Boolean) entity.getProperty("loggedIn"));
        player.setName((String) entity.getProperty("name"));
        return player;
    }
}
