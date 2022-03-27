package de.chaos.swlnmngr.route

import de.chaos.swlnmngr.logger
import de.chaos.swlnmngr.route.routes.*
import org.apache.logging.log4j.Level
import kotlin.system.exitProcess

val ROUTES :List<Route> = listOf(InstallRoute(), UpdateRoute(), LinkRoute(), NewRoute(), ReplRoute(), UpdateJarRoute())

fun route(args: Array<String>): Boolean {
    for (route in ROUTES) {
        if(route.getName().lowercase() == args[0].lowercase()) {
            val rArgs :Array<String> = Array(args.size - 1) { i: Int -> args[i + 1] }
            logger.info("Running: {}", route.getName())
            return route.route(rArgs)
        }
    }
    return false;
}

fun printRoutes() {
    logger.log(Level.INFO, "Available Routes: ")
    logger.log(Level.INFO, "\t{}", ROUTES.map { route -> route.getName() }.reduce { acc, s -> "$acc, $s" })
}