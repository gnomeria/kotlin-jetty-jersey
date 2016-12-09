package etc.gnomeria.kotlin.example

import mu.KLogging
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.DefaultServlet
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.glassfish.jersey.server.ServerProperties
import org.glassfish.jersey.servlet.ServletContainer
import java.io.File

/**
 * Created by Sami on 17:52 - 09/12/16.
 */

class JettyRunnerMain{
    companion object : KLogging()
}

fun main(args: Array<String>){
    val logger = JettyRunnerMain.logger;
    var port = 8080;
    var server = Server(port)
    var webDir = System.getProperty("user.dir") + File.separator + "web"
    var context = ServletContextHandler(server, "", ServletContextHandler.SESSIONS)

    var sh = ServletHolder(ServletContainer::class.java)
    sh.initOrder = 1
    sh.setInitParameter(ServerProperties.PROVIDER_PACKAGES, "etc.gnomeria.kotlin.example.servlet.v1")
    sh.setInitParameter(ServerProperties.PROVIDER_SCANNING_RECURSIVE, "true")
    sh.setInitParameter(ServerProperties.TRACING, "ALL")

    var sh2 = ServletHolder(ServletContainer::class.java)
    sh2.initOrder = 2
    sh2.setInitParameter(ServerProperties.PROVIDER_PACKAGES, "etc.gnomeria.kotlin.example.servlet.v2")
    sh2.setInitParameter(ServerProperties.PROVIDER_SCANNING_RECURSIVE, "true")
    sh2.setInitParameter(ServerProperties.TRACING, "ALL")

    var webHolder = ServletHolder("default", DefaultServlet::class.java)
    webHolder.setInitParameter("resourceBase", webDir)
    webHolder.setInitParameter("dirAllowed", "true")

    context.addServlet(sh, "/rest/v1/*")
    context.addServlet(sh2, "/rest/v2/*")
    context.addServlet(webHolder, "/*")

    logger.info("Context: {} === {}", context.contextPath, context.servletContext)

    server.handler = context
    server.start()
    server.join()
}