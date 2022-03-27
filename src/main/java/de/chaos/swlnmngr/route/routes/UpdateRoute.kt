package de.chaos.swlnmngr.route.routes

import de.chaos.swlnmngr.config.config
import de.chaos.swlnmngr.logger
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.BasicCredentialsProvider
import org.apache.http.impl.client.HttpClients
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipInputStream

class UpdateRoute :Route{

    override fun getName(): String = "update"

    override fun route(args: Array<out String>): Boolean {
        val defaultLibs = File(config.libPath, config.default)
        if(!defaultLibs.mkdirs()) {
            defaultLibs.listFiles()!!.forEach {
                it.delete()
            }
        }

        val provider = BasicCredentialsProvider()
        provider.setCredentials(AuthScope(config.libUrl.host, config.libUrl.port), UsernamePasswordCredentials(config.userName, config.password))
        HttpClients.custom().setDefaultCredentialsProvider(provider).build().use { client ->
            val httpGet = HttpGet(config.libUrl)
            client.execute(httpGet).use { response ->
                logger.debug("${response.statusLine.statusCode}: ${response.statusLine.reasonPhrase}")
                response.allHeaders.forEach {
                    logger.debug("${it.name}: ${it.value}")
                }

                logger.info("Writing libs...")

                val zis = ZipInputStream(response.entity.content)
                val buffer = ByteArray(2048)

                while (true) {
                    val entry = zis.nextEntry ?: break
                    val filePath = defaultLibs.toPath().resolve(entry.name)
                    logger.info("Inflating ${entry.name}...")
                    logger.debug("Size: ${entry.size}")

                    //Write file from Zip to file
                    val fos = FileOutputStream(filePath.toFile())
                    while (true) {
                        val count = zis.read(buffer)
                        if (count == -1) break
                        fos.write(buffer, 0, count)
                    }
                }
            }
        }
        return true
    }
}
