package de.chaos.swlnmngr

import de.chaos.swlnmngr.config.cliConfig
import de.chaos.swlnmngr.route.printRoutes
import de.chaos.swlnmngr.route.route
import de.chaos.swlnmngr.route.routes.Route
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.LogManager.getLogger
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.core.config.Configurator
import kotlin.system.exitProcess

val logger = getLogger("de.chaos.swlnmngr.MainKt")!!

var allArgs : Array<String>? = null
get() = field!!

fun main(args: Array<String>) {
    allArgs = args
    logger.log(Level.INFO, "SteamWarLinkManager by Chaoscaot")
    if(cliConfig.isDebug) {
        logger.log(Level.INFO, "Debug mode enabled")
        Configurator.setLevel("de.chaos.swlnmngr.MainKt", Level.DEBUG)
    }
    if(!cliConfig.noUpdate) {
        checkForUpdates()
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
