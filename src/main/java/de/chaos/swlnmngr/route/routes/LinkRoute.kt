package de.chaos.swlnmngr.route.routes

import de.chaos.swlnmngr.config.config
import de.chaos.swlnmngr.logger
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.file.Files

class LinkRoute :Route {
    override fun getName(): String = "link"

    override fun route(args: Array<out String>): Boolean {
        if(args.isEmpty()) {
            logger.error(("Usage: swlnmngr [Project] {Libs}"))
            return false
        }

        val libs = args.getOrNull(1) ?: config.default
        val libsFile = File(config.libPath, libs)
        if(!libsFile.exists()) {
            logger.error("No Libs with name: $libs")
            if(libs == config.default) {
                logger.info("To Create the ${config.default} Folder run 'swlnmngr update'");
            }
            return false
        }
        val projectDir = File(config.projectPath, args[0])
        if (!projectDir.exists()) {
            logger.error("No Project with name: ${args[0]}")
            return false
        }
        val link = File(projectDir, "lib")
        try {
            if(link.exists()) {
                if(link.isDirectory) {
                    FileUtils.deleteDirectory(link)
                } else {
                    FileUtils.delete(link)
                }
            }

            logger.debug(libsFile)
            logger.debug(link)
            val linkPath = Files.createSymbolicLink(link.toPath(), libsFile.toPath())
            logger.debug(linkPath)
        } catch (e :IOException) {
            logger.error("Could not create SymLink", e)
            return false
        }
        return true
    }
}
