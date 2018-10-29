/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities.service;

import entities.StepCount;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author shufan
 */
@Stateless
@Path("/")
public class StepCountFacadeREST extends AbstractFacade<StepCount> {

    @PersistenceContext(unitName = "WearableDeviceServerPU")
    private EntityManager em;

    public StepCountFacadeREST() {
        super(StepCount.class);
    }

    /*
        Automaticlly genereated codes
    */
    
    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(StepCount entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, StepCount entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public StepCount find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public List<StepCount> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<StepCount> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    /*
        Wearable sevice service endpoints
    */
    
    @POST
    @Path("{userID}/{day}/{timeInterval}/{stepCount}") 
    public void postStepCount(@PathParam("userID") Integer userID, @PathParam("day") Integer day, @PathParam("timeInterval") Integer timeInterval, @PathParam("stepCount") Integer stepCount) {
        Integer dummyId = -1;
        StepCount entity = new StepCount(dummyId, userID, day, timeInterval, stepCount);
        super.create(entity);
    }
    
    @GET
    @Path("current/{userID}")
    @Produces(MediaType.TEXT_PLAIN)
    public int getCurrent(@PathParam("userID") Integer userID) {
        int currentDay = (Integer) em.createNamedQuery("StepCount.findCurrentDayByUserId").setParameter("userId", userID).getSingleResult();
        return ((Long) em.createNamedQuery("StepCount.findStepSumByUserIdDay").setParameter("userId", userID).setParameter("day", currentDay).getSingleResult()).intValue();
    }
    
    @GET
    @Path("single/{userID}/{day}")
    @Produces(MediaType.TEXT_PLAIN)
    public int getSingle(@PathParam("userID") Integer userID, @PathParam("day") Integer day) {
        return ( (Long) em.createNamedQuery("StepCount.findStepSumByUserIdDay").setParameter("userId", userID).setParameter("day", day).getSingleResult()).intValue();
    }
    
    @GET
    @Path("range/{userID}/{startDay}/{numDays}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getRange(@PathParam("userID") Integer userID, @PathParam("startDay") Integer startDay, @PathParam("numDays") Integer numDays) {
        return em.createNamedQuery("StepCount.findRangeStepSumByUserId").setParameter("userId", userID).setParameter("startDay", startDay).setParameter("endDay", startDay+numDays).getResultList().toString();
    }
    
    //return: the number of deleted records    
    @POST
    @Path("deleteAll")
    @Produces(MediaType.TEXT_PLAIN)
    public int deleteAll() {
        return em.createNamedQuery("StepCount.deleteAll").executeUpdate();
    }

}
