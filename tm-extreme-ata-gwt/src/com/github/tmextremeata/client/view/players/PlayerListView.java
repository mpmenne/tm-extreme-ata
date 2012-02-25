package com.github.tmextremeata.client.view.players;

import com.github.tmextremeata.client.view.Display;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

public class PlayerListView extends Composite implements Display {

    @UiField
    FlexTable playersTable;

    @UiField
    Label loadingLabel;

    private static PlayerListUiBinder uiBinder = GWT.create(PlayerListUiBinder.class);

    interface PlayerListUiBinder extends UiBinder<Widget, PlayerListView> {
    }

    public PlayerListView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public FlexTable getPlayersTable() {
        return playersTable;
    }

    public Label getLoadingLabel() {
        return loadingLabel;
    }

}
