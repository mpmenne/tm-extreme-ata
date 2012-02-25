package com.github.tmextremeata.server;

import com.github.tmextremeata.client.view.players.PlayerListView;
import com.github.tmextremeata.server.domain.Game;
import com.google.appengine.api.datastore.*;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;

import java.util.Arrays;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static junit.framework.Assert.assertEquals;

public class DatastoreExamples_UT {

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
    public void justDontBombOut() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Entity game = new Entity("Game");

        ds.put(game);

        assertEquals(1, ds.prepare(new Query("Game")).countEntities(withLimit(10)));
    }

    @Test
    public void takeAPeekAtAllOfThePlayers() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Entity player1 = new Entity("Player");

        ds.put(Arrays.asList(player1));

        assertEquals(1, ds.prepare(new Query("Player")).countEntities(withLimit(10)));
    }

    @Test
    public void whatsJoesNickName() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Entity player1 = new Entity("Player");
        player1.setProperty("name", "joe");
        player1.setProperty("nickName", "joe_1.0");

        ds.put(Arrays.asList(player1));

        assertEquals("joe_1.0", (String) ds.prepare(new Query("Player")).asSingleEntity().getProperty("nickName"));
    }

    @Test
    public void whatsBobsNickName() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Entity player1 = new Entity("Player");
        player1.setProperty("name", "joe");
        player1.setProperty("nickName", "joe_1.0");
        Entity player2 = new Entity("Player");
        player2.setProperty("name", "bob");
        player2.setProperty("nickName", "bobo");

        ds.put(Arrays.asList(player1, player2));

        Query query = new Query("Player");
        query.addFilter("name", Query.FilterOperator.EQUAL, "bob");
        assertEquals("bobo", (String) ds.prepare(query).asSingleEntity().getProperty("nickName"));
    }

    @Test
    public void wheresJoe() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Entity player1 = new Entity("Player");
        player1.setProperty("name", "joe");
        Entity player2 = new Entity("Player");
        player2.setProperty("name", "bob");
        Entity bannedPlayer = new Entity("Player");
        bannedPlayer.setProperty("name", "black");
        bannedPlayer.setProperty("nickName", "balled");

        ds.put(Arrays.asList(player1, player2));

        Query query = new Query("Player");
        query.addFilter("nickName", Query.FilterOperator.NOT_EQUAL, "balled");
        assertEquals(0, ds.prepare(query).countEntities(withLimit(10)));  // say WHAT!?  where's Joe and Bob
    }

    @Test
    public void letsPlayAGame() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Key key = KeyFactory.createKey("Game", "game1");
        Entity game = new Entity("Game");
        Entity judge = new Entity("Judge", key);
        Entity player1 = new Entity("Player 1", key);
        Entity player2 = new Entity("Player 2", key);
        Entity player3 = new Entity("Player 3", key);

        ds.put(Arrays.asList(game, judge, player1, player2, player3));

        assertEquals(4, ds.prepare(new Query(key)).countEntities(withLimit(10)));
    }
}
