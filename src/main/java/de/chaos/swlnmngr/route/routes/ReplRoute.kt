package de.chaos.swlnmngr.route.routes

import de.chaos.swlnmngr.logger
import java.util.Scanner

class ReplRoute: Route {
    override fun route(args: Array<out String>): Boolean {
        logger.info("Starting REPL")
        print(">>> ")
        Scanner(System.`in`).use {
            while (it.hasNextLine()) {
                val line = it.nextLine()
                if(line == "exit") {
                    logger.info("Goodbye!")
                    return true
                }
                logger.info(line)
                de.chaos.swlnmngr.route.route(line.split(" ").toTypedArray())
                print(">>> ")
            }
        }
        return true
    }

    override fun getName(): String = "repl"
}