package de.chaos.swlnmngr.config;

import de.chaos.swlnmngr.Main;
import org.apache.commons.cli.*;

public class CLIConfig {

    public static final boolean IS_DEBUG;
    public static final boolean UPDATE;
    public static final String[] ARGS;

    static {
        Options options = new Options();
        options.addOption(new Option("d", "debug", false, "Set the Log Level to Debug"));
        options.addOption(new Option("u", "update", false, "Update the Default libs"));

        CommandLine cli = null;
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            cli = parser.parse(options, Main.getAllArgs());
        } catch (ParseException e) {
            Main.getLogger().error(e.getMessage());
            formatter.printHelp("swlnmngr", options, true);
            System.exit(1);
        }

        ARGS = cli.getArgs();
        IS_DEBUG = cli.hasOption("d");
        UPDATE = cli.hasOption("u");
    }
}
