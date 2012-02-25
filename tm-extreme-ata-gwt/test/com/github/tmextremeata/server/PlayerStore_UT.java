package com.github.tmextremeata.server;

import com.github.tmextremeata.server.dao.PlayerStore;
import com.github.tmextremeata.server.domain.Player;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

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

    @Test
    public void playerExistsAlreadyReturnThatPlayer() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Entity entity = new Entity(PlayerStore.PLAYER);
        entity.setProperty("sessionId", "sesssionId12312423423");
        entity.setProperty("name", "joe");
        ds.put(entity);
        PlayerStore playerStore = new PlayerStore();

        Player player = playerStore.getPlayer("sesssionId12312423423");

        assertEquals("joe", player.getName());
    }

    @Test
    public void returnNullIfPlayerDoesntExist() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        PlayerStore playerStore = new PlayerStore();

        Player player = playerStore.getPlayer("sesssionId12312423423");

        assertNull(player);
    }

    @Test
    public void playerDoesNotExistAlreadyCreateNewPlayer() {
        PlayerStore playerStore = new PlayerStore();

        Player player = playerStore.makeNewPlayer("sessionID1234", "bob");

        assertEquals("bob", player.getName());
    }

    @Test
    public void dataStoreShouldHaveOneMoreItemAfterAdd() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        PlayerStore playerStore = new PlayerStore();

        Player player = playerStore.makeNewPlayer("sessionID1234", "bob");

        assertEquals(1, ds.prepare(new Query(PlayerStore.PLAYER)).countEntities(withLimit(10)));
    }
}
