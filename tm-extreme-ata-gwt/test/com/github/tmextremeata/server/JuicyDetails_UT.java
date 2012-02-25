package com.github.tmextremeata.server;

import com.github.tmextremeata.server.dao.JuicyDetails;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static junit.framework.Assert.assertEquals;

public class JuicyDetails_UT {

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
    public void addingNewDetailsShouldReallyAddSomethingToTheDataStore() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        JuicyDetails juicyDetails = new JuicyDetails();

        juicyDetails.addDetail(new Date(), "something juicy");

        assertEquals(1, ds.prepare(new Query("Detail")).countEntities(withLimit(10)));
    }

    @Test
    public void detailShouldHaveDateAndHaveText() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Entity detail = new Entity(JuicyDetails.DETAIL_ENTITY);
        detail.setProperty("date", new Date());
        detail.setProperty("text", "juicy juicy juicy");
        ds.put(detail);
        JuicyDetails juicyDetails = new JuicyDetails();

        List<String> details = juicyDetails.getDetails();

        assertEquals("juicy juicy juicy", details.iterator().next());
    }
}
