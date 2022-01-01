package de.chaos.swlnmngr.config;

import de.chaos.swlnmngr.Main;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class Config {

    private static final File CONFIG_FILE = new File("config.json");

    public static final String[] ADDITIONAL_ARGS;

    static {
        JSONObject config = null;
        try {
            if(!CONFIG_FILE.exists()) {
                Files.write(CONFIG_FILE.toPath(), Config.class.getResourceAsStream("/default_config.json").readAllBytes(), StandardOpenOption.CREATE_NEW);
            }

            config = new JSONObject(new JSONTokener(new FileInputStream(CONFIG_FILE)));
        } catch (Exception e) {
            Main.getLogger().fatal("Could not load Config", e);
            System.exit(1);
        }

        ADDITIONAL_ARGS = config.getJSONArray("additionalArgs").toList().toArray(new String[0]);
    }

    public static void reset() {

    }
}
