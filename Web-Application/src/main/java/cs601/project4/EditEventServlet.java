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
 * Handle editing and deleting an event.
 * @author Kei Fukutani
 */
public class EditEventServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Logger logger = LogManager.getLogger();
	
	private String username;
	private String eventId;
	private String eventName;
	private String eventDesc;
	private String totalTickets;
	
	
	/**
	 * EditEventServlet#doGet
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		doPost(request, response);
	}	
	
	
	/**
	 * EditEventServlet#doPost
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String username = TicketPurchaseWebApp.getUtilities().checkSession(request, response);
		logger.trace("doGet() session username: " + username);
		
		// If user is not authenticated, go back to Login Page. 
		if (username == null) {
			response.sendRedirect("/login");
			return;
		}
		
		eventId = request.getParameter("eventId");
		eventName = request.getParameter("eventName");
		eventDesc = request.getParameter("eventDesc");
		totalTickets = request.getParameter("totalTickets");
		
		String button = request.getParameter("button");
		// Perform differently depending on the button user hit. 
		if (button.equals("Edit")) {
			sendEditPage(request, response);
		} else if (button.equals("Delete")) {
			sendDeletePage(request, response);
		} else if (button.equals("Save")) {
			updateEvent(request, response);
		} else if (button.equals("Delete This Event!")) {
			deleteEvent(request, response);
		}
			
	}


	/**
	 * Send Edit Event Page to client. 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void sendEditPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String username = TicketPurchaseWebApp.getUtilities().checkSession(request, response);
		
        PrintWriter out = response.getWriter();
		
		response.setContentType("text/html");
		
        out.println("<html><title>EditEventServlet</title><body>");
		out.println("<h3>Edit Event Page</h3>");
		out.println("Hello, " + username + "!<br/><br/>");
		
	    out.println("<form method=\"post\" action=\"/editevent?eventId=" + eventId + "\">");
	    out.println("Event ID: " + eventId + "<br/>");
		out.println("Event Name: ");
		out.println("<input type=\"text\" name=\"eventName\" value=\"" + eventName + "\" /><br/>");
		out.println("Event Description: ");
		out.println("<input type=\"text\" name=\"eventDesc\" value=\"" + eventDesc + "\" /><br/>");
		out.println("Total Number of Tickets: ");
		out.println("<input type=\"text\" name=\"totalTickets\" value=\"" + totalTickets + "\" /><br/>");
		
		String status = request.getParameter("status");
		if (status != null && status.equals("invalid")) {
			String message = request.getParameter("message");
			out.println("Saving event failed.<br/>");
			out.println(message + "<br/>");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);			
		} else if (status != null && status.equals("emptyFields")) {
			out.println("All fields are required. Saving event failed.<br/><br/>");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} else {
			out.println("<br/><br/>");		
			response.setStatus(HttpServletResponse.SC_OK);
		}
		out.println("<input type=\"submit\" name=\"button\" value=\"Save\" /></form>");
		out.println("<a href=\"/myaccount\">Cancel</a><br/><br/>");
		
		out.println("</body></html>");
	}
	
	
	/**
	 * This is called when user presses the Save button. 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void updateEvent(HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.trace("updateEvent() start.");
				
        PrintWriter out = response.getWriter();
		
		if (eventName.equals("") || eventDesc.equals("") || totalTickets.equals("")) {
			response.sendRedirect("/editevent?status=emptyFields&button=Edit&eventId=" + eventId +
					              "&eventName=" + eventName + "&eventDesc=" + eventDesc +
					              "&totalTickets=" + totalTickets);
			return;
		}
		
		// Access MySQL and save the event. 
		String message = TicketPurchaseWebApp.getDbManager()
				              .saveMyEvent(eventId, eventName, eventDesc, totalTickets);
		logger.trace(message);
		
		if (message.equals("OK")) {
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);

			out.println("<html><head>");
			out.println("<title>EVENT SAVED PAGE</title>");
			out.println("</head><body>");
			out.println("<h3>Your event (Event Name: " + eventName + ", Event Description: " + eventDesc + 
					    ", Total Number of Tickets: " + totalTickets + ") has been saved!</h3><br/>");
			out.println("<a href=\"/main\" >Main Page</a><br/>");
			out.println("<a href=\"/logout\" >Logout</a><br/>");
			out.println("</body></html>");
						
		} else {
			response.sendRedirect("/editevent?status=invalid&button=Edit&eventId=" + eventId + 
					              "&eventName=" + eventName + "&eventDesc=" + eventDesc + 
					              "&totalTickets=" + totalTickets + "&message=" + message);
		}
		
		out.close();
	}
	
	
	/**
	 * Send Delete Event Page to client. 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void sendDeletePage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String username = TicketPurchaseWebApp.getUtilities().checkSession(request, response);
		
		PrintWriter out = response.getWriter();
		
		response.setContentType("text/html");
		
        out.println("<html><title>EditEventServlet</title><body>");
		out.println("<h3>Delete Event Page</h3>");
		out.println("Hello, " + username + "!<br/><br/>");
		out.println("Are you sure you want to delete this event?<br/><br/>");		
	    out.println("<form method=\"post\" action=\"/editevent?" + 
    		        "eventId=" + eventId + 
    		        "&eventName=" + eventName + 
    		        "&eventDesc=" + eventDesc + 
    		        "&totalTickets=" + totalTickets + 	    		    
	    		    "\">");
	    out.println("Event ID: " + eventId + "<br/>");
		out.println("Event Name: "+ eventName + "<br/>");
		out.println("Event Description: "+ eventDesc + "<br/>");
		out.println("Total Number of Tickets: "+ totalTickets + "<br/>");
		
		String status = request.getParameter("status");
		if (status != null && status.equals("invalid")) {
			out.println("Deleting event failed.<br/><br/>");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);			
		} else {
			out.println("<br/><br/>");		
			response.setStatus(HttpServletResponse.SC_OK);
		}
		out.println("<input type=\"submit\" name=\"button\" value=\"Delete This Event!\" /></form>");
		out.println("<a href=\"/myaccount\">Cancel</a><br/><br/>");
		
		out.println("</body></html>");	
	}
	
	
	/**
	 * This is called when user presses the 'Delete This Event!' button. 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void deleteEvent(HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.trace("deleteEvent() starts.");

		PrintWriter out = response.getWriter();

		// Access MySQL and delete a row in the event table. 
		boolean success = TicketPurchaseWebApp.getDbManager().deleteEventRow(eventId);

		if (success) {
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);

			out.println("<html><head>");
			out.println("<title>EVENT DELETED PAGE</title>");
			out.println("</head><body>");
			out.println("<h3>Your event (Event Id: " + eventId + ", Event Name: " + eventName + 
					    ", Event Description: " + eventDesc + 
					    ", Total Number of Tickets: " + totalTickets + ") has been deleted!</h3><br/>");
			out.println("<a href=\"/main\" >Main Page</a><br/>");
			out.println("<a href=\"/logout\" >Logout</a><br/>");
			out.println("</body></html>");

		} else {
			response.sendRedirect("/editevent?status=invalid&button=Delete&eventId=" + eventId + 
					              "&eventName=" + eventName + "&eventDesc=" + eventDesc + 
					              "&totalTickets=" + totalTickets);
		}

		out.close();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


	

}
