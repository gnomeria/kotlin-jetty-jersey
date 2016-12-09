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

    fun setInitParamForServlet(sh: ServletHolder,
                               provider: String = "etc.gnomeria.kotlin.example.servlet",
                               initOrder: Int = 1) {
        sh.initOrder = initOrder
        sh.setInitParameter(ServerProperties.PROVIDER_PACKAGES, provider)
        sh.setInitParameter(ServerProperties.PROVIDER_SCANNING_RECURSIVE, "true")
        sh.setInitParameter(ServerProperties.TRACING, "ALL")
    }

    var sh = ServletHolder(ServletContainer::class.java)
    setInitParamForServlet(sh, initOrder = 1, provider = "etc.gnomeria.kotlin.eaxmple.servlet.v1")

    var sh2 = ServletHolder(ServletContainer::class.java)
    setInitParamForServlet(sh, initOrder = 2, provider = "etc.gnomeria.kotlin.eaxmple.servlet.v2")

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