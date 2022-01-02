package de.chaos.swlnmngr.route;

import de.chaos.swlnmngr.Main;
import de.chaos.swlnmngr.route.routes.*;

import java.util.*;

public class Router {

    private static final List<Route> ROUTES;

    static {
        ROUTES = List.of(new InstallRoute(), new UpdateRoute(), new LinkRoute(), new NewRoute());
    }

    public static void route(String[] args) {
        for (Route route : ROUTES) {
            if(route.getName().equalsIgnoreCase(args[0])) {
                String[] rArgs = new String[args.length - 1];
                System.arraycopy(args, 1, rArgs, 0, args.length - 1);
                Main.getLogger().info("Running: {}", route.getName());
                if(route.route(rArgs)) {
                    System.exit(0);
                } else {
                    System.exit(1);
                }
            }
        }
    }

    public static void printRoutes() {
        Main.getLogger().info("Available Routes: ");
        Main.getLogger().info("\t{}", ROUTES.stream().map(Route::getName).reduce((s, s2) -> s + ", " + s2).get());
    }
}
