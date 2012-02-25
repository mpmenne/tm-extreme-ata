package com.github.tmextremeata.server;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class SimpleServletWithJsp extends HttpServlet{


    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.setAttribute("dateTime", new Date().toString());
        RequestDispatcher view = req.getRequestDispatcher("simple.jsp");
        view.forward(req, resp);
    }

}
