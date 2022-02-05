package de.chaos.swlnmngr.route.routes

import de.chaos.swlnmngr.config.config
import de.chaos.swlnmngr.logger
import org.apache.commons.io.FileUtils.listFiles
import org.apache.commons.io.filefilter.FileFilterUtils
import java.io.File
import java.nio.file.Files

class NewRoute :Route{
    override fun getName(): String = "new"

    override fun route(args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            logger.error("Usage: swlnmngr new [Name]")
            return false
        }

        val name = args.component1()
        val newFile = File(config.libPath, name)
        logger.debug(newFile)
        if(newFile.exists()) {
            logger.error("Directory {} already exists", name)
            return false
        }

        newFile.mkdir()
        val defaultFile = File(config.libPath, config.default)
        logger.debug(defaultFile)
        for (file in listFiles(defaultFile, FileFilterUtils.trueFileFilter(), FileFilterUtils.trueFileFilter())) {
            logger.debug(file)
            Files.createSymbolicLink(File(newFile, file.name).toPath(), file.toPath())
        }

        return true
    }

}
