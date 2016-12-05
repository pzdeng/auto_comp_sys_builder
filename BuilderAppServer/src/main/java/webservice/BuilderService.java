package main.java.webservice;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import main.java.databuilder.ComputerBuild;
import main.java.databuilder.ComputerBuilder;
import main.java.databuilder.DataBuilder;
import main.java.databuilder.DataInitializer;
import main.java.global.AppConstants;
import main.java.objects.ClientPayload;
import main.java.objects.ComputerType;

@Path("/")
public class BuilderService {
    
    @Path("/build")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response respond(
    		@QueryParam("budget") int budget,
    		@QueryParam("computerType") String computerType,
    		@QueryParam("timeOut") int timeout) {
    	Date profileTime = new Date(System.currentTimeMillis());
    	//Good timeout values are between 1 - 300 seconds, otherwise set to default build time limit
    	if(timeout < 1 || timeout > 300){
    		timeout = AppConstants.buildTime;
    	}
    	//Check server cache for existing query entry
    	if(AppConstants.cache.get(ComputerType.toType(computerType)) == null){
    		AppConstants.cache.put(ComputerType.toType(computerType), new HashMap<String, ComputerBuild>());
    	}
    	ComputerBuild compBuild = AppConstants.cache.get(ComputerType.toType(computerType)).get(budget + "");
    	
    	//Execute the query if 
    		//Build doesn't exist in cache OR
    		//Build in cache was executed for a small time interval, we can replace with better results with more time
    	if(compBuild == null || compBuild.timeout < timeout){
    		ComputerBuilder builder = new ComputerBuilder(budget, computerType, timeout);
    		compBuild = builder.getBuild();
    		AppConstants.cache.get(ComputerType.toType(computerType)).put(budget + "", compBuild);
    		System.out.println("No Cache Hit: Completed Search in {" + (float)(System.currentTimeMillis() - profileTime.getTime())/1000 + 
    				"} with TimeOut Val {" + timeout + "}" );
    	}
    	else{
    		System.out.println("Cache Hit: ProfileTime {" + (float)(System.currentTimeMillis() - profileTime.getTime())/1000 + "}" );
    	}
    	String jsonStr = "";
    	
    	try {
			jsonStr = new ObjectMapper().writeValueAsString(compBuild.createClientPayload());
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
    	DataInitializer dBuilder = DataInitializer.getInstance();
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
    
    @Path("/cacheRefresh")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response refreshCache() {
    	String msg = "Refreshed server cache.";
    	AppConstants.cache = new HashMap<ComputerType, HashMap<String, ComputerBuild>>();
    	System.out.println(msg);
    	//Building response with CORS support
        return Response.status(200).entity(msg)
        		.header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
        		.build();
    }
    
    @Path("/productPriceUpdate")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateInventoryPrices() {
    	String msg = "Updating inventory items.";
    	System.out.println(msg);
    	//Building response with CORS support
            new Thread(new Runnable() {
                @Override
                public void run() {
                	Date updateTime = new Date(System.currentTimeMillis());
                	DataBuilder db = DataBuilder.getInstance();
                	db.initAllData();
                    db.updateProductPricing();
                    //Reset data structures
                    DataInitializer.getInstance().refresh();
                    AppConstants.cache = new HashMap<ComputerType, HashMap<String, ComputerBuild>>();
                    System.out.println("Update Duration {" + (float)(System.currentTimeMillis() - updateTime.getTime())/1000 + "}" );
                }
            }).start();
        return Response.status(200).entity(msg)
        		.header("Access-Control-Allow-Origin", "*")
    			.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
        		.build();
    }
}