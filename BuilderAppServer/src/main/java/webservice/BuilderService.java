package main.java.webservice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import main.java.objects.ClientPayload;
import main.java.objects.ComputerBuild;

@Path("/build")
public class BuilderService {
	
    public ComputerBuild doBuilderService(float budget, String type){
        //get build with this budget
		return new ComputerBuild();
    }
	/*
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ClientPayload respond(
    		@QueryParam("budget") int budget,
    		@QueryParam("computerType") String computerType) {
    	ComputerBuild build = new ComputerBuild(budget, computerType);
    	//JSON String as payload
        return build.createClientPayload();
    }
    */
    //OR
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response respond(
    		@QueryParam("budget") int budget,
    		@QueryParam("computerType") String computerType) {
    	String jsonStr = "";
    	ComputerBuild build = new ComputerBuild(budget, computerType);
    	try {
			jsonStr = new ObjectMapper().writeValueAsString(build.createClientPayload());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return Response.status(200).entity(jsonStr).build();
    }
    
}