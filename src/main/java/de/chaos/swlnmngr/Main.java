package de.chaos.swlnmngr;

import de.chaos.swlnmngr.config.CLIConfig;
import de.chaos.swlnmngr.route.Router;
import lombok.Getter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.util.Arrays;

public class Main {

    @Getter
    private static final Logger logger = LogManager.getLogger(Main.class);
    @Getter
    private static String[] allArgs;

    public static void main(String[] args) {
        logger.log(Level.INFO, "SteamWarLinkManager by Chaoscaot");
        allArgs = args;
        if(CLIConfig.IS_DEBUG) {
            Configurator.setRootLevel(Level.DEBUG);
        }
        if(!CLIConfig.NO_UPDATE) {
            UpdateChecker.checkForUpdates();
        }
        logger.debug("Arguments: {}", Arrays.toString(allArgs));
        if(CLIConfig.ARGS.length > 0) {
            Router.route(CLIConfig.ARGS);
        } else {
            Router.printRoutes();
        }
    }
}
