package de.chaos.swlnmngr.route.routes;

import de.chaos.swlnmngr.MainKt;
import de.chaos.swlnmngr.config.Config;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LinkRoute implements Route {
    @Override
    public String getName() {
        return "link";
    }

    @Override
    public boolean route(String[] args) {
        if(args.length < 1) {
            MainKt.getLogger().error("Usage: swlnmngr [Project] {Libs}");
            return false;
        }
        String libs = args.length>1?args[1]:Config.DEFAULTS;
        File libsFile = new File(Config.LIB_PATH, libs);
        if(!libsFile.exists()) {
            MainKt.getLogger().error("No Libs with name: {}", libs);
            if(libs.equals(Config.DEFAULTS)) {
                MainKt.getLogger().info("To Create the {} Folder run 'swlnmngr update'", Config.DEFAULTS);
            }
            return false;
        }
        File projectDir = new File(Config.PROJECT_PATH, args[0]);
        if(!projectDir.exists()) {
            MainKt.getLogger().error("No Project with name: {}", args[0]);
            return false;
        }
        File link = new File(projectDir, "lib");
        try {
            if(link.exists()) {
                if(link.isDirectory()) {
                    FileUtils.deleteDirectory(link);
                } else {
                    FileUtils.delete(link);
                }
            }
            MainKt.getLogger().debug(libsFile);
            MainKt.getLogger().debug(link);
            Path linkPath = Files.createSymbolicLink(link.toPath(), libsFile.toPath());
            MainKt.getLogger().debug(linkPath);
        } catch (IOException e) {
            MainKt.getLogger().error("Could not Create SymLink", e);
            return false;
        }
        return true;
    }
}
