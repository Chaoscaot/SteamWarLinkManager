package de.chaos.swlnmngr.route.routes;

import de.chaos.swlnmngr.MainKt;
import de.chaos.swlnmngr.config.Config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class NewRoute implements Route {
    @Override
    public String getName() {
        return "new";
    }

    @Override
    public boolean route(String[] args) {
        if(args.length < 1) {
            MainKt.getLogger().error("Usage: swlnmngr [Name]");
            return false;
        }
        String name = args[0];
        File newFile = new File(Config.LIB_PATH, name);
        MainKt.getLogger().debug(newFile);
        if(newFile.exists()) {
            MainKt.getLogger().error("Directory {} already exists", name);
            return false;
        }
        newFile.mkdir();
        File defaultFile = new File(Config.LIB_PATH, Config.DEFAULTS);
        MainKt.getLogger().debug(defaultFile);
        try {
            for (File file : defaultFile.listFiles()) {
                MainKt.getLogger().debug(file);
                Files.createSymbolicLink(new File(newFile, file.getName()).toPath(), file.toPath());
            }
        } catch (IOException e) {
            MainKt.getLogger().error("Could not create SymLink", e);
            return false;
        }
        return true;
    }
}
