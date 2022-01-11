package de.chaos.swlnmngr

import de.chaos.swlnmngr.config.Config
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import java.io.IOException
import java.net.URI
import kotlin.system.exitProcess

class UpdateChecker {
    companion object {
        @JvmStatic
        val repoURL :URI = URI.create("https://steamwar.de/devlabs/repos/Chaoscaot/SteamwarLinkManager/releases")

        @JvmStatic
        public val CURRENT_VERSION :String;

        init {
            try {
                CURRENT_VERSION = String(UpdateChecker::class.java.getResourceAsStream("/jar.version").readAllBytes());
            } catch (e :IOException) {
                Main.getLogger().error("Could not read Version", e)
                exitProcess(1)
                throw SecurityException(e)
            }
        }

        @JvmStatic
        fun checkForUpdates() {
            val client :CloseableHttpClient = HttpClients.createMinimal()
            client.use { client: CloseableHttpClient -> {
                val get = HttpGet(repoURL);
                val response :CloseableHttpResponse = client.execute(get)
                response.use { response: CloseableHttpResponse -> {

                }}
            }}
        }
    }
}

/*
private static final URI repoUrl = URI.create("https://steamwar.de/devlabs/api/v1/repos/Chaoscaot/SteamwarLinkManager/releases?draft=false&pre-release=" + Config.PRE_RELEASES + "&page=1&limit=1");

    public static final String CURRENT_VERSION;

    static {
        try {
            CURRENT_VERSION = new String(UpdateChecker.class.getResourceAsStream("/jar.version").readAllBytes());
        } catch (IOException e) {
            Main.getLogger().error("Could not read Version", e);
            System.exit(1);
            throw new SecurityException(e);
        }
    }

    public static void checkForUpdates() {
        try (CloseableHttpClient client = HttpClients.createMinimal()) {
            HttpGet get = new HttpGet(repoUrl);
            try (CloseableHttpResponse response = client.execute(get)) {
                JSONArray array = new JSONArray(new JSONTokener(response.getEntity().getContent()));
                JSONObject latestObject = array.getJSONObject(0);
                String latestVersion = latestObject.getString("tag_name");
                if(!latestVersion.equals(CURRENT_VERSION)) {
                    Main.getLogger().info("The Running Jar is not the Latest release Version\n\tYour Version: {}\n\tLatest Release Version: {}", CURRENT_VERSION, latestVersion);
                    Main.getLogger().info("Download the Latest Jar here: https://steamwar.de/devlabs/Chaoscaot/SteamwarLinkManager/releases");
                }
            }
        } catch (IOException e) {
            Main.getLogger().error("Could not fetch Updates", e);
        }
    }
 */