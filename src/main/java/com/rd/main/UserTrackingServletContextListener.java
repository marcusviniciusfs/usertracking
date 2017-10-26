package com.rd.main;

import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class UserTrackingServletContextListener implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(UserTrackingServletContextListener.class);

    @Override
    public final void contextDestroyed(final ServletContextEvent arg0) {
        System.out.println("ServletContextListener destroyed");
    }

    @Override
    public final void contextInitialized(final ServletContextEvent arg0) {
        LOGGER.info("ServletContextListener started");
        System.out.println("ServletContextListener started");
    }
}
