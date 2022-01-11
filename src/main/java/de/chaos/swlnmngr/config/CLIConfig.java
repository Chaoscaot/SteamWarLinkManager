package de.chaos.swlnmngr.config;

import de.chaos.swlnmngr.Main;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class CLIConfig {

    public static final boolean IS_DEBUG;
    public static final boolean NO_UPDATE;
    public static final File CONFIG;
    public static final File INSTALL_DIR;
    public static final String[] ARGS;
    public static final boolean INSTALL_DIR_IS_SET;

    static {
        Options options = new Options();
        options.addOption(new Option("h", "help", false, "Display this Message"));
        options.addOption(new Option("d", "debug", false, "Set the Log Level to Debug"));
        options.addOption(new Option("c", "config", true, "Use another Config File"));
        options.addOption(new Option("i", "installdir", true, "Use other Install Dir"));
        options.addOption(new Option("u", "update-checker", false, "Enable the Auto-Update Checker"));

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

        if(cli.hasOption("h")) {
            formatter.printHelp("swlnmngr", options, true);
            System.exit(0);
        }

        ARGS = cli.getArgs();
        IS_DEBUG = cli.hasOption("d");
        NO_UPDATE = cli.hasOption("u");
        if(cli.hasOption("i")) {
            INSTALL_DIR = new File(cli.getOptionValue("i"));
            INSTALL_DIR_IS_SET = true;
        } else {
            try {
                INSTALL_DIR = new File(CLIConfig.class.getProtectionDomain()
                        .getCodeSource()
                        .getLocation()
                        .toURI()
                        .getPath())
                        .getCanonicalFile()
                        .getParentFile();
                INSTALL_DIR_IS_SET = false;
            } catch (IOException | URISyntaxException e) {
                Main.getLogger().error(e.getMessage(), e);
                throw new SecurityException(e);
            }
        }
        if(cli.hasOption("c")) {
            CONFIG = new File(cli.getOptionValue("c"));
        } else {
            CONFIG = new File(INSTALL_DIR, "config.json");
        }
    }
}
