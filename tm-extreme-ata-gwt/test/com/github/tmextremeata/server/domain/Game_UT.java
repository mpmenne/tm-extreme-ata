package com.github.tmextremeata.server.domain;

import com.google.appengine.repackaged.com.google.common.collect.Sets;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class Game_UT {

    @Test
    public void playerShouldOnlyBeInGameIfNameMatches() {
        Game game = new Game();
        game.setWord("bar-b-buffalo");
        game.setPlayers(Sets.<String>newHashSet("bob", "tom", "sean"));

        boolean inGame = game.isInGame("tom");

        assertThat(inGame, is(true));
    }

    @Test
    public void noOneInGameMeansNo() {
        Game game = new Game();
        game.setWord("bar-b-buffalo");
        game.setPlayers(Sets.<String>newHashSet());

        boolean inGame = game.isInGame("tom");

        assertThat(inGame, is(false));
    }

    @Test
    public void noMatcheeNoGamee() {
        Game game = new Game();
        game.setWord("bar-b-buffalo");
        game.setPlayers(Sets.<String>newHashSet("john"));

        boolean inGame = game.isInGame("tom");

        assertThat(inGame, is(false));
    }

}
