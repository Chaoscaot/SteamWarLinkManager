package de.chaos.swlnmngr

import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import java.io.IOException
import java.net.URI
import kotlin.system.exitProcess

private val repoURL :URI = URI.create("https://github.com/Chaoscaot/SteamwarLinkManager/releases/latest")
private val download = URI.create("$repoURL/download/")

class UpdateChecker {
    private val CURRENT_VERSION :String

    init {
        try {
            CURRENT_VERSION = String(UpdateChecker::class.java.getResourceAsStream("/jar.version").readAllBytes())
        } catch (e :IOException) {
            getLogger().error("Could not read Version", e)
            exitProcess(1)
            throw SecurityException(e)
        }
    }

    fun checkForUpdates() {
        val client = HttpClients.custom().disableRedirectHandling().build()
        getLogger().debug("Checking for Updates...")
        val get = HttpGet(repoURL)
        getLogger().debug(get)
        val response = client.execute(get)

        getLogger().debug(response.statusLine.toString())
        val latest = response.getFirstHeader("Location").value.replaceFirst(".*/".toRegex(), "")
        if (latest != CURRENT_VERSION) {
            getLogger().info("The Running Jar is not the Latest release Version\n\tYour Version: {}\n\tLatest Release Version: {}",
                CURRENT_VERSION, latest)
            getLogger().info("Download the Latest Jar here: https://steamwar.de/devlabs/Chaoscaot/SteamwarLinkManager/releases/latest")
        }

        response.close()
        client.close()
    }
}
