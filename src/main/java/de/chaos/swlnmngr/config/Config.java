package de.chaos.swlnmngr.config;

import de.chaos.swlnmngr.MainKt;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public class Config {

    private static File CONFIG_FILE = new File(CLIConfig.INSTALL_DIR, "config.json");

    public static final File PROJECT_PATH;
    public static final File LIB_PATH;
    public static final String DEFAULTS;
    public static final URI LIB_URL;
    public static final boolean PRE_RELEASES;

    public static final String USERNAME;
    public static final String PASSWORD;

    static {
        JSONObject config = null;
        try {
            if(!CONFIG_FILE.exists()) {
                config = new JSONObject(new JSONTokener(Objects.requireNonNull(Config.class.getResourceAsStream("/default_config.json"))));
            } else {
                config = new JSONObject(new JSONTokener(new FileInputStream(CONFIG_FILE)));
            }
        } catch (Exception e) {
            MainKt.getLogger().fatal("Could not load Config", e);
            System.exit(1);
        }

        PROJECT_PATH = new File(config.getString("projectPath"));
        LIB_PATH = new File(config.getString("libPath"));
        DEFAULTS = config.getString("defaultName");
        PRE_RELEASES = config.getBoolean("preReleases");
        try {
            LIB_URL = new URI(config.getString("libUrl"));
        } catch (URISyntaxException e) {
            MainKt.getLogger().error("libUrl is not a URL", e);
            System.exit(1);
            throw new SecurityException(e);
        }

        USERNAME = config.getString("username");
        PASSWORD = config.getString("password");
    }
}
