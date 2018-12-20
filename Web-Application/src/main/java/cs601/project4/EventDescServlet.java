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
 * Display event description. It also has 'Buy Ticket' button, and 
 * handle ticket purchase functionality. 
 * @author Kei Fukutani
 */
public class EventDescServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger();

	
	/**
	 * EventDescServlet#doGet
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
		
		String eventId = request.getParameter("eventId");
		String eventName = request.getParameter("eventName");
		String eventDesc = request.getParameter("eventDesc");
		
		PrintWriter out = response.getWriter();
		
		response.setContentType("text/html");
		
		out.println("<html><title>EventDescServlet</title><body>");
		out.println("<h3>View Event Description Page</h3>");
		out.println("Hello, " + username + "!<br/><br/>");
		out.println("<h3>" + eventName + "</h3>");
		out.println("<p>" + eventDesc + "</p>");	
		
		
		// Access MySQL and check to see if a ticket for the specified event is available. 
		int num = TicketPurchaseWebApp.getDbManager().checkAvailableTicket(eventId);
		if (num == 0) {
			out.println("Sold out!");	
		} else {
			out.println("<form method=\"post\" action=\"/eventdesc?" +
				"eventId=" + eventId + "&eventName=" + eventName + 
				"&eventDesc=" + eventDesc + "\"><input type=\"submit\" value=\"Buy Ticket\" /></form>");	
		}
		
		String status = request.getParameter("status");
		if (status != null && status.equals("invalid")) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			out.println("Purchase failed.<br/>");		
		} else {
			out.println("<br/><br/>");		
			response.setStatus(HttpServletResponse.SC_OK);
		}
		out.println("<a href=\"/main\">Main Page</a>");
		
		out.println("</body></html>");
	}
	
	
	/**
	 * EventDescServlet#doPost
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		HttpSession session = request.getSession();
		String newOwner = (String) session.getAttribute("username");
		String eventId = request.getParameter("eventId");
		String eventName = request.getParameter("eventName");
		String eventDesc = request.getParameter("eventDesc");

		logger.trace("doPost() username: " + newOwner);
		logger.trace("doPost() eventId: " + eventId);
		
		// Access MySQL and update the owner of the ticket in the ticket table and 
		// update the available_ticket in the event table.  
		boolean success = TicketPurchaseWebApp.getDbManager().buyTicket(eventId, newOwner);

		if (success) {
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);

			out.println("<html><head>");
			out.println("<title>PURCHASED PAGE</title>");
			out.println("</head><body>");
			out.println("<h3>You has purchased ticket (Event Name: " + eventName + ")!</h3><br/>");
			out.println("<a href=\"/main\" >Main Page</a><br/>");
			out.println("<a href=\"/logout\" >Logout</a><br/>");
			out.println("</body></html>");
						
		} else {
			response.sendRedirect("/eventdesc?status=invalid" + 
					    "&eventId=" + eventId + "&eventName=" + eventName + "&eventDesc=" + eventDesc);
		}
		
		out.close();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
