package com.github.tmextremeata.client.view;

import com.github.tmextremeata.shared.Player;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {

    Player loginServer(String name, String password);
    Player loginFromSessionServer();
    void logout();
}
