package com.github.tmextremeata.server;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

public class PersistenceFactory {

    private static final PersistenceManagerFactory pmfInstance =
        JDOHelper.getPersistenceManagerFactory("transactions-optional");

    private PersistenceFactory() { }

    public static PersistenceManagerFactory get() {
        return pmfInstance;
    }


}
