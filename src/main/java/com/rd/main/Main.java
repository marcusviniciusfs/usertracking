package com.rd.main;

import com.rd.UserTracking;
import com.rd.factory.UserTrackingFactory;
import com.rd.persistence.database.DatabaseService;
import com.rd.persistence.database.exception.DatabaseException;
import com.rd.persistence.database.migration.DatabaseMigrationService;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.io.InputStream;

enum CommandLineOption {

    HELP(Option.builder("h").longOpt("help").build()),

    MIGRATE_DB(Option.builder("m").longOpt("migrate-db").build()),

    MIGRATE_DB_DRY_RUN(Option.builder("d").longOpt("migrate-db-dry-run").build());

    private final Option o;

    CommandLineOption(final Option option) {
        o = option;
    }

    final boolean isInCMD(final CommandLine commandLine) {
        return commandLine.hasOption(o.getOpt());
    }

    final Option option() {
        return o;
    }
}

public final class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static final String CMD_LINE_SYNTAX = "[option]";

    private Main() {
    }

    public static void main(final String[] args) {

        final DefaultParser defaultParser = new DefaultParser();

        final OptionGroup optionGroup = new OptionGroup()
                .addOption(CommandLineOption.HELP.option())
                .addOption(CommandLineOption.MIGRATE_DB.option())
                .addOption(CommandLineOption.MIGRATE_DB_DRY_RUN.option());

        optionGroup.setRequired(false);

        final Options options = new Options()
                .addOptionGroup(optionGroup);

        final CommandLine commandLine;
        try {
            commandLine = defaultParser.parse(options, args);
        } catch (ParseException e) {
            LOGGER.trace("", e);
            LOGGER.error("{}\n", e.getMessage());
            printHelp(options);
            return;
        }

        if (CommandLineOption.HELP.isInCMD(commandLine)) {
            printHelp(options);
        } else if (CommandLineOption.MIGRATE_DB.isInCMD(commandLine)) {
            new DatabaseMigrationService().execute(false);
        } else if (CommandLineOption.MIGRATE_DB_DRY_RUN.isInCMD(commandLine)) {
            new DatabaseMigrationService().execute(true);
        } else {
            start();
        }
    }

    private static void printHelp(final Options options) {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(CMD_LINE_SYNTAX, "Options:", options, null);
    }

    private static void start() {
        final DatabaseService databaseService;
        try {
            InputStream inputStream = (ServletContext.class.getResourceAsStream("WEB-INF/hibernate.properties"));
            databaseService = new DatabaseService(UserTracking.class.getSimpleName(), inputStream);
        } catch (DatabaseException e) {
            LOGGER.trace("", e);
            LOGGER.error("", ExceptionUtils.getRootCause(e));
            return;
        }

        final UserTracking userTracking = UserTrackingFactory.create(databaseService);
        LOGGER.info(userTracking.getVersion());
    }
}
