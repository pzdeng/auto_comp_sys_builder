package main.java.webservice;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import main.java.objects.ComputerBuild;

@Path("/build")
public class BuilderService {

    public ComputerBuild doBuilderService(float budget, String type){
        //get build with this budget
		return new ComputerBuild();
    }
	
    @GET
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String respond() {
        return "Computer Parts";
    }
}