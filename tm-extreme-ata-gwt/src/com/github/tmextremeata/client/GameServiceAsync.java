package com.github.tmextremeata.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

public interface GameServiceAsync {
    void joinAs(String playerName, AsyncCallback<String> async);

    void getPlayers(AsyncCallback<List<String>> async);
}
