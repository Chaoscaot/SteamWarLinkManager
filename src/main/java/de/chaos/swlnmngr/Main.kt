package de.chaos.swlnmngr

import de.chaos.swlnmngr.config.cliConfig
import de.chaos.swlnmngr.route.printRoutes
import de.chaos.swlnmngr.route.route
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager.getLogger
import org.apache.logging.log4j.core.config.Configurator

val logger = getLogger()
get() = field!!

var allArgs : Array<String>? = null
get() = field!!

fun main(args: Array<String>) {
    logger.log(Level.INFO, "SteamWarLinkManager by Chaoscaot")
    allArgs = args
    if(cliConfig.isDebug) {
        Configurator.setRootLevel(Level.DEBUG)
    }
    if(cliConfig.noUpdate) {
        UpdateChecker().checkForUpdates()
    }
    logger.debug("Arguments: {}", if (args.isNotEmpty()) args.reduce { acc, s ->  "$acc $s"} else "")
    if(cliConfig.args.isNotEmpty()) {
        route(cliConfig.args)
    } else {
        printRoutes()
    }
}
