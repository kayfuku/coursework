package cs601.project4;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Hold useful methods throughout the app.  
 * @author Kei Fukutani
 */
public class Utilities {

	private static Logger logger = LogManager.getLogger();


	/**
	 * Check the session and get the username. 
	 * @param request
	 * @param response
	 * @return
	 */
	public String checkSession(HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");

		return username;
	}




}
