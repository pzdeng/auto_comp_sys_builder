package main.java.webservice;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import main.java.databuilder.ComputerBuild;
import main.java.databuilder.DataBuilder;
import main.java.objects.ClientPayload;

@Path("/")
public class BuilderService {
	
    public ComputerBuild doBuilderService(float budget, String type){
        //get build with this budget
		return new ComputerBuild();
    }
    
    @Path("/build")
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
    	//Building response with CORS support
        return Response.status(200).entity(jsonStr)
        		.header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
        		.build();
    }
    
    @Path("/inventory")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInventory() {
    	String jsonStr = "";
    	DataBuilder dBuilder = DataBuilder.getInstance();
    	try {
			jsonStr = new ObjectMapper().writeValueAsString(dBuilder.getInventory());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//Building response with CORS support
        return Response.status(200).entity(jsonStr)
        		.header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
        		.build();
    }
}