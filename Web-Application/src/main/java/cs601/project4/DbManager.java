package cs601.project4;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Connect to MySQL, create SQL statement, and retrieve data.  
 * @author Kei Fukutani
 */
public class DbManager {
	
	private Logger logger = LogManager.getLogger();

	private String CONFIGURATION_FILE = TicketPurchaseWebApp.getConfig();	
	private String DRIVER;
	private String HOST;
	private String USERNAME;
	private String PASSWORD;
	private String DBNAME;
	private String TIME_ZONE_SETTINGS;
	
	
	public DbManager() {
		initialize();
	}
	
	
	/**
	 * Reads the configuration file and gets some MySQL authentication information. 
	 */
	private void initialize() {
		Properties config = new Properties();
		
		try (FileInputStream input = new FileInputStream(CONFIGURATION_FILE)) {
			
			config.load(input);
			
			DRIVER = config.getProperty("driver");
			HOST = config.getProperty("host");
			USERNAME = config.getProperty("username");
			PASSWORD = config.getProperty("password");
			DBNAME = config.getProperty("dbname");
			TIME_ZONE_SETTINGS = config.getProperty("timeZoneSettings");
			
			try {
				Class.forName(DRIVER).newInstance();
			}
			catch (Exception e) {
				System.err.println("Can't find driver");
				System.exit(1);
			}
			
		} catch (IOException e) {
			logger.trace(e.getMessage() + ", not found.");
			logger.trace("Bye.");
			System.exit(1);
		}
	}
	
	
	/**
	 * Store the user information in DB, such as username and password. 
	 * @param userid
	 * @param password
	 */
	public synchronized boolean signUp(String username, String password) {
		
		try (Connection connection = DriverManager.getConnection(HOST + DBNAME + TIME_ZONE_SETTINGS,
                                                                 USERNAME, PASSWORD)) {
			
			// Check to see if the username already exists. 
			String selectStr = "SELECT username FROM user WHERE username = ?";
			PreparedStatement selectStmt = connection.prepareStatement(selectStr);
			selectStmt.setString(1, username);
			
			ResultSet rs = selectStmt.executeQuery();
			
			if (!rs.next()) {
				
				byte[] salt = getSalt();
				byte[] hashedPassword = hashPassword(password, salt);
				
				String hexStringSalt = DatatypeConverter.printHexBinary(salt);
				String hexStringHashedPassword = DatatypeConverter.printHexBinary(hashedPassword);
				logger.trace("Getting into MySQL.");
				logger.trace("hexStringSalt: " + hexStringSalt);
				logger.trace("hexStringHashedPassword: " + hexStringHashedPassword);				
				
				// Sign up the username. 
				String insertStr = "INSERT INTO user (username, password, salt) VALUES (?, ?, ?)";
			    PreparedStatement insertStmt = connection.prepareStatement(insertStr);
				insertStmt.setString(1, username);
				insertStmt.setString(2, hexStringHashedPassword);
				insertStmt.setString(3, hexStringSalt);
				insertStmt.execute();
				logger.trace("username, " + username + " has been registered.");
				
				return true;
				
			} else {
				logger.trace("username, " + username + " already exists.");
			}
			
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			logger.trace("SQLException: " + sqle.getMessage());
		} 
		
		return false;		
	}


