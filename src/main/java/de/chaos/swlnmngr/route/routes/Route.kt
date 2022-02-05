package de.chaos.swlnmngr.route.routes

interface Route {
    fun route(args :Array<out String>) :Boolean

    fun getName() :String
}