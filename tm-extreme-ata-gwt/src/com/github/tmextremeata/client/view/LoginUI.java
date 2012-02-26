package com.github.tmextremeata.client.view;

import com.github.tmextremeata.shared.Player;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

public class LoginUI extends Composite {
//TODO Internationalisation
private final Label txtUser = new Label("Username:");
    //TODO Internationalisation
    private final Label txtPw = new Label("Password:");
    private final TextBoxBase inpUser = new TextBox();
    private final TextBoxBase inpPw = new PasswordTextBox();
    //TODO Internationalisation
    private final Button btLogIn = new Button("Login");

    LoginServiceAsync loginService = GWT.create(LoginService.class);


    public LoginUI() {
        final FormPanel form = new FormPanel();
        form.add(this.createAndFormatContentPanel());
        this.initWidget(form);
        this.btLogIn.addClickListener(new ClickListener() {


            public void onClick(final Widget source) {

                loginService.loginServer(inpUser.getText(), inpPw.getText(), new AsyncCallback<Player>() {
                    public void onFailure(Throwable error) {
//          RootPanel.get().add(new LoginUI());
                        error.printStackTrace();
                    }

                    public void onSuccess(Player result) {

                        if (result.isLoggedIn()) {

                            //Zeige Hauptanwendung
                            Window.alert("Welcome");
                            RootPanel.get().clear();
                            RootPanel.get().add(new MainUI());
//
                        } else {
                            Window.alert("Error");
                        }


                    }
                });


            }
        });
    }

    private Widget createAndFormatContentPanel() {
        final Grid grid = new Grid(3, 2);
        grid.setWidget(0, 0, this.txtUser);
        grid.setWidget(0, 1, this.inpUser);
        grid.setWidget(1, 0, this.txtPw);
        grid.setWidget(1, 1, this.inpPw);
        grid.setWidget(2, 1, this.btLogIn);

        return grid;
    }
}
