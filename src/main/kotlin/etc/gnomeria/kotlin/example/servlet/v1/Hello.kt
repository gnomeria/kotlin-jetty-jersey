package etc.gnomeria.kotlin.example.servlet.v1

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.core.Response

/**
 * Created by Sami on 17:57 - 09/12/16.
 */

@Path("/")
class Hello{
    @Path("hello")
    @GET
    fun sayHello() : Response{
        var response = Response.ok("Hello from version 1!")
        return response.build()
    }
}