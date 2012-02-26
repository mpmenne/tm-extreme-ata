package com.github.tmextremeata.server;

import com.github.tmextremeata.client.view.LoginService;
import com.github.tmextremeata.server.dao.PlayerStore;
import com.github.tmextremeata.server.util.BCrypt;
import com.github.tmextremeata.shared.Player;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import javax.servlet.http.HttpSession;

public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {

    private HttpSession httpSession;
    private PlayerStore playerStore = new PlayerStore();

    private HttpSession getSession() {
        if (this.getThreadLocalRequest() != null) {
            httpSession = this.getThreadLocalRequest().getSession();
        }
        return httpSession;
    }

    public Player loginServer(String name, String password) {
        Player player = playerStore.getPlayer(name);
        if (!BCrypt.checkpw(password, playerStore.getHashPassword(name))) {
            throw new IllegalArgumentException("Password does not match");
        }
        player.setLoggedIn(true);
        getSession().setAttribute("player", player);
        playerStore.updatePlayer(player);
        return player;
    }

    public Player loginFromSessionServer() {
        Player player = null;
        Object userObj = getSession().getAttribute("player");
        if (userObj != null && userObj instanceof Player) {
            player = (Player) userObj;
        }
        return player;
    }

    public void logout() {
        this.getThreadLocalRequest().getSession().removeAttribute("user");
    }

    public void setPlayerStore(PlayerStore playerStore) {
        this.playerStore = playerStore;
    }

    public void setHttpSession(HttpSession httpSession) {
        this.httpSession = httpSession;
    }
}
