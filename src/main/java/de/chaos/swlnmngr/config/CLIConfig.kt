package de.chaos.swlnmngr.config

import de.chaos.swlnmngr.allArgs
import de.chaos.swlnmngr.logger
import org.apache.commons.cli.*
import java.io.File
import kotlin.system.exitProcess

data class CLIConfig(
    val isDebug: Boolean,
    val noUpdate: Boolean,
    val config: File,
    val installDir: File,
    val args: Array<String>,
    val installDirSet: Boolean,
    val force: Boolean
)

val cliConfig: CLIConfig by lazy {
    val options = Options()
    options.addOption(Option("h", "help", false, "Display this Message"))
    options.addOption(Option("d", "debug", false, "Set the Log Level to Debug"))
    options.addOption(Option("c", "config", true, "Use another Config File"))
    options.addOption(Option("i", "installdir", true, "Use other Install Dir"))
    options.addOption(Option("nu", "no-update", false, "Disable the Auto Update-Check"))
    options.addOption(Option("f", "force", false, "Force Something"))
    options.addOption(Option(null, "update-the-fucking-jar-please", false, "Self Describing"))

    var cli :CommandLine? = null
    val parser = DefaultParser()
    val formatter = HelpFormatter()
    try {
        cli = parser.parse(options, allArgs)
    } catch (e :ParseException) {
        logger.error(e.message)
        formatter.printHelp("swlnmngr", options, true)
        exitProcess(1)
    }

    if(cli.hasOption("h")) {
        formatter.printHelp("swlnmngr", options, true)
        exitProcess(0)
    }

    val args = cli.args
    val isDebug = cli.hasOption("d")
    val noUpdate = cli.hasOption("nu")
    val installDirSet = cli.hasOption("i")
    val force = cli.hasOption("f")
    val installDir = if(cli.hasOption("i")) File(cli.getOptionValue("i")) else {
        try {
            File(
                CLIConfig::class.java.protectionDomain
                .codeSource
                .location
                .toURI()
                .path)
                .canonicalFile
                .parentFile
        } catch (e :Exception) {
            logger.error(e.message, e)
            throw SecurityException(e)
        }
    }
    val config = if(cli.hasOption("c")) File(cli.getOptionValue("c")) else File(installDir, "config.json")

    CLIConfig(isDebug, noUpdate, config, installDir, args, installDirSet, force)
}
