package cs601.project4;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import com.mysql.cj.util.Util;


/**
 * Run HTTP server using Jetty and add all servlets for Ticket Purchase Web Application. 
 * @author Kei Fukutani
 */
public class TicketPurchaseWebApp {
	
//	private static String CONFIGURATION_FILE = "config/ticketpurchase_app_config.properties";
	private static String CONFIGURATION_FILE = "config/ticketpurchase_app_config_for_mc.properties";
	private static int PORT;
	private static String BASE_PATH;
	private static Logger logger = LogManager.getLogger();
	
	private static DbManager dbManager;
	private static Utilities utilities;
	
	public static String getConfig() {
		return CONFIGURATION_FILE;
	}
	public static DbManager getDbManager() {
		return dbManager;
	}
	public static Utilities getUtilities() {
		return utilities;
	}
	public static String getBasePath() {
		return BASE_PATH;
	}
	
	
	/**
	 * Run HTTP server using Jetty and add all servlets. 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		initialize();
		
		dbManager = new DbManager();
		utilities = new Utilities();
		Server server = new Server(PORT);
		
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		server.setHandler(context);
		
		context.addServlet(LoginServlet.class, "/login");
		context.addServlet(LogoutServlet.class, "/logout");
		context.addServlet(SignUpServlet.class, "/signup");
		context.addServlet(MainServlet.class, "/main");
		context.addServlet(ViewEventsServlet.class, "/viewevents");
		context.addServlet(EventDescServlet.class, "/eventdesc");
		context.addServlet(MyAccountServlet.class, "/myaccount");
		context.addServlet(TicketTransferServlet.class, "/transfer");
		context.addServlet(CreateEventServlet.class, "/createevent");
		context.addServlet(EditEventServlet.class, "/editevent");
		
		server.start();
		server.join();
	}

	
	/**
	 * Reads the configuration file and gets some deployment information. 
	 */
	private static void initialize() {

		Properties config = new Properties();
		
		logger.trace("config file: " + CONFIGURATION_FILE);
		try (FileInputStream input = new FileInputStream(CONFIGURATION_FILE)) {

			config.load(input);
			
			PORT = Integer.parseInt(config.getProperty("port"));
			BASE_PATH = config.getProperty("basePath");
			
		} catch (IOException e) {
			logger.trace(e.getMessage() + ", not found.");
			logger.trace("Bye.");
			System.exit(1);
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
