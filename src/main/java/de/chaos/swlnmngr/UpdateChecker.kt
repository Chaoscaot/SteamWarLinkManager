package de.chaos.swlnmngr

import de.chaos.swlnmngr.config.CLIConfig
import de.chaos.swlnmngr.config.cliConfig
import de.chaos.swlnmngr.route.routes.Route
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import java.io.File
import java.io.IOException
import java.net.URI
import java.net.URL
import kotlin.system.exitProcess
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import kotlin.math.log

private val repoURL :URI = URI.create("https://github.com/Chaoscaot/SteamwarLinkManager/releases/latest")
private val download = URI.create("$repoURL/download/SteamWarLinkManager.jar")

private val CURRENT_VERSION :String by lazy {
    try {
        return@lazy String(Route::class.java.getResourceAsStream("/jar.version")!!.readAllBytes())
    } catch (e :IOException) {
        logger.error("Could not read Version", e)
        exitProcess(1)
    }
}

data class UpdateInfo(val updateAvailable: Boolean, val version: String)

fun updateAvailable(): UpdateInfo {
    val httpClient = HttpClients.custom().disableCookieManagement().disableRedirectHandling().build()
    val httpGet = HttpGet(repoURL)
    val response = httpClient.execute(httpGet)
    if(cliConfig.isDebug) {
        logger.debug("Response Code: ${response.statusLine.statusCode}")
        response.allHeaders.forEach {
            logger.debug("${it.name}: ${it.value}")
        }
    }
    val latestVersion = response.getFirstHeader("location").value.split("/").last()
    response.close()
    httpClient.close()
    return UpdateInfo(latestVersion != CURRENT_VERSION, latestVersion)
}

fun checkForUpdates() {
    val updateInfo = updateAvailable()
    if (updateInfo.updateAvailable) {
        logger.info("The Running Jar is not the Latest release Version\n\tYour Version: {}\n\tLatest Release Version: {}",
                CURRENT_VERSION, updateInfo.version)
        logger.info("Run the Jar with `updatejar` to update to the latest Version")
    }
}

fun downloadToFile(): File {
    Files.newOutputStream(File(cliConfig.installDir, "SteamWarLinkManager-new.jar").toPath(), StandardOpenOption.CREATE_NEW, StandardOpenOption.TRUNCATE_EXISTING).use {
        URL(download.toString()).openStream().transferTo(it)
        it.flush()
    }
    return File(cliConfig.installDir, "SteamWarLinkManager-new.jar")
}
