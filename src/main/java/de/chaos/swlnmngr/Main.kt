package de.chaos.swlnmngr

import de.chaos.swlnmngr.config.CLIConfig
import de.chaos.swlnmngr.route.Router
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.config.Configurator

val logger = LogManager.getLogger()

var allArgs : Array<String>? = null

fun main(args: Array<String>) {
    logger.log(Level.INFO, "SteamWarLinkManager by Chaoscaot")
    allArgs = args
    if(CLIConfig.IS_DEBUG) {
        Configurator.setRootLevel(Level.DEBUG)
    }
    if(CLIConfig.NO_UPDATE) {
        UpdateChecker().checkForUpdates()
    }
    logger.debug("Arguments: {}", if (args.isNotEmpty()) args.reduce { acc, s ->  "$acc $s"} else "")
    if(CLIConfig.ARGS.isNotEmpty()) {
        Router().route(CLIConfig.ARGS)
    } else {
        Router().printRoutes()
    }
}