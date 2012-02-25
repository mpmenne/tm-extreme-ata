package com.github.tmextremeata.server;

import com.github.tmextremeata.server.dao.JuicyDetails;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class SimpleServletWithFormImpl extends HttpServlet {

    JuicyDetails juicyDetails = new JuicyDetails();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        RequestDispatcher view = req.getRequestDispatcher("juicyCampus.jsp");
        view.forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.setAttribute("lastUpdated", currentDate());
        if (req.getParameter("juicyDetails") != null) {
            juicyDetails.addDetail(new Date(), req.getParameter("juicyDetails"));
            req.setAttribute("juicyDetails", juicyDetails.getDetails());
        }
        RequestDispatcher view = req.getRequestDispatcher("juicyCampus.jsp");
        view.forward(req, resp);
    }

    private static String currentDate() {
        org.joda.time.DateTime dateTime = new org.joda.time.DateTime();
        return String.format("%s/%s/%s", dateTime.getMonthOfYear(), dateTime.getDayOfMonth(), dateTime.getYear());
    }


}
