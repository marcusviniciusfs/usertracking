package com.rd.main;

import com.rd.UserTracking;
import com.rd.factory.UserTrackingFactory;
import com.rd.persistence.database.DatabaseService;
import com.rd.persistence.database.exception.DatabaseException;
//import org.apache.commons.lang3.exception.ExceptionUtils;
//import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.InputStream;

public class UserTrackingServletContextListener implements ServletContextListener {

    //private static final Logger LOGGER = Logger.getLogger(UserTrackingServletContextListener.class);

    @Override
    public final void contextDestroyed(final ServletContextEvent arg0) {
        System.out.println("ServletContextListener destroyed");
    }

    @Override
    public final void contextInitialized(final ServletContextEvent arg0) {
        final DatabaseService databaseService;
        try {
            InputStream inputStream = (ServletContext.class.getResourceAsStream("WEB-INF/hibernate.properties"));
            databaseService = new DatabaseService(UserTracking.class.getSimpleName(), inputStream);
        } catch (DatabaseException e) {
            //LOGGER.trace("", e);
            //LOGGER.error("", ExceptionUtils.getRootCause(e));
            return;
        }

        /*final UserTracking userTracking = */UserTrackingFactory.create(databaseService);
        //LOGGER.info(userTracking.getVersion());
    }
}
