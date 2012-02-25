package com.github.tmextremeata.server;

import com.github.tmextremeata.client.view.LoginService;
import com.github.tmextremeata.shared.Player;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {


    public Player loginServer(String name, String password) {
        Player player = new Player();

        if (name.equals("guest") && password.equals("guest")) {
            player.setLoggedIn(true);
        }
        return player;
    }

    public Player loginFromSessionServer() {
        Player player = null;
        HttpSession session = this.getThreadLocalRequest().getSession();
        Object userObj = session.getAttribute("user");
        if (userObj != null && userObj instanceof Player) {
            player = (Player) userObj;
        }
        return player;
    }

    public void logout() {
        this.getThreadLocalRequest().getSession().removeAttribute("user");
    }
}
