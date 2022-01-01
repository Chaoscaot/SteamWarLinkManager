package de.chaos.swlnmngr.route.routes;

public interface Route {
    String getName();

    boolean route(String[] args);
}
