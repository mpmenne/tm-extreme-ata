package com.github.tmextremeata.server.dao;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.search.SortSpec;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JuicyDetails {


    public static final String DETAIL_ENTITY = "Detail";

    public void addDetail(Date date, String juiciness) {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Entity juicyDetail = new Entity(DETAIL_ENTITY);
        juicyDetail.setProperty("date", date);
        juicyDetail.setProperty("text", juiciness);
        ds.put(juicyDetail);
    }

    public List<String> getDetails() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Query query = new Query(DETAIL_ENTITY);
        query.addSort("date", Query.SortDirection.DESCENDING);
        query.addFilter("date", Query.FilterOperator.GREATER_THAN, new DateTime().minusMinutes(5).toDate());
        Iterable<Entity> entityIterable = ds.prepare(query).asIterable();
        return new ArrayList(extractJuiciness(entityIterable));
    }


    private List<String> extractJuiciness(Iterable<Entity> entityIterable) {
        List<String> details = new ArrayList<String>();
        for (Entity entity : entityIterable) {
            details.add((String) entity.getProperty("text"));
        }
        return details;
    }
}
