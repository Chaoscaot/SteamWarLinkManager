package de.chaos.swlnmngr.route.routes;

import de.chaos.swlnmngr.Main;
import de.chaos.swlnmngr.config.CLIConfig;
import org.apache.commons.lang.SystemUtils;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

public class InstallRoute implements Route {

    private static final String[] defaultFiles = new String[] {"default_swlnmngr.bat", "default_swlnmngr.sh"};

    @Override
    public String getName() {
        return "install";
    }

    @Override
    public boolean route(String[] args) {
        File installDir = CLIConfig.INSTALL_DIR;
        if(!installDir.exists()) {
            installDir.mkdir();
        }

        for (String defaultFile : defaultFiles) {
            String normalName = defaultFile.replace("default_", "");
            try {
                Files.copy(Objects.requireNonNull(InstallRoute.class.getResourceAsStream("/" + defaultFile)), new File(CLIConfig.INSTALL_DIR, normalName).toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                Main.getLogger().error("Could not create File", e);
                return false;
            }
        }

        if(SystemUtils.IS_OS_UNIX) {
            try {
                Files.deleteIfExists(new File(CLIConfig.INSTALL_DIR, "swlnmngr").toPath());
                Files.createSymbolicLink(new File(CLIConfig.INSTALL_DIR, "swlnmngr").toPath(), new File(CLIConfig.INSTALL_DIR, "swlnmngr.sh").toPath());
            } catch (IOException e) {
                Main.getLogger().error("Could not create SymLink", e);
                return false;
            }
        }

        File configFile = new File(installDir, "config.json");
        if(!configFile.exists()) {
            try {
                Files.copy(Objects.requireNonNull(InstallRoute.class.getResourceAsStream("/default_config.json")), configFile.toPath());
            } catch (IOException e) {
                Main.getLogger().error("Could not copy Config File", e);
                return false;
            }
        } else {
            try {
                JSONObject defaultConfig = new JSONObject(new JSONTokener(Objects.requireNonNull(InstallRoute.class.getResourceAsStream("/default_config.json"))));
                Main.getLogger().info(defaultConfig);
                JSONObject currentConfig = new JSONObject(Files.readString(configFile.toPath()));
                Main.getLogger().info(currentConfig);
                for (String s : defaultConfig.keySet()) {
                    if(currentConfig.has(s)) continue;
                    currentConfig.put(s, defaultConfig.get(s));
                }
                Files.writeString(configFile.toPath(),
                        currentConfig.toString(2),
                        StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException e) {
                Main.getLogger().error("Could write Config File", e);
                return false;
            }
        }

        try {
            File jar = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
            Main.getLogger().debug(jar);
            Files.copy(jar.toPath(), new File(CLIConfig.INSTALL_DIR, "SteamWarLinkManager.jar").toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (URISyntaxException e) {
            Main.getLogger().error("Could parse Jar Location", e);
            return false;
        } catch (IOException e) {
            Main.getLogger().error("Could not Copy JarFile", e);
            return false;
        }
        return true;
    }
}
