package com.github.tmextremeata.server;

import com.github.tmextremeata.server.dao.PlayerStore;
import com.github.tmextremeata.shared.Player;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PlayerStore_UT {

    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Before
    public void setup() {
        helper.setUp();
    }

    @After
    public void teardown() {
        helper.tearDown();
    }

    //just to prove it works
     // run this test twice to prove we're not leaking any state across tests
    private void doTest() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        assertEquals(0, ds.prepare(new Query("yam")).countEntities(withLimit(10)));
        ds.put(new Entity("yam"));
        ds.put(new Entity("yam"));
        assertEquals(2, ds.prepare(new Query("yam")).countEntities(withLimit(10)));
    }

    @Test
    public void testInsert1() {
        doTest();
    }

    @Test
    public void testInsert2() {
        doTest();
    }

    @Test (expected = IllegalArgumentException.class)
    public void exceptionShouldBeThrownIfPlayerDoesNotExist() {
        PlayerStore playerStore = new PlayerStore();
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Entity entity = new Entity(PlayerStore.PLAYER);
        entity.setProperty("name", "mike");
        ds.put(entity);

        playerStore.getPlayer("not going to find this one");

        //exception expected
    }

    @Test
    public void weShouldGetAPlayerBackWithLoggedInNamePassword() {
        PlayerStore playerStore = new PlayerStore();
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Entity entity = new Entity(PlayerStore.PLAYER);
        entity.setProperty("name", "mike");
        entity.setProperty("loggedIn", true);
        entity.setProperty("hashPassword", "hashedPassword");
        ds.put(entity);

        Player player = playerStore.getPlayer("mike");

        assertEquals("mike", player.getName());
        assertThat(player.isLoggedIn(), is(true));
    }

    @Test
    public void replacePlayerShouldGetRidOfOldAndBringInNew() {
        PlayerStore playerStore = new PlayerStore();
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Entity entity = new Entity(PlayerStore.PLAYER);
        entity.setProperty("name", "mike");
        entity.setProperty("loggedIn", false);
        entity.setProperty("hashPassword", "hashedPassword");
        ds.put(entity);
        Player player = new Player();
        player.setName("mike");
        player.setLoggedIn(true);

        playerStore.updatePlayer(player);

        Query query = new Query(PlayerStore.PLAYER);
        query.addFilter("name", Query.FilterOperator.EQUAL, "mike");
        assertThat((Boolean) ds.prepare(query).asSingleEntity().getProperty("loggedIn"), is(true));
    }

    @Test
    public void getBySessionIdShouldReturnPlayerUnderThatSession() {
        PlayerStore playerStore = new PlayerStore();
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Entity entity = new Entity(PlayerStore.PLAYER);
        entity.setProperty("name", "mike");
        entity.setProperty("loggedIn", true);
        entity.setProperty("hashPassword", "hashedPassword");
        entity.setProperty("sessionId", "abcsession");
        ds.put(entity);
        Entity differentEntity = new Entity(PlayerStore.PLAYER);
        differentEntity.setProperty("name", "different person");
        differentEntity.setProperty("sessionId", "different session");
        ds.put(differentEntity);

        Player player = playerStore.getBySessionId("abcsession");

        assertEquals("mike", player.getName());
        assertThat(player.isLoggedIn(), is(true));
    }

    @Test
    public void registerSessionShouldAssociateSessionWithAPlayer() {
        PlayerStore playerStore = new PlayerStore();
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Entity entity = new Entity(PlayerStore.PLAYER);
        entity.setProperty("name", "mike");
        entity.setProperty("loggedIn", false);
        entity.setProperty("hashPassword", "hashedPassword");
        ds.put(entity);

        playerStore.registerSession("mike", "abcsession");

        Query query = new Query(PlayerStore.PLAYER);
        query.addFilter("name", Query.FilterOperator.EQUAL, "mike");
        assertThat((String) ds.prepare(query).asSingleEntity().getProperty("sessionId"), is("abcsession"));
    }

    @Test
    public void getBySessionIdShouldJustReturnNullIfNoSessionExistsForPlayer() {
        PlayerStore playerStore = new PlayerStore();

        Player player = playerStore.getBySessionId("abcsession");

        Assert.assertNull(player);
    }
}
