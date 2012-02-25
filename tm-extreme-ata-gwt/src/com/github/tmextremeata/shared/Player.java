package com.github.tmextremeata.shared;

import java.io.Serializable;

public class Player implements Serializable {
    static final long serialVersionUID = 8691571286358652288L;

    private boolean loggedIn;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}
