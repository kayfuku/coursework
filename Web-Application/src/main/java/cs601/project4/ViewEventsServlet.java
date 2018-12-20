package cs601.project4;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Display all events. It also has search functionality.  
 * @author Kei Fukutani
 */
public class ViewEventsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger();

	
	/**
	 * ViewEventsServlet#doGet
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		doPost(request, response);
	}
	
	
	/**
	 * ViewEventsServlet#doPost
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String username = TicketPurchaseWebApp.getUtilities().checkSession(request, response);
		logger.trace("username: " + username);
		
		// If user is not authenticated, go back to Login Page. 
		if (username == null) {
			response.sendRedirect("/login");
			return;
		}
		
		String term = request.getParameter("term");
		if (term != null) {
			term = term.trim();
		} else {
			term = "";
		}
		String ticketAvailable = request.getParameter("ticketAvailable");
		logger.trace("doPost() term: " + term);
		logger.trace("doPost() ticketAvailable: " + ticketAvailable);
		
		PrintWriter out = response.getWriter();
		
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		
		out.println("<html><title>ViewEventsServlet</title><body>");
		out.println("<h3>View Events Page</h3>");
		out.println("Hello, " + username + "!<br/><br/>");
		out.println("<a href=\"/main\">Main Page</a><br/><br/>");
		
		out.println("<form method=\"post\" action=\"/viewevents\">");
		out.println("Search: ");
		out.println("<input type=\"text\" name=\"term\" value=\"" + term + "\" /><br/>");
		out.println("Ticket available: ");
		out.println("<input type=\"checkbox\" name=\"ticketAvailable\" " + 
		            (ticketAvailable != null && ticketAvailable.equals("on") ? "checked" : "") + 
		            " /><br/>");
		out.println("<input type=\"submit\" name=\"button\" value=\"Search\" /></form>");
				
		// Access MySQL and get results. 
		ArrayList<Event> events = TicketPurchaseWebApp.getDbManager().searchEvent(term, ticketAvailable);
		
		out.println("<table border=\"1\">");
		out.println("<tr><th>Event Name</th></tr>");
		for (Event event : events) {
			int eventId = event.getId();
			String eventName = event.getName();
			String eventDesc = event.getDescription();
			out.println("<tr>");
			out.println("<td><a href=\"/eventdesc?eventId=" + eventId + "&eventName=" + eventName + "&eventDesc=" + eventDesc + "\" >" + eventName + "</a></td>");
			out.println("</tr>");
		}
		out.println("</table>");

		out.println("</body></html>");
		
		out.close();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
