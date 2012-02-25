package com.github.tmextremeata.client.view.intro;

import com.github.tmextremeata.client.view.Display;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

public class IntroView extends Composite implements Display {

    @UiField
    TextBox nameField;

    @UiField
    Button sendButton;

    private static IntroViewUiBinder uiBinder = GWT.create(IntroViewUiBinder.class);

    interface IntroViewUiBinder extends UiBinder<Widget, IntroView> {
    }

    public IntroView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public TextBox getNameField() {
        return nameField;
    }

    public Button getSendButton() {
        return sendButton;
    }
}
