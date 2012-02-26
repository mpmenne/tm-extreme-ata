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
        playerStore.updatePlayer(player);
        playerStore.registerSession(name, getSession().getId());
        getSession().setAttribute("player", player);
        return player;
    }

    public Player loginFromSessionServer() {
        validatePlayerInSession();
        Player player = playerStore.getBySessionId(getSession().getId());
        if (player == null) { return null; }
        if (!player.isLoggedIn()) { return null; }
        player.setLoggedIn(true);
        playerStore.updatePlayer(player);
        return player;
    }

    public void logout() {
        validatePlayerInSession();
        Player datastorePlayer = playerStore.getBySessionId(getSession().getId());
        datastorePlayer.setLoggedIn(false);
        playerStore.updatePlayer(datastorePlayer);
        getSession().removeAttribute("player");
    }

    private void validatePlayerInSession() {
        Player datastorePlayer = playerStore.getBySessionId(getSession().getId());
        Player sessionPlayer = (Player) getSession().getAttribute("player");
        if (datastorePlayer == null || sessionPlayer == null) { return; }
        if (!sessionPlayer.getName().equals(datastorePlayer.getName())) {
            throw new IllegalArgumentException("Player saved under this session does not match player in session");
        }
    }

    public void setPlayerStore(PlayerStore playerStore) {
        this.playerStore = playerStore;
    }

    public void setHttpSession(HttpSession httpSession) {
        this.httpSession = httpSession;
    }
}
