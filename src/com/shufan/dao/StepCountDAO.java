package com.shufan.dao;

import javax.ws.rs.core.Response;

import com.shufan.model.*;

public interface StepCountDAO {
	public Response postStepCount( StepCount stepCount );
	public Response getCurrent(int userId);
	public Response getSingle(int userId, int day);
	public Response getRange(int userId, int startDay, int numDays);
	public Response deleteAll();
}
