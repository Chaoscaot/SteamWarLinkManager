package de.chaos.swlnmngr.route.routes

import de.chaos.swlnmngr.config.cliConfig
import de.chaos.swlnmngr.logger
import org.apache.commons.lang.SystemUtils
import org.json.JSONObject
import org.json.JSONTokener
import java.io.File
import java.io.IOException
import java.net.URISyntaxException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.nio.file.StandardOpenOption
import java.util.*

class InstallRoute : Route {

    override fun getName(): String {
        return "install"
    }

    override fun route(args: Array<out String>): Boolean {
        var installDir: File = cliConfig.installDir
        if (!cliConfig.installDirSet) {
            installDir = File(System.getProperty("user.home", ".swlnmngr"))
        }
        if (!installDir.exists()) {
            try {
                Files.createDirectories(installDir.toPath())
            } catch (e: IOException) {
                logger.error("Could not create Install Directory", e)
                return false
            }
        }
        for (defaultFile in arrayOf("default_swlnmngr.bat", "default_swlnmngr.sh", "default_swlnmngr_admin.bat")) {
            val normalName = defaultFile.replace("default_", "")
            try {
                Files.copy(
                    Objects.requireNonNull(InstallRoute::class.java.getResourceAsStream("/$defaultFile")),
                    File(installDir, normalName).toPath(),
                    StandardCopyOption.REPLACE_EXISTING
                )
                File(installDir, normalName).setExecutable(true, true)
            } catch (e: IOException) {
                logger.error("Could not create File", e)
                return false
            }
        }
        if (SystemUtils.IS_OS_UNIX) {
            try {
                Files.deleteIfExists(File(installDir, "swlnmngr").toPath())
                Files.createSymbolicLink(
                    File(installDir, "swlnmngr").toPath(),
                    File(installDir, "swlnmngr.sh").toPath()
                )
            } catch (e: IOException) {
                logger.error("Could not create SymLink", e)
                return false
            }
        } else if (SystemUtils.IS_OS_WINDOWS) {
            try {
                Files.writeString(
                    File(installDir, "swlnmngr.bat").toPath(),
                    Files.readString(File(installDir, "swlnmngr.bat").toPath())
                        .replace("\${iDir}", installDir.absolutePath),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.TRUNCATE_EXISTING
                )
                Files.writeString(
                    File(installDir, "swlnmngr_admin.bat").toPath(),
                    Files.readString(File(installDir, "swlnmngr_admin.bat").toPath())
                        .replace("\${iDir}", installDir.absolutePath),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.TRUNCATE_EXISTING
                )
            } catch (e: IOException) {
                logger.error("Could not create Link", e)
                return false
            }
        }
        val configFile = File(installDir, "config.json")
        if (!configFile.exists()) {
            try {
                var configStr = String(
                    Objects.requireNonNull(InstallRoute::class.java.getResourceAsStream("/default_config.json"))
                        .readAllBytes()
                )
                if (SystemUtils.IS_OS_WINDOWS) {
                    configStr = configStr.replace("~", System.getProperty("user.home"))
                        .replace("\\", "\\\\")
                        .replace("/", "\\\\")
                        .replace("https:\\\\\\\\steamwar.de\\\\lib.php", "https://steamwar.de/lib.php")
                }
                Files.writeString(configFile.toPath(), configStr, StandardOpenOption.CREATE_NEW)
            } catch (e: IOException) {
                logger.error("Could not copy Config File", e)
                return false
            }
        } else {
            try {
                val defaultConfig = JSONObject(
                    JSONTokener(
                        Objects.requireNonNull(
                            InstallRoute::class.java.getResourceAsStream("/default_config.json")
                        )
                    )
                )
                logger.info(defaultConfig)
                val currentConfig = JSONObject(Files.readString(configFile.toPath()))
                logger.info(currentConfig)
                for (s in defaultConfig.keySet()) {
                    if (currentConfig.has(s)) continue
                    currentConfig.put(s, defaultConfig[s])
                }
                Files.writeString(
                    configFile.toPath(),
                    currentConfig.toString(2),
                    StandardOpenOption.TRUNCATE_EXISTING
                )
            } catch (e: IOException) {
                logger.error("Could write Config File", e)
                return false
            }
        }
        try {
            val jar = File(javaClass.protectionDomain.codeSource.location.toURI())
            logger.debug(jar)
            Files.copy(
                jar.toPath(),
                File(installDir, "SteamWarLinkManager.jar").toPath(),
                StandardCopyOption.REPLACE_EXISTING
            )
        } catch (e: URISyntaxException) {
            logger.error("Could parse Jar Location", e)
            return false
        } catch (e: IOException) {
            logger.error("Could not Copy JarFile", e)
            return false
        }
        return true
    }
}
