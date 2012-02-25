package com.github.tmextremeata.server.dao;

import com.github.tmextremeata.server.domain.Player;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import org.joda.time.DateTime;

public class PlayerStore {

    public static final String PLAYER = "Player";


    public Player getPlayer(String sessionId) {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query(PLAYER);
        query.addFilter("sessionId", Query.FilterOperator.EQUAL, sessionId);
        Entity entity = ds.prepare(query).asSingleEntity();
        if (entity != null) {
            return new Player((String) entity.getProperty("name"));
        } else {
            return null;
        }
    }

    public Player makeNewPlayer(String sessionId, String name) {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Entity entity = new Entity(PLAYER);
        entity.setProperty("name", name);
        entity.setProperty("sessionId", sessionId);
        ds.put(entity);
        Player player = new Player();
        player.setSessionId(sessionId);
        player.setName(name);
        return player;
    }
}
