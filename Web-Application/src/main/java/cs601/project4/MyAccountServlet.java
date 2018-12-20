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
 * Display username and tickets that the user purchased, 
 * and the events that the user created.  
 * @author Kei Fukutani
 */
public class MyAccountServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger();

	
	/**
	 * MyAccountServlet#doGet
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String username = TicketPurchaseWebApp.getUtilities().checkSession(request, response);
		logger.trace("session username: " + username);
		
		// If user is not authenticated, go back to Login Page. 
		if (username == null) {
			response.sendRedirect("/login");
			return;
		}
		
		PrintWriter out = response.getWriter();
		
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		
		out.println("<html><title>MyAccountServlet</title><body>");
		
		out.println("<h3>My Account Page</h3>");
		out.println("Hello, " + username + "!<br/><br/>");
		out.println("<a href=\"/main\">Main Page</a><br/><br/>");
		out.println("username: " + username + "<br/><br/>");
		
		out.println("My Tickets: <br/>");
		// Access MySQL and get the list of my tickets. 
        ArrayList<MyTicket> myTickets = TicketPurchaseWebApp.getDbManager().getMyTickets(username);
		
		out.println("<table border=\"1\">");
		out.println("<tr><th>Ticket ID</th><th>Event Name</th><th>Ticket Transfer</th></tr>");
		for (MyTicket myTicket : myTickets) {
			int ticketId = myTicket.getTicketId();
			String eventName = myTicket.getEventName();
			out.println("<tr>");
			out.println("<td>" + ticketId + "</td>");
			out.println("<td>" + eventName + "</td>");
			out.println("<td><a href=\"/transfer?ticketId=" + ticketId + "&eventName=" + eventName + 
					    "\" >transfer</a></td>");
			out.println("</tr>");
		}
		out.println("</table><br/>");
		
		out.println("My Events: <br/>");
		// Access MySQL and get the list of my events. 
        ArrayList<Event> myEvents = TicketPurchaseWebApp.getDbManager().getMyEvents(username);
		
		out.println("<table border=\"1\">");
		out.println("<tr><th>Event ID</th><th>Event Name</th><th>Event Description</th>" + 
				    "<th>Total Number of Tickets</th><th>Available Tickets</th><th>Edit/Delete</th></tr>");
		for (Event myEvent : myEvents) {
			int eventId = myEvent.getId();
			String eventName = myEvent.getName();
			String eventDesc = myEvent.getDescription();
			int totalTickets = myEvent.getTotalTickets();
			int availableTickets = myEvent.getAvailableTickets();
			out.println("<tr>");
			out.println("<td>" + eventId + "</td>");
			out.println("<td>" + eventName + "</td>");
			out.println("<td>" + eventDesc + "</td>");
			out.println("<td>" + totalTickets + "</td>");
			out.println("<td>" + availableTickets + "</td>");
			out.println("<td><form method=\"post\" action=\"/editevent?" + 
					    "eventId=" + eventId + "&eventName=" + eventName + "&eventDesc=" + eventDesc + 
					    "&totalTickets=" + totalTickets + "&availableTickets=" + availableTickets + "\">" + 
				        "<input type=\"submit\" name=\"button\"value=\"Edit\" " + 
				         "style=\"background-color:#ffcc00;\" />" + 
				        "<input type=\"submit\" name=\"button\"value=\"Delete\" " + 
				         "style=\"background-color:#ffcc00;\" /></form>" + 
					    "</td>");
			out.println("</tr>");
		}
		out.println("</table>");
		
		out.println("</body></html>");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
