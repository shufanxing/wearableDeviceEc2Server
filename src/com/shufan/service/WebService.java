package com.shufan.service;

import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.apache.tomcat.jdbc.pool.PoolProperties;
//import org.apache.tomcat.jdbc.pool.DataSource;


import com.shufan.dao.StepCountDAO;
import com.shufan.dao.impl.StepCountDAOImpl;
import com.shufan.model.StepCount;
import com.shufan.util.Database;

@Path("tomcat")
public class WebService {
	private Logger logger = Logger.getLogger(WebService.class);	
	
    private DataSource datasource = Database.getDataSource();

	private StepCountDAO daoImpl = new StepCountDAOImpl(datasource);
	
	@Path("status")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getStatus() {
		logger.info("Inside getStatus()...");
		return "Server Status is OK...";
	}
	
    @POST
    @Path("{userID}/{day}/{timeInterval}/{stepCount}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response postStepCount(@PathParam("userID") Integer userID, @PathParam("day") Integer day, @PathParam("timeInterval") Integer timeInterval, @PathParam("stepCount") Integer stepCount) {
        long dummyId = -1;
        StepCount entity = new StepCount(dummyId, userID, day, timeInterval, stepCount);
        
        logger.info("Inside postStepCount");
        
        return daoImpl.postStepCount(entity);
    }
    
    @GET
    @Path("current/{userID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCurrent(@PathParam("userID") Integer userID) {
    	
		logger.info("Inside getCurrent...");

		return daoImpl.getCurrent(userID);
    }
    
    @GET
    @Path("single/{userID}/{day}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSingle(@PathParam("userID") Integer userID, @PathParam("day") Integer day) {
    	
		logger.info("Inside getSingle...");

		return daoImpl.getSingle(userID, day);
    }
    
    @GET
    @Path("range/{userID}/{startDay}/{numDays}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRange(@PathParam("userID") Integer userID, @PathParam("startDay") Integer startDay, @PathParam("numDays") Integer numDays) {
    	
		logger.info("Inside getRange...");
		
		return daoImpl.getRange(userID, startDay, numDays);
    }
    
    @POST
    @Path("deleteAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAll() {
		logger.info("Inside deleteAll...");

		return daoImpl.deleteAll();
    }
    
}
