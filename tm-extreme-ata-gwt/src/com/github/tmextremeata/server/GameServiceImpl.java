package com.github.tmextremeata.server;

import com.github.tmextremeata.client.GameService;
import com.github.tmextremeata.server.dao.PlayerStore;
import com.github.tmextremeata.server.domain.Player;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static java.util.Arrays.asList;

public class GameServiceImpl extends RemoteServiceServlet implements GameService {
    
	private static final long serialVersionUID = -3037022503946607846L;
	
	public static final String PLAYER_NAME = "playerName";

    PlayerStore playerStore = new PlayerStore();

    public String joinAs(String playerName) {
        HttpSession session = this.getThreadLocalRequest().getSession();
        String sessionId = session.getId();
        if (playerStore.getPlayer(sessionId) == null) {
            Player player = playerStore.makeNewPlayer(sessionId, (String) session.getAttribute(PLAYER_NAME));
            return String.format("Dude, %s you're in", player.getName());
        } else {
            playerStore.getPlayer(playerName);
            return String.format("Welcome back in %s", playerName);
        }
    }

    public List<String> getPlayers() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return asList("Mike", "Joe");
    }


}
