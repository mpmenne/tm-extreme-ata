package com.github.tmextremeata.client;

import com.github.tmextremeata.client.view.LoginService;
import com.github.tmextremeata.client.view.LoginServiceAsync;
import com.github.tmextremeata.client.view.LoginUI;
import com.github.tmextremeata.client.view.MainUI;
import com.github.tmextremeata.client.view.intro.IntroView;
import com.github.tmextremeata.client.view.players.PlayerListView;
import com.github.tmextremeata.shared.Player;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import javax.servlet.http.HttpSession;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Tm_extreme_ata_gwt implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);
    private final GameServiceAsync gameService = GWT.create(GameService.class);
    private PlayerListView playerListView = new PlayerListView();;
    private LoginServiceAsync loginService = GWT.create(LoginService.class);

    /**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
        loginService.loginFromSessionServer(new AsyncCallback<Player>() {

            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }

            public void onSuccess(Player user) {
                if (user == null || !user.isLoggedIn()) {
                    Window.alert("Game (in beta) require Login. Redirecting");
                    RootPanel.get().add(new LoginUI());

                } else if (user.isLoggedIn()) {
                    Window.alert("Whatcha you say");
                    RootPanel.get().clear();
                    RootPanel.get().add(new MainUI(user));
                }
            }
        });
//        IntroView introView = new IntroView();
//        RootPanel.get().clear();
//        RootPanel.get().add(introView);
//        final TextBox nameField = introView.getNameField();
//        final Button sendButton = introView.getSendButton();
//        nameField.setText("name here");
//
//        // We can add style names to widgets
//        sendButton.addStyleName("sendButton");
//
//        // Focus the cursor on the name field when the app loads
//        nameField.setFocus(true);
//        nameField.selectAll();
//
//        // Create the popup dialog box
//        final DialogBox dialogBox = new DialogBox();
//        dialogBox.setText("Remote Procedure Call");
//        dialogBox.setAnimationEnabled(true);
//        final Button closeButton = new Button("Close");
//        // We can set the id of a widget by accessing its Element
//        closeButton.getElement().setId("closeButton");
//        final Label textToServerLabel = new Label();
//        final HTML serverResponseLabel = new HTML();
//        VerticalPanel dialogVPanel = new VerticalPanel();
//        dialogVPanel.addStyleName("dialogVPanel");
//        dialogVPanel.add(new HTML("<b>Now sending name to the server:</b>"));
//        dialogVPanel.add(textToServerLabel);
//        dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
//        dialogVPanel.add(serverResponseLabel);
//        dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
//        dialogVPanel.add(closeButton);
//        dialogBox.setWidget(dialogVPanel);

        // Add a handler to close the DialogBox
//		closeButton.addClickHandler(new ClickHandler() {
//            public void onClick(ClickEvent event) {
//                dialogBox.hide();
//                sendButton.setEnabled(true);
//                sendButton.setFocus(true);
//                RootPanel.get().clear();
//                RootPanel.get().add(playerListView);
//                gamePresenter.setup();
//            }
//        });

        // Create a handler for the sendButton and nameField
//        class MyHandler implements ClickHandler, KeyUpHandler {
//            /**
//             * Fired when the user clicks on the sendButton.
//             */
//            public void onClick(ClickEvent event) {
//                sendNameToServer();
//            }
//
//            /**
//             * Fired when the user types in the nameField.
//             */
//            public void onKeyUp(KeyUpEvent event) {
//                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
//                    sendNameToServer();
//                }
//            }
//
//            /**
//             * Send the name from the nameField to the server and wait for a response.
//             */
//            private void sendNameToServer() {
//                // First, we validate the input.
//                String textToServer = nameField.getText();
//
//                // Then, we send the input to the server.
//                sendButton.setEnabled(false);
//                textToServerLabel.setText(textToServer);
//                serverResponseLabel.setText("");
//                gameService.joinAs(textToServer,
//                        new AsyncCallback<String>() {
//                            public void onFailure(Throwable caught) {
//                                // Show the RPC error message to the user
//                                dialogBox
//                                        .setText("Remote Procedure Call - Failure");
//                                serverResponseLabel
//                                        .addStyleName("serverResponseLabelError");
//                                serverResponseLabel.setHTML(SERVER_ERROR);
//                                dialogBox.center();
//                                closeButton.setFocus(true);
//                            }
//
//                            public void onSuccess(String result) {
//                                dialogBox.setText("Remote Procedure Call");
//                                serverResponseLabel
//                                        .removeStyleName("serverResponseLabelError");
//                                serverResponseLabel.setHTML(result);
//                                dialogBox.center();
//                                closeButton.setFocus(true);
////                                eventBus.fireEvent(new LoadGameEvent());
//                            }
//                        });
//            }
//        }
//
//        // Add a handler to send the name to the server
//        MyHandler handler = new MyHandler();
//        sendButton.addClickHandler(handler);
//        nameField.addKeyUpHandler(handler);
    }
}
