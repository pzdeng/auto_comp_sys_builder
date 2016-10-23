package main.java.webservice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * This is a test webservice to make sure that I had everything set up correctly.
 */
@Path("/hello")
public class WebServiceTest {

    @GET
    @Produces("text/plain")
    public String hello() {
        return "Hello World 2.";
    }
}