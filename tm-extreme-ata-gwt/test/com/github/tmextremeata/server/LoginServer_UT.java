package com.github.tmextremeata.server;

import com.github.tmextremeata.client.GameService;
import com.github.tmextremeata.client.view.LoginService;
import com.github.tmextremeata.server.dao.PlayerStore;
import com.github.tmextremeata.server.util.BCrypt;
import com.github.tmextremeata.shared.Player;
import com.sun.net.httpserver.HttpsServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpSession;

import static com.github.tmextremeata.server.util.BCrypt.gensalt;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Mockito.*;
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
    @Captor
    private ArgumentCaptor<Player> playerCaptor;
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

    @Test
    public void logoutShouldRemoveThePlayerFromTheSession() {
        Player player = new Player();
        player.setName("mike");
        when(httpSession.getAttribute("player")).thenReturn(player);
        when(httpSession.getId()).thenReturn("session id");
        when(playerStore.getBySessionId("session id")).thenReturn(player);
        when(playerStore.getPlayer("mike")).thenReturn(player);

        loginService.logout();

        verify(httpSession).removeAttribute("player");
    }

    @Test
    public void logoutShouldSaveThePlayerAsLoggedOut() {
        Player player = new Player();
        player.setName("mike");
        player.setLoggedIn(true);
        when(playerStore.getBySessionId("session id")).thenReturn(player);
        when(httpSession.getAttribute("player")).thenReturn(player);
        when(httpSession.getId()).thenReturn("session id");

        loginService.logout();

        verify(playerStore).updatePlayer(playerCaptor.capture());
        assertThat(playerCaptor.getValue().isLoggedIn(), is(false));
    }

    @Test (expected = IllegalArgumentException.class)
    public void onLogoutAnIllegalArgumentExceptionShouldBeThrownIfNameDoesNotMatchTheSession() {
        Player player = new Player();
        player.setName("mike");
        player.setLoggedIn(false);
        Player hacker = new Player();
        hacker.setName("hacker");
        hacker.setLoggedIn(true);
        when(httpSession.getId()).thenReturn("session id");
        when(playerStore.getBySessionId("session id")).thenReturn(player);
        when(httpSession.getAttribute("player")).thenReturn(hacker);

        loginService.logout();

        //exception expected
    }

    @Test (expected = IllegalArgumentException.class)
    public void loginFromServerSessionShouldBeWaryOfPlayersThatDontMatchTheSavedData() {
        Player player = new Player();
        player.setName("mike");
        player.setLoggedIn(false);
        Player hacker = new Player();
        hacker.setName("hacker");
        hacker.setLoggedIn(true);
        when(httpSession.getId()).thenReturn("session id");
        when(playerStore.getBySessionId("session id")).thenReturn(player);
        when(httpSession.getAttribute("player")).thenReturn(hacker);

        loginService.loginFromSessionServer();

        //exception expected
    }

    @Test
    public void loginFromServerShouldMakeThePlayerLoggedIn() {
        Player player = new Player();
        player.setName("mike");
        player.setLoggedIn(false);
        when(playerStore.getBySessionId("session id")).thenReturn(player);
        when(httpSession.getAttribute("player")).thenReturn(player);
        when(httpSession.getId()).thenReturn("session id");

        loginService.loginFromSessionServer();

        verify(playerStore).updatePlayer(playerCaptor.capture());
        assertThat(playerCaptor.getValue().isLoggedIn(), is(true));
    }

    @Test
    public void initialLoginShouldRegisterSessionWithPlayer() {
        Player player = new Player();
        player.setName("mike");
        when(playerStore.getHashPassword("mike")).thenReturn(BCrypt.hashpw("password", gensalt()));
        when(playerStore.getPlayer("mike")).thenReturn(player);
        when(httpSession.getId()).thenReturn("session id");

        loginService.loginServer("mike", "password");

        verify(playerStore).registerSession("mike", "session id");
    }

    @Test
    public void skipLoginFromServerIfNoPlayerInSession() {
        Player player = new Player();
        player.setName("mike");
        player.setLoggedIn(false);
        when(httpSession.getId()).thenReturn("session id");
        when(playerStore.getBySessionId("session id")).thenReturn(null);
        when(httpSession.getAttribute("player")).thenReturn(null);

        loginService.loginFromSessionServer();

        verify(playerStore, never()).updatePlayer(player);
    }

}
