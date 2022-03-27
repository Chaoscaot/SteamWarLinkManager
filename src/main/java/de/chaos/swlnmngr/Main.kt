package de.chaos.swlnmngr

import de.chaos.swlnmngr.config.cliConfig
import de.chaos.swlnmngr.route.printRoutes
import de.chaos.swlnmngr.route.route
import de.chaos.swlnmngr.route.routes.Route
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager.getLogger
import org.apache.logging.log4j.core.config.Configurator
import kotlin.system.exitProcess

val logger = getLogger(Route::javaClass)
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
        if(route(cliConfig.args)) {
            exitProcess(0)
        } else {
            exitProcess(1)
        }
    } else {
        printRoutes()
    }
}
