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
 * Handle creating an event.
 * @author Kei Fukutani
 */
public class CreateEventServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger();
	
	
	/**
	 * CreateEventServlet#doGet
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String username = TicketPurchaseWebApp.getUtilities().checkSession(request, response);
		logger.trace("doGet() session username: " + username);
		
		// If user is not authenticated, go back to Login Page. 
		if (username == null) {
			response.sendRedirect("/login");
			return;
		}
		
        PrintWriter out = response.getWriter();
		
		response.setContentType("text/html");
		
        out.println("<html><title>CreateEventServlet</title><body>");
		out.println("<h3>Create Event Page</h3>");
		out.println("Hello, " + username + "!<br/><br/>");
		out.println("<a href=\"/main\">Main Page</a><br/><br/>");
		
	    out.println("<form method=\"post\" action=\"/createevent\">");
		out.println("Event Name: ");
		out.println("<input type=\"text\" name=\"eventName\" /><br/>");
		out.println("Event Description: ");
		out.println("<input type=\"text\" name=\"eventDesc\" /><br/>");
		out.println("Total Number of Tickets: ");
		out.println("<input type=\"text\" name=\"totalTickets\" /><br/>");
		
		String status = request.getParameter("status");
		if (status != null && status.equals("invalid")) {
			out.println("Creating event failed.<br/><br/>");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);			
		} else if (status != null && status.equals("emptyFields")) {
			out.println("All fields are required. Creating event failed.<br/><br/>");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} else {
			out.println("<br/><br/>");		
			response.setStatus(HttpServletResponse.SC_OK);
		}
		out.println("<input type=\"submit\" name=\"button\" value=\"Create\" /></form>");
		
		out.println("</body></html>");
	}
	
	
	/**
	 * CreateEventServlet#doPost
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		String username = TicketPurchaseWebApp.getUtilities().checkSession(request, response);

//		HttpSession session = request.getSession();
//		String username = (String) session.getAttribute("username");
		logger.trace("doPost() session username: " + username);
		
		String eventName = request.getParameter("eventName");
		String eventDesc = request.getParameter("eventDesc");
		String totalTickets = request.getParameter("totalTickets");
		logger.trace("eventName: " + eventName);
		logger.trace("eventDesc: " + eventDesc);
		logger.trace("totalTickets: " + totalTickets);
		
		if (eventName.equals("") || eventDesc.equals("") || totalTickets.equals("")) {
			response.sendRedirect("/createevent?status=emptyFields");
			return;
		}
		
		// Access MySQL and add a new event in the event and add tickets in the ticket table. 
		boolean success = TicketPurchaseWebApp.getDbManager()
				              .createEvent(eventName, eventDesc, totalTickets, username);
		
		if (success) {
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);

			out.println("<html><head>");
			out.println("<title>EVENT CREATED PAGE</title>");
			out.println("</head><body>");
			out.println("<h3>Your event (Event Name: " + eventName + ", Event Description: " + eventDesc + 
					    ", Total Number of Tickets: " + totalTickets + ") has been created!</h3><br/>");
			out.println("<a href=\"/main\" >Main Page</a><br/>");
			out.println("<a href=\"/logout\" >Logout</a><br/>");
			out.println("</body></html>");
						
		} else {
			response.sendRedirect("/createevent?status=invalid");
		}
		
		out.close();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
