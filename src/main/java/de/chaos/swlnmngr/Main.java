package de.chaos.swlnmngr;

import de.chaos.swlnmngr.config.CLIConfig;
import de.chaos.swlnmngr.config.Config;
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
        allArgs = new String[args.length + Config.ADDITIONAL_ARGS.length];
        System.arraycopy(args, 0, allArgs, 0, args.length);
        System.arraycopy(Config.ADDITIONAL_ARGS, 0, allArgs, args.length, Config.ADDITIONAL_ARGS.length);
        if(CLIConfig.IS_DEBUG) {
            Configurator.setRootLevel(Level.DEBUG);
        }
        logger.debug("Arguments: {}", Arrays.toString(allArgs));
    }
}
