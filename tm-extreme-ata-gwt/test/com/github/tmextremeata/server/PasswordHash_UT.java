package com.github.tmextremeata.server;

import com.github.tmextremeata.server.util.BCrypt;
import org.junit.Test;

import static com.github.tmextremeata.server.util.BCrypt.gensalt;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 * User: Tim King
 * Date: 2/25/12
 * Time: 1:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class PasswordHash_UT {
    @Test
    public void hashPasswordShouldMatchInput() {
        String password = "guest";
        String hash = BCrypt.hashpw(password, gensalt());
        boolean unHashedPassword = BCrypt.checkpw(password, hash);
        assertTrue(unHashedPassword);
    }

    @Test
    public void hashPasswordShouldNotMatchInput() {
        String hash = BCrypt.hashpw("guest", gensalt());
        String password = "fart";
        boolean unHashedPassword = BCrypt.checkpw(password, hash);
        assertFalse(unHashedPassword);
    }
}
