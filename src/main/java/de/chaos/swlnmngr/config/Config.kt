package de.chaos.swlnmngr.config

import de.chaos.swlnmngr.logger
import org.json.JSONObject
import org.json.JSONTokener
import java.io.File
import java.io.FileInputStream
import java.net.URI
import java.net.URISyntaxException
import kotlin.system.exitProcess

data class Config(val projectPath :File, val libPath :File, val default :String, val libUrl :URI, val userName :String, val password :String)

val config :Config by lazy {
    val conf = try {
        if(!cliConfig.config.exists()) {
            JSONObject(JSONTokener(config.javaClass.getResourceAsStream("/default_config.json")))
        } else {
            JSONObject(JSONTokener(FileInputStream(cliConfig.config)));
        }
    } catch (e :Exception) {
        logger.fatal("Could not load Config", e)
        exitProcess(1)
    }

    val projectPath = File(conf.getString("projectPath"))
    val libPath = File(conf.getString("libPath"))
    val default = conf.getString("defaultName")
    val libUrl = try {
        URI(conf.getString("libUrl"))
    } catch (e :URISyntaxException) {
        logger.error("libUrl is not a URL", e)
        exitProcess(1)
    }

    val userName = conf.getString("username")
    val password = conf.getString("password")

    Config(projectPath, libPath, default, libUrl, userName, password)
}
