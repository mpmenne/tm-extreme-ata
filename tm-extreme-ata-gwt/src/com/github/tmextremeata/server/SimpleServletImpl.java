package com.github.tmextremeata.server;

import com.github.tmextremeata.server.dao.PlayerStore;
import com.github.tmextremeata.server.util.BCrypt;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import static com.github.tmextremeata.server.util.BCrypt.gensalt;

public class SimpleServletImpl extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Entity entity = new Entity(PlayerStore.PLAYER);
        entity.setProperty("name", "mike");
        entity.setProperty("loggedIn", false);
        entity.setProperty("hashPassword", BCrypt.hashpw("mike21", gensalt()));
        ds.put(entity);
        resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();
        writer.println("<p>Hey the system date time is " + new Date().toString() + "</p><p>And added mike</p>");
    }
}
