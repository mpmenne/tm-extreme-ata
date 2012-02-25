package com.github.tmextremeata.server.domain;

import javax.jdo.annotations.*;
import java.util.Set;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Game {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key="gae.encoded-pk", value="true")
    private String key;

    @Persistent
    private String word;

    @Persistent
    private Set<String> players;


    public Set<String> getPlayers() {
        return players;
    }

    public void setPlayers(Set<String> players) {
        this.players = players;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public boolean isInGame(String name) {
        for (String player : players) {
            if (player.equals(name)) {
                return true;
            }
        }
        return false;
    }
}
