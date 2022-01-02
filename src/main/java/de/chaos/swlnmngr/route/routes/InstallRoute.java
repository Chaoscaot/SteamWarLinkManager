package de.chaos.swlnmngr.route.routes;

import com.sun.nio.file.ExtendedCopyOption;
import de.chaos.swlnmngr.Main;
import de.chaos.swlnmngr.config.CLIConfig;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class InstallRoute implements Route {

    private static final String[] defaultFiles = new String[] {"default_config.json", "default_swlnmngr.bat", "default_swlnmngr.sh"};

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
