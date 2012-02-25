package com.github.tmextremeata.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

@RemoteServiceRelativePath("game")
public interface GameService extends RemoteService {

    public String joinAs(String playerName);

    public List<String> getPlayers();

}
