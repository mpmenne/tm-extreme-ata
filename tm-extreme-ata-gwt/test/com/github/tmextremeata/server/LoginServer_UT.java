package com.github.tmextremeata.server;

import com.github.tmextremeata.client.GameService;
import com.github.tmextremeata.client.view.LoginService;
import com.github.tmextremeata.server.dao.PlayerStore;
import com.github.tmextremeata.server.util.BCrypt;
import com.github.tmextremeata.shared.Player;
import com.sun.net.httpserver.HttpsServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpSession;

import static com.github.tmextremeata.server.util.BCrypt.gensalt;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by IntelliJ IDEA.
 * User: Tim King
 * Date: 2/25/12
 * Time: 2:03 PM
 * To change this template use File | Settings | File Templates.
 */

@RunWith(MockitoJUnitRunner.class)
public class LoginServer_UT {

    @Mock
    private PlayerStore playerStore;
    @Mock
    private HttpSession httpSession;
    @InjectMocks
    private LoginServiceImpl loginService = new LoginServiceImpl();

    @Test(expected = IllegalArgumentException.class)
    public void ifPlayerDoesNotExistThrowException() {
        when(playerStore.getPlayer("mike")).thenThrow(new IllegalArgumentException());

        loginService.loginServer("mike","password");

        //Exception Expected
    }

    @Test(expected = IllegalArgumentException.class)
    public void  ifPlayerExistsButHasIncorrectPasswordThrowException() {
        Player player = new Player();
        player.setName("mike");
        when(playerStore.getHashPassword("mike")).thenReturn(BCrypt.hashpw("password", gensalt()));
        when(playerStore.getPlayer("mike")).thenReturn(player);

        loginService.loginServer("mike", "incorrect");

        //Exception Expected
    }

    @Test
    public void ifPlayerExistsAndPasswordIsCorrectThenPlayerIsReturned() {
        Player player = new Player();
        player.setName("mike");
        when(playerStore.getHashPassword("mike")).thenReturn(BCrypt.hashpw("password", gensalt()));
        when(playerStore.getPlayer("mike")).thenReturn(player);

        Player returnedPlayer = loginService.loginServer("mike", "password");

        assertEquals("mike", returnedPlayer.getName());
    }

    @Test
    public void ifPlayerExistsAndPasswordIsCorrectThenPlayerIsInSession() {
        Player player = new Player();
        player.setName("mike");
        when(playerStore.getHashPassword("mike")).thenReturn(BCrypt.hashpw("password", gensalt()));
        when(playerStore.getPlayer("mike")).thenReturn(player);

        loginService.loginServer("mike", "password");

        verify(httpSession).setAttribute("player", player);
    }

    @Test
    public void ifPlayerExistsAndPasswordIsCorrectThenPlayerIsLoggedIn() {
        Player player = new Player();
        player.setName("mike");
        when(playerStore.getHashPassword("mike")).thenReturn(BCrypt.hashpw("password", gensalt()));
        when(playerStore.getPlayer("mike")).thenReturn(player);

        Player returnedPlayer = loginService.loginServer("mike", "password");

        assertTrue(returnedPlayer.isLoggedIn());
        verify(playerStore).updatePlayer(player);
    }
}
