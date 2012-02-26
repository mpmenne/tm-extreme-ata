package com.github.tmextremeata.client.view;

import com.github.tmextremeata.shared.Player;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

public class MainUI extends Composite {

    private final Button btLogIn = new Button("Logout");
    LoginServiceAsync loginService = GWT.create(LoginService.class);

    public MainUI(Player player) {
        this.initWidget(this.createAndFormateContentPanel(player));

    }

    private Widget createAndFormateContentPanel(Player player) {
        final Panel panel = new VerticalPanel();

        panel.add(new Label("Hello, " + player.getName()));

        panel.add(btLogIn);

        this.btLogIn.addClickListener(new ClickListener() {

            public void onClick(final Widget source) {

                loginService.logout(new AsyncCallback<Void>() {
                    public void onFailure(Throwable error) {
                        // RootPanel.get().add(new LoginUI());
                        error.printStackTrace();
                    }

                    @Override
                    public void onSuccess(Void result) {
                        Window.alert("Sucessfully logged out");
                        RootPanel.get().clear();
                        RootPanel.get().add(new LoginUI());
                    }

                });

            }

        });
        return panel;
    }
}
