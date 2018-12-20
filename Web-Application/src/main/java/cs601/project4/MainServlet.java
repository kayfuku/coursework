package cs601.project4;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Handle Main Page function. 
 * @author Kei Fukutani
 */
public class MainServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger();

	
	/**
	 * MainServlet#doGet
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String username = TicketPurchaseWebApp.getUtilities().checkSession(request, response);
		logger.trace("username: " + username);
		
		// If user is not authenticated, go back to Login Page. 
		if (username == null) {
			response.sendRedirect("/login");
			return;
		}
		
		PrintWriter out = response.getWriter();
		
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		
		out.println("<html><title>MainServlet</title><body>");
		out.println("<h3>Main Page</h3>");
		out.println("Hello, " + username + "!<br/><br/>");

		out.println("<a href=\"/viewevents\">View all events</a><br/>");
		out.println("<a href=\"/myaccount\">My account</a><br/>");
		out.println("<a href=\"/createevent\">Create event</a><br/>");
		out.println("<a href=\"/logout\">Logout</a><br/>");
		
		out.println("</body></html>");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
