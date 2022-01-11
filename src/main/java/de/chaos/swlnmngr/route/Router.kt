package de.chaos.swlnmngr.route

import de.chaos.swlnmngr.Main
import de.chaos.swlnmngr.route.routes.*
import org.apache.logging.log4j.Level
import kotlin.system.exitProcess

class Router {

    companion object {
        @JvmStatic
        val ROUTES :List<Route> = listOf(InstallRoute(), UpdateRoute(), LinkRoute(), NewRoute())

        @JvmStatic
        fun route(args: Array<String>) {
            for (route in ROUTES) {
                if(route.name.lowercase() == args[0].lowercase()) {
                    val rArgs :Array<String> = Array(args.size - 1) { i: Int -> args[i + 1] }
                    Main.getLogger().info("Running: {}", route.name)
                    if(route.route(rArgs)) {
                        exitProcess(0)
                    } else {
                        exitProcess(1)
                    }
                }
            }
        }

        @JvmStatic
        fun printRoutes() {
            Main.getLogger().log(Level.INFO, "Available Routes: ")
            Main.getLogger().log(Level.INFO, "\t{}", ROUTES.map { route -> route.name }.reduce { acc, s -> "$acc, $s" })
        }
    }
}
