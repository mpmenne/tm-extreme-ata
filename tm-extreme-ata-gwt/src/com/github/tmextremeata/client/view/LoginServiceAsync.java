package com.github.tmextremeata.client.view;

import com.github.tmextremeata.shared.Player;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

public interface LoginServiceAsync {
    void loginServer(String name, String password, AsyncCallback<Player> async);

    void loginFromSessionServer(AsyncCallback<Player> async);

    void logout(AsyncCallback<Void> async);
}
