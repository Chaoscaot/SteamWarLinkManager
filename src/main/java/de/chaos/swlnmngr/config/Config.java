package de.chaos.swlnmngr.config;

import de.chaos.swlnmngr.Main;
import org.apache.logging.log4j.Level;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class Config {

    private static final File CONFIG_FILE = new File(CLIConfig.INSTALL_DIR, "config.json");

    public static final String PROJECT_PATH;
    public static final File LIB_PATH;
    public static final String DEFAULTS;
    public static final URI LIB_URL;

    public static final String USERNAME;
    public static final String PASSWORD;

    static {
        JSONObject config = null;
        try {
            if(!CONFIG_FILE.exists()) {
                if(CONFIG_FILE.getParentFile().mkdir()) {
                    Main.getLogger().log(Level.DEBUG, "Created Config Folder");
                }
                Files.write(CONFIG_FILE.toPath(), Config.class.getResourceAsStream("/default_config.json").readAllBytes(), StandardOpenOption.CREATE_NEW);
            }

            config = new JSONObject(new JSONTokener(new FileInputStream(CONFIG_FILE)));
        } catch (Exception e) {
            Main.getLogger().fatal("Could not load Config", e);
            System.exit(1);
        }

        PROJECT_PATH = config.getString("projectPath");
        LIB_PATH = new File(config.getString("libPath"));
        DEFAULTS = config.getString("defaultName");
        try {
            LIB_URL = new URI(config.getString("libUrl"));
        } catch (URISyntaxException e) {
            Main.getLogger().error("libUrl is not a URL", e);
            System.exit(1);
            throw new SecurityException(e);
        }

        JSONObject credentials = config.getJSONObject("credentials");
        USERNAME = credentials.getString("username");
        PASSWORD = credentials.getString("password");
    }
}
