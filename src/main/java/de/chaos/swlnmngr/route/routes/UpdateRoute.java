package de.chaos.swlnmngr.route.routes;

import de.chaos.swlnmngr.Main;
import de.chaos.swlnmngr.config.Config;
import org.apache.http.Header;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UpdateRoute implements Route {

    @Override
    public String getName() {
        return "update";
    }

    @Override
    public boolean route(String[] args) {
        File defaultLibs = new File(Config.LIB_PATH, Config.DEFAULTS);
        if (!defaultLibs.mkdirs()) {
            for (File file : defaultLibs.listFiles()) {
                file.delete();
            }
        }
        CredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(
                new AuthScope(Config.LIB_URL.getHost(), Config.LIB_URL.getPort()),
                new UsernamePasswordCredentials(Config.USERNAME, Config.PASSWORD));
        try (CloseableHttpClient client = HttpClients.custom()
                .setDefaultCredentialsProvider(provider)
                .build()) {
            HttpGet httpGet = new HttpGet(Config.LIB_URL);
            try (CloseableHttpResponse response = client.execute(httpGet)) {
                Main.getLogger().debug(response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());
                for (Header allHeader : response.getAllHeaders()) {
                    Main.getLogger().debug(allHeader.getName() + ": " + allHeader.getValue());
                }

                Main.getLogger().info("Writing libs...");

                ZipInputStream zis = new ZipInputStream(response.getEntity().getContent());
                byte[] buffer = new byte[2048];
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    Path filePath = defaultLibs.toPath().resolve(entry.getName());
                    Main.getLogger().debug("Inflating {}...", entry.getName());

                    try (FileOutputStream fos = new FileOutputStream(filePath.toFile());
                         BufferedOutputStream bos = new BufferedOutputStream(fos, buffer.length)) {

                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            bos.write(buffer, 0, len);
                        }
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
