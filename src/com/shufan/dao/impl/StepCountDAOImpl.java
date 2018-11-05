package com.shufan.dao.impl;

import org.apache.tomcat.jdbc.pool.ConnectionPool;
//import java.sql.Connection;
//import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PooledConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.sql.DataSource;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import java.sql.Connection;
import com.shufan.dao.StepCountDAO;
import com.shufan.model.StatusMessage;
import com.shufan.model.StepCount;

public class StepCountDAOImpl implements StepCountDAO {
	private DataSource datasource = null;
	private Logger logger = Logger.getLogger(StepCountDAOImpl.class);

	public StepCountDAOImpl(DataSource datasource) {
		this.datasource = datasource;
	}

	@Override
	public Response postStepCount(StepCount stepCount)  {
		Connection conn = null;
		PreparedStatement ps = null;
		StatusMessage statusMessage = null;

		String sql = "insert into step_count (user_id, day, time_interval, step_count) values (?,?,?,?)";

		try {
			
			conn = datasource.getConnection();

			ps = conn.prepareStatement(sql);
			ps.setInt(1, stepCount.getUserId());
			ps.setInt(2, stepCount.getDay());
			ps.setInt(3, stepCount.getTimeInterval());
			ps.setInt(4, stepCount.getStepCount());

			int rows = ps.executeUpdate();

			if (rows == 0) {
				logger.error("Unable to create step_count...");
				statusMessage = new StatusMessage();
				statusMessage.setStatus(Status.NOT_FOUND.getStatusCode());
				statusMessage.setMessage("Unable to create step_count...");
				return Response.status(404).entity(statusMessage).build();
			}

			ps.close();
			ps = null;
			conn.close(); // Return to connection pool
			conn = null; // Make sure we don't close it twice
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					logger.error("Error closing PreparedStatement: " + e.getMessage());
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("Error closing connection: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
		return Response.status(200).entity(stepCount).build();
	}

	@Override
	public Response getCurrent(int userId)  {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps2 = null;
		ResultSet rs2 = null;

		Integer current = -1;
		Integer sum = -1;

		String sql = "SELECT MAX(day) as current FROM step_count WHERE user_id = ?";
		String sql2 = "SELECT SUM(step_count) as sum FROM step_count WHERE user_id = ? and day = ?";

		try {
			
			conn = datasource.getConnection();

			ps = conn.prepareStatement(sql);
			ps.setInt(1, userId);
			rs = ps.executeQuery();

			if (rs.next()) {
				current = rs.getInt("current");
			} else {
				logger.error(String.format("Current day with user_id of %d is not found.", userId));
				StatusMessage statusMessage = new StatusMessage();
				statusMessage.setStatus(Status.NOT_FOUND.getStatusCode());
				statusMessage.setMessage(String.format("Customer with ID of %d is not found.", userId));
				return Response.status(404).entity(statusMessage).build();
			}


			ps2 = conn.prepareStatement(sql2);
			ps2.setInt(1, userId);
			ps2.setInt(2, current);
			rs2 = ps2.executeQuery();

			if (rs2.next()) {
				sum = rs2.getInt("sum");
			} else {
				logger.error(String.format("step count with user_id of %d in current day %d is not found.", userId,
						current));
				StatusMessage statusMessage = new StatusMessage();
				statusMessage.setStatus(Status.NOT_FOUND.getStatusCode());
				statusMessage.setMessage(String.format("step count with user_id of %d in current day %d is not found.",
						userId, current));
				return Response.status(404).entity(statusMessage).build();
			}

			rs.close();
			rs = null;
			ps.close();
			ps = null;

			rs2.close();
			rs2 = null;
			ps2.close();
			ps2 = null;

			conn.close(); // Return to connection pool
			conn = null; // Make sure we don't close it twice

		} catch (Exception e) {
			logger.error("Error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error("Error closing resultset: " + e.getMessage());
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					logger.error("Error closing PreparedStatement: " + e.getMessage());
					e.printStackTrace();
				}
			}

			if (rs2 != null) {
				try {
					rs2.close();
				} catch (SQLException e) {
					logger.error("Error closing resultset: " + e.getMessage());
					e.printStackTrace();
				}
			}
			if (ps2 != null) {
				try {
					ps2.close();
				} catch (SQLException e) {
					logger.error("Error closing PreparedStatement: " + e.getMessage());
					e.printStackTrace();
				}
			}

			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("Error closing connection: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}

		return Response.status(200).entity(sum).build();
	}

	@Override
	public Response getSingle(int userId, int day) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		Integer sum = -1;

		String sql = "SELECT SUM(step_count) as sum FROM step_count WHERE user_id = ? and day = ?";

		try {
			
			conn = datasource.getConnection();

			ps = conn.prepareStatement(sql);
			ps.setInt(1, userId);
			ps.setInt(2, day);
			rs = ps.executeQuery();

			if (rs.next()) {
				sum = rs.getInt("sum");
			} else {
				logger.error(String.format("step count with user_id of %d in day %d is not found.", userId, day));
				StatusMessage statusMessage = new StatusMessage();
				statusMessage.setStatus(Status.NOT_FOUND.getStatusCode());
				statusMessage.setMessage(
						String.format("step count with user_id of %d in day %d is not found.", userId, day));
				return Response.status(404).entity(statusMessage).build();
			}

			rs.close();
			rs = null;
			ps.close();
			ps = null;
			conn.close(); // Return to connection pool
			conn = null; // Make sure we don't close it twice

		} catch (Exception e) {
			logger.error("Error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error("Error closing resultset: " + e.getMessage());
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					logger.error("Error closing PreparedStatement: " + e.getMessage());
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("Error closing connection: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
		return Response.status(200).entity(sum).build();
	}

	@Override
	public Response getRange(int userId, int startDay, int numDays) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		Integer sum = -1;

		String sql = "SELECT SUM(step_count) as sum FROM step_count WHERE user_id = ? and day >= ? and day < ? GROUP BY day";

		try {
			
			conn = datasource.getConnection();

			ps = conn.prepareStatement(sql);
			ps.setInt(1, userId);
			ps.setInt(2, startDay);
			ps.setInt(3, startDay + numDays);
			rs = ps.executeQuery();

			if (rs.next()) {
				sum = rs.getInt("sum");
			} else {
				logger.error(String.format("step count with user_id of %d from day %d to day %d is not found.", userId,
						startDay, startDay + numDays));
				StatusMessage statusMessage = new StatusMessage();
				statusMessage.setStatus(Status.NOT_FOUND.getStatusCode());
				statusMessage
						.setMessage(String.format("step count with user_id of %d from day %d to day %d is not found.",
								userId, startDay, startDay + numDays));
				return Response.status(404).entity(statusMessage).build();
			}

			rs.close();
			rs = null;
			ps.close();
			ps = null;
			conn.close(); // Return to connection pool
			conn = null; // Make sure we don't close it twice
		} catch (Exception  e) {
			logger.error("Error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error("Error closing resultset: " + e.getMessage());
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					logger.error("Error closing PreparedStatement: " + e.getMessage());
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("Error closing connection: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
		return Response.status(200).entity(sum).build();
	}

	public Response deleteAll() {
		Connection conn = null;
		PreparedStatement ps = null;

		Integer deleted = -1;

		String sql = "DELETE FROM step_count";

		try {
			
			conn = datasource.getConnection();

			ps = conn.prepareStatement(sql);

			deleted = ps.executeUpdate();

			ps.close();
			ps = null;
			conn.close(); // Return to connection pool
			conn = null; // Make sure we don't close it twice

		} catch (Exception  e) {
			logger.error("Error: " + e.getMessage());
			e.printStackTrace();
		} finally {

			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					logger.error("Error closing PreparedStatement: " + e.getMessage());
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("Error closing connection: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
		return Response.status(200).entity(deleted).build();
	}

}