	/**
	 * Generate salt. 
	 * @return salt generated
	 */
	private byte[] getSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		
		return salt;
	}


	/**
	 * Get a hashed password using PBKDF2 algorithm with salt. 
	 * @param password  the password to be hashed
	 * @return hashedPassword
	 */
	private static byte[] hashPassword(String password, byte[] salt) {
		
		byte[] hashedPassword = null;
		try {
			KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			
			hashedPassword = factory.generateSecret(spec).getEncoded();
			
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		
		return hashedPassword;
	}


	/**
	 * Authenticate the user.
	 * Check the username and password.
	 * @param username
	 * @param password
	 * @return true if success
	 */
	public synchronized boolean authenticate(String username, String password) {
		
		try (Connection connection = DriverManager.getConnection(HOST + DBNAME + TIME_ZONE_SETTINGS,
                                                                 USERNAME, PASSWORD)) {
			
			String selectStr = "SELECT username, password, salt FROM user WHERE username = ?";
			PreparedStatement selectStmt = connection.prepareStatement(selectStr);
			selectStmt.setString(1, username);
			
			ResultSet rs = selectStmt.executeQuery();
			
			if (rs.next()) {
				
				byte[] salt = DatatypeConverter.parseHexBinary(rs.getString("salt"));
				byte[] hashedPassword = hashPassword(password, salt);
				logger.trace("Getting out of MySQL.");
				logger.trace("rs.getString(\"salt\"): " + rs.getString("salt"));
				
				String hashedPasswordHexString = DatatypeConverter.printHexBinary(hashedPassword);
				String hashedPasswordHexStringRegistered = rs.getString("password");
				logger.trace("hashedPasswordHexString: " + hashedPasswordHexString);
				logger.trace("hashedPasswordHexStringRegistered: " + hashedPasswordHexStringRegistered);

				return username.equals(rs.getString("username")) && 
					   hashedPasswordHexString.equals(hashedPasswordHexStringRegistered);
			}
			
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			logger.trace("SQLException: " + sqle.getMessage());
		}
		
		return false;
	}
	
	
	/**
	 * To view all events, get all events in the event table. 
	 * @return
	 */
	public synchronized ArrayList<Event> getAllEvents() {
		ArrayList<Event> events = new ArrayList<>();
		
		try (Connection connection = DriverManager.getConnection(HOST + DBNAME + TIME_ZONE_SETTINGS,
                USERNAME, PASSWORD)) {
			
			String selectStr = "SELECT * FROM event";
			PreparedStatement selsectStmt = connection.prepareStatement(selectStr);
			
			ResultSet resultSet = selsectStmt.executeQuery();
			
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				String description = resultSet.getString("description");
				logger.trace("id: " + id + " name: " + name + " description: " + description);
				
				Event event = new Event();
				event.setId(id);
				event.setName(name);
				event.setDescription(description);
				
				events.add(event);				
			}
				
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			logger.trace("SQLException: " + sqle.getMessage());
		} 
		
		return events;
	}
	
	
	/**
	 * Get all tickets data that user purchased or got from other user.  
	 * @return result list
	 */
	public synchronized ArrayList<MyTicket> getMyTickets(String username) {
		ArrayList<MyTicket> myTickets = new ArrayList<>();
		
		try (Connection connection = DriverManager.getConnection(HOST + DBNAME + TIME_ZONE_SETTINGS,
                USERNAME, PASSWORD)) {
			
			String selectStr = "SELECT ticket.id, event.name " + 
					           "FROM ticket " + 
					           "INNER JOIN event ON ticket.eventid = event.id " + 
					           "INNER JOIN user ON ticket.owner = user.username " + 
					           "WHERE ticket.owner = ?";
			PreparedStatement selsectStmt = connection.prepareStatement(selectStr);
			selsectStmt.setString(1, username);
			
			ResultSet resultSet = selsectStmt.executeQuery();
			
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				logger.trace("id: " + id + " name: " + name);
				
				MyTicket myTicket = new MyTicket();
				myTicket.setTicketId(id);
				myTicket.setEventName(name);
				
				myTickets.add(myTicket);				
			}
				
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			logger.trace("SQLException: " + sqle.getMessage());
		} 
		
		return myTickets;
	}
	
	
	/**
	 * Transfer ticket. In other words, update the owner in the ticket table. 
	 * @param ticketId
	 * @param toWhom
	 * @return true if success
	 */
	public synchronized boolean transferTicket(String ticketId, String toWhom) {
		logger.trace("transferTicket(ticketId: " + ticketId + " toWhom: " + toWhom + ")");

		try (Connection connection = DriverManager.getConnection(HOST + DBNAME + TIME_ZONE_SETTINGS,
                                                                 USERNAME, PASSWORD)) {
			String updateStr = "UPDATE ticket " + 
					           "SET owner = ? " + 
					           "WHERE id = ?";
			PreparedStatement updateStmt = connection.prepareStatement(updateStr);
			updateStmt.setString(1, toWhom);
			updateStmt.setString(2, ticketId);
			
			int res = updateStmt.executeUpdate();
			logger.trace(res);
			if (res == 1) {
				return true;
			}
				
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			logger.trace("SQLException: " + sqle.getMessage());
		} 
		
		return false;
	}
	
	
	/**
	 * Update owner in ticket table and decrement available_ticket by 1 in event table. 
	 * @param eventId
	 * @param newOwner  new owner to be set
	 * @return true if success
	 */
	public synchronized boolean buyTicket(String eventId, String newOwner) {
		logger.trace("setOwner(eventId: " + eventId + " newOwner: " + newOwner + ")");

		try (Connection connection = DriverManager.getConnection(HOST + DBNAME + TIME_ZONE_SETTINGS,
				                                                 USERNAME, PASSWORD)) {
			// Update the owner in ticket table, which has a null in owner,  
			// and has a specified event id, and has a minimum ticket id. 
			String updateStr1 = "UPDATE ticket, " + 
					            " (SELECT min(id) min_id " + 
					              "FROM ticket " + 
					              "WHERE eventid = ? " + 
					              "  and owner IS NULL) mt " + 
					            "SET ticket.owner = ? " + 
					            "WHERE ticket.id = mt.min_id";
			PreparedStatement updateStmt1 = connection.prepareStatement(updateStr1);
			updateStmt1.setString(1, eventId);
			updateStmt1.setString(2, newOwner);
			int res1 = updateStmt1.executeUpdate();
			logger.trace("res1: " + res1);
			
			// Decrement available_ticket in event table. 
			int res2 = 0;
			if (res1 == 1) {
				String updateStr2 = "UPDATE event " + 
						            "SET available_ticket = available_ticket - 1 " + 
						            "WHERE event.id = ?";
				PreparedStatement updateStmt2 = connection.prepareStatement(updateStr2);
				updateStmt2.setString(1, eventId);
				res2 = updateStmt2.executeUpdate();
				logger.trace("res2: " + res2);
			}

			if (res1 == 1 && res2 == 1) {
				return true;
			}

		} catch (SQLException sqle) {
			sqle.printStackTrace();
			logger.trace("SQLException: " + sqle.getMessage());
		} 

		return false;
	}
	
	
	/**
	 * Check the number of tickets available,
	 * to display a "Buy Ticket" button or "Sold out!". 
	 * @param eventId  the event to be checked
	 * @return the number of tickets available 
	 */
	public synchronized int checkAvailableTicket(String eventId) {
		int availableTicket = 0;
		
		try (Connection connection = DriverManager.getConnection(HOST + DBNAME + TIME_ZONE_SETTINGS,
                USERNAME, PASSWORD)) {
			
			String selectStr = "SELECT available_ticket " + 
					           "FROM event " + 
					           "WHERE event.id = ?";
			PreparedStatement selsectStmt = connection.prepareStatement(selectStr);
			selsectStmt.setString(1, eventId);
			
			ResultSet resultSet = selsectStmt.executeQuery();
			
			resultSet.next();
			availableTicket = resultSet.getInt("available_ticket");
				
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			logger.trace("SQLException: " + sqle.getMessage());
		} 
		
		return availableTicket;
	}
	
	
	/**
	 * Create an event. 
	 * @param eventName
	 * @param eventDesc  the event description
	 * @param totalTicket
	 * @param username  the user that creates the event
	 * @return
	 */
	public synchronized boolean createEvent(String eventName, String eventDesc, 
			                                String totalTicket, String username) {
		logger.trace("createEvent(eventName: " + eventName + " eventDesc: " + eventDesc + 
				     " totalTicket: " + totalTicket + " username: " + username + ")");

		try (Connection connection = DriverManager.getConnection(HOST + DBNAME + TIME_ZONE_SETTINGS,
                                                                 USERNAME, PASSWORD)) {
			// 1. Add a new event row in event table. 
			String insertStr = "INSERT INTO event " + 
					           "(name, description, total_ticket, available_ticket, createdby) " + 
					           "VALUES " + 
					           "(?, ?, ?, ?, ?)";
			PreparedStatement insertStmt = connection.prepareStatement(insertStr);
			insertStmt.setString(1, eventName);
			insertStmt.setString(2, eventDesc);
			int totalTicketInteger = Integer.parseInt(totalTicket);
			insertStmt.setInt(3, totalTicketInteger);
			insertStmt.setInt(4, totalTicketInteger);
			insertStmt.setString(5, username);
			
			int res1 = insertStmt.executeUpdate();
			logger.trace("res1: " + res1);
			
			// Get auto-increment event id that is in the newly added row in event table 
			// right before this.  
			String selectStr = "SELECT LAST_INSERT_ID()";
			PreparedStatement selectStmt = connection.prepareStatement(selectStr);
		    ResultSet rs = selectStmt.executeQuery("SELECT LAST_INSERT_ID()");
		    rs.next();
		    int eventId = rs.getInt(1);
			
			// 2. Add new ticket rows in ticket table with the eventId. 
		    addTickets(String.valueOf(eventId), totalTicketInteger);
		    		
			return true;
				
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			logger.trace("SQLException: " + sqle.getMessage());
		} 
		
		return false;
	}


	/**
	 * Get all events data that user created.  
	 * @return result list
	 */
	public synchronized ArrayList<Event> getMyEvents(String username) {
		ArrayList<Event> myEvents = new ArrayList<>();
		
		try (Connection connection = DriverManager.getConnection(HOST + DBNAME + TIME_ZONE_SETTINGS,
                                                                 USERNAME, PASSWORD)) {
			
			String selectStr = "SELECT event.id, event.name, event.description, " + 
					           "event.total_ticket, event.available_ticket " + 
					           "FROM event " + 
					           "WHERE event.createdby = ?";
			PreparedStatement selsectStmt = connection.prepareStatement(selectStr);
			selsectStmt.setString(1, username);
			
			ResultSet resultSet = selsectStmt.executeQuery();
			
			while (resultSet.next()) {
				int eventId = resultSet.getInt("id");
				String eventName = resultSet.getString("name");
				String eventDesc = resultSet.getString("description");
				int totalTickets = resultSet.getInt("total_ticket");
				int availableTickets = resultSet.getInt("available_ticket");
				logger.trace("eventId: " + eventId + " eventName: " + eventName);
				logger.trace("eventDesc: " + eventDesc);
				logger.trace("totalTickets: " + totalTickets + " availableTickets: " + availableTickets);
				
				Event myEvent = new Event();
				myEvent.setId(eventId);
				myEvent.setName(eventName);
				myEvent.setDescription(eventDesc);
				myEvent.setTotalTickets(totalTickets);
				myEvent.setAvailableTickets(availableTickets);
				
				myEvents.add(myEvent);				
			}
				
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			logger.trace("SQLException: " + sqle.getMessage());
		} 
		
		return myEvents;
	}
	
	
	/**
	 * Save the event user edited. 
	 * Check the total num of tickets and available tickets. Depending on that, 
	 * add tickets in ticket table, delete tickets, or error. 
	 * @param eventId
	 * @param eventName
	 * @param eventDesc
	 * @param totalTickets
	 * @return
	 */
	public synchronized String saveMyEvent(String eventId, String eventName, 
			                                String eventDesc, String totalTickets) {
		logger.trace("saveMyEvent() start.");
		String message = "NG";
		
		try (Connection connection = DriverManager.getConnection(HOST + DBNAME + TIME_ZONE_SETTINGS,
                                                                 USERNAME, PASSWORD)) {
			// Check the total num of tickets and available tickets. 
			String selectStr = "SELECT total_ticket, available_ticket " + 
					           "FROM event " + 
					           "WHERE event.id = ?";
			PreparedStatement selsectStmt = connection.prepareStatement(selectStr);
			selsectStmt.setInt(1, Integer.parseInt(eventId));
			ResultSet resultSet = selsectStmt.executeQuery();			
			resultSet.next();
			int availableTicket = resultSet.getInt("available_ticket");
			int totalTicketsBeforeUpdate = resultSet.getInt("total_ticket");
			logger.trace("availableTicket: " + availableTicket);
			logger.trace("totalTicketsBeforeUpdate: " + totalTicketsBeforeUpdate);
			logger.trace("totalTickets: " + totalTickets);
			
			int totalTicketsInt = Integer.parseInt(totalTickets);
			int diff = totalTicketsInt - totalTicketsBeforeUpdate;
			logger.trace(diff);
			if (diff > 0) {
				// User entered the totalTickets more than the total_ticket in the table. 
				// Update event table, and add new tickets. 
				logger.trace("case 1. add tickets");
				boolean res1 = updateEventTable(eventId, eventName, eventDesc, totalTickets, diff);
				boolean res2 = addTickets(eventId, diff);
				
				if (res1 && res2) {
					message = "OK";
				}
				
				return message;
							
			} else if (diff < 0 && Math.abs(diff) < availableTicket) {
				// User entered the totalTickets less than the total_ticket in the table, 
				// and the difference is less than available_ticket, so 
				// we can delete the tickets that is not own by someone. 
				// Update event table, and delete tickets that are not owned by someone. 
				logger.trace("case 2. delete tickets");
				boolean res1 = updateEventTable(eventId, eventName, eventDesc, totalTickets, diff);
				boolean res2 = deleteTickets(eventId, Math.abs(diff));

				if (res1 && res2) {
					message = "OK";
				}
				
				return message;
				
			} else if (diff < 0 && Math.abs(diff) > availableTicket) {
				// Error. Total number of tickets cannot be smaller than the number of tickets that 
				// users have already purchased. 
				logger.trace("case 3. error");
				
				message = "The total number of tickets should not be smaller than " + 
				          "the number of tickets that users have already purchased.";
				return message;
				
			} else {
				// User did not edit the total number of tickets. 
				logger.trace("case 4. no change in ticket table");
				
				boolean success = updateEventTable(eventId, eventName, eventDesc, totalTickets, 0);
				if (success) {
					message = "OK";
				}
				
				return message;
			}
			
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			logger.trace("SQLException: " + sqle.getMessage());
		} 
		
		return message;
	}


	/**
	 * Update event name, description, total_ticket, and available_ticket in event table. 
	 * @param eventId
	 * @param eventName
	 * @param eventDesc
	 * @param totalTickets
	 * @param diff  the number of rows to be increased(positive), or decreased(negative)
	 * @return
	 */
	private synchronized boolean updateEventTable(String eventId, String eventName, 
			                                      String eventDesc, String totalTickets, int diff) {

		try (Connection connection = DriverManager.getConnection(HOST + DBNAME + TIME_ZONE_SETTINGS,
				USERNAME, PASSWORD)) {

			String updateStr = "UPDATE event " + 
					           "SET name = ?, description = ?, total_ticket = ?, " + 
					           "available_ticket = available_ticket + ? " + 
					           "WHERE event.id = ?";
			PreparedStatement updateStmt = connection.prepareStatement(updateStr);
			updateStmt.setString(1, eventName);
			updateStmt.setString(2, eventDesc);
			updateStmt.setString(3, totalTickets);
			updateStmt.setInt(4, diff);
			updateStmt.setString(5, eventId);
			int res = updateStmt.executeUpdate();
			logger.trace("updateEventTable() res: " + res);

			return res == 1;

		} catch (SQLException sqle) {
			sqle.printStackTrace();
			logger.trace("SQLException: " + sqle.getMessage());
		} 

		return false;
	}
	
	
	/**
	 * Add specified number of ticket rows in ticket table. 
	 * @param eventId
	 * @param diff  (>0) the num of rows to be added 
	 * @return
	 */
	private synchronized boolean addTickets(String eventId, int diff) {

		try (Connection connection = DriverManager.getConnection(HOST + DBNAME + TIME_ZONE_SETTINGS,
				                                                 USERNAME, PASSWORD)) {
			
			String insertStr = "INSERT INTO ticket " + 
					           "(eventid, owner) " + 
					           "VALUES " + 
					           "(?, NULL)";
			PreparedStatement insertStmt = connection.prepareStatement(insertStr);
			insertStmt.setInt(1, Integer.parseInt(eventId));

			int sum = 0;
			for (int i = 0; i < diff; i++) {
				sum += insertStmt.executeUpdate();
			}	
			logger.trace("sum: " + sum);
			
			return sum == diff;

		} catch (SQLException sqle) {
			sqle.printStackTrace();
			logger.trace("SQLException: " + sqle.getMessage());
		} 

		return false;
	}

	
	/**
	 * Delete specified number of ticket rows in ticket table. 
	 * @param eventId
	 * @param diff  the num of rows to be added
	 * @return
	 */
	private synchronized boolean deleteTickets(String eventId, int diff) {
		logger.trace("deleteTickets() starts.");

		try (Connection connection = DriverManager.getConnection(HOST + DBNAME + TIME_ZONE_SETTINGS,
				                                                 USERNAME, PASSWORD)) {
			// Delete a ticket row where owner is null, the ticket id is max, and eventid is eventId.  
			String deleteStr = "DELETE t FROM ticket t, " + 
		                       " (SELECT max(id) max_id " + 
		                       "  FROM ticket " + 
		                       "  WHERE eventid = ? " + 
		                       "    and owner IS NULL) mt " + 
		                       "WHERE t.id = mt.max_id";
			PreparedStatement deleteStmt = connection.prepareStatement(deleteStr);
			deleteStmt.setInt(1, Integer.parseInt(eventId));

			int sum = 0;
			for (int i = 0; i < diff; i++) {
				sum += deleteStmt.executeUpdate();
			}
			logger.trace("sum: " + sum);

			return sum == diff;

		} catch (SQLException sqle) {
			sqle.printStackTrace();
			logger.trace("SQLException: " + sqle.getMessage());
		} 
		
		return false;
	}


	/**
	 * Delete a row in event table.
	 * @param eventId  the event to be deleted
	 * @return
	 */
	public boolean deleteEventRow(String eventId) {
		logger.trace("deleteEventRow(eventId: " + eventId + ") starts. ");
		
		try (Connection connection = DriverManager.getConnection(HOST + DBNAME + TIME_ZONE_SETTINGS,
                USERNAME, PASSWORD)) {

			String deleteStr = "DELETE FROM event " +
					           "WHERE id = ?";
			PreparedStatement deleteStmt = connection.prepareStatement(deleteStr);
			deleteStmt.setInt(1, Integer.parseInt(eventId));

			int res = deleteStmt.executeUpdate();
			logger.trace("res: " + res);

			return res == 1;

		} catch (SQLException sqle) {
			sqle.printStackTrace();
			logger.trace("SQLException: " + sqle.getMessage());
		} 
		
		return false;
	}


	/**
	 * Search event name, event description with ticket availability. 
	 * @param term
	 * @param ticketAvailable
	 * @return the result list of events
	 */
	public synchronized ArrayList<Event> searchEvent(String term, String ticketAvailable) {
		ArrayList<Event> resEvents = new ArrayList<>();
		
		try (Connection connection = DriverManager.getConnection(HOST + DBNAME + TIME_ZONE_SETTINGS,
                                                                 USERNAME, PASSWORD)) {
			
			String selectStr = "SELECT event.id, event.name, event.description, " + 
					           "event.total_ticket, event.available_ticket " + 
					           "FROM event " + 
					           "WHERE (name LIKE ? " +
					           "   or description LIKE ?) ";
		    selectStr += (ticketAvailable != null && ticketAvailable.equals("on") ? 
		    		     "  and available_ticket > 0" : "");

			PreparedStatement selsectStmt = connection.prepareStatement(selectStr);
			selsectStmt.setString(1, "%" + term + "%");
			selsectStmt.setString(2, "%" + term + "%");
			
			ResultSet resultSet = selsectStmt.executeQuery();
			
			while (resultSet.next()) {
				int eventId = resultSet.getInt("id");
				String eventName = resultSet.getString("name");
				String eventDesc = resultSet.getString("description");
				int totalTickets = resultSet.getInt("total_ticket");
				int availableTickets = resultSet.getInt("available_ticket");
				logger.trace("eventId: " + eventId);
				logger.trace("eventName: " + eventName);
				logger.trace("eventDesc: " + eventDesc);
				logger.trace("totalTickets: " + totalTickets);
				logger.trace("availableTickets: " + availableTickets);
				
				Event resEvent = new Event();
				resEvent.setId(eventId);
				resEvent.setName(eventName);
				resEvent.setDescription(eventDesc);
				resEvent.setTotalTickets(totalTickets);
				resEvent.setAvailableTickets(availableTickets);
				
				resEvents.add(resEvent);				
			}
				
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			logger.trace("SQLException: " + sqle.getMessage());
		} 
		
		return resEvents;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
