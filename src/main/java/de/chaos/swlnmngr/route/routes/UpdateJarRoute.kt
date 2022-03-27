package de.chaos.swlnmngr.route.routes

import de.chaos.swlnmngr.config.cliConfig
import de.chaos.swlnmngr.downloadToFile
import de.chaos.swlnmngr.logger
import de.chaos.swlnmngr.updateAvailable
import kotlin.system.exitProcess

class UpdateJarRoute: Route {
    override fun route(args: Array<out String>): Boolean {
        val updateAvailable = updateAvailable()
        if(!updateAvailable.updateAvailable && !cliConfig.force) {
            logger.warn("No update available")
            return false
        }
        val file = downloadToFile();
        logger.info("Update downloaded!")
        logger.info("Restart to apply update")
        return true
    }

    override fun getName(): String = "updateJar"
}