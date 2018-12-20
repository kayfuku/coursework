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
 * Handle Ticket Transfer function. 
 * @author Kei Fukutani
 */
public class TicketTransferServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger();
	
	
	/**
	 * TicketTransferServlet#doGet
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
		
		String ticketId = request.getParameter("ticketId");
		String eventName = request.getParameter("eventName");
		
        PrintWriter out = response.getWriter();
		
		response.setContentType("text/html");
		
        out.println("<html><title>TicketTransferServlet</title><body>");
		out.println("<h3>Ticket Transfer Page</h3>");
		out.println("Hello, " + username + "!<br/><br/>");
		out.println("<a href=\"/main\">Main Page</a><br/><br/>");
		
		String status = request.getParameter("status");
		if (status != null && status.equals("invalid")) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			out.println("To whom? Enter a username.<br/>");		
			out.println("Transfer failed.<br/>");		
		} else {
			out.println("To whom? Enter a username.<br/><br/>");		
			response.setStatus(HttpServletResponse.SC_OK);
		}
		
		out.println("<table border=\"1\"><tr><th>Ticket Id</th><th>Event Name</th><th>To Whom</th></tr>");	
		out.println("<tr><td>" + ticketId + "</td><td>" + eventName + "</td>" + 
			"<td><form method=\"post\" action=\"/transfer?ticketId=" + ticketId + "&eventName=" + eventName +  
			"\"><input type=\"text\" name=\"toWhom\" />" + "</td></tr></table><br/>");
		out.println("<input type=\"submit\" name=\"button\" value=\"Transfer\" /></form>");
		
		out.println("</body></html>");
	}
	
	
	/**
	 * TicketTransferServlet#doPost
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		String toWhom = request.getParameter("toWhom").trim();
		String ticketId = request.getParameter("ticketId");
		String eventName = request.getParameter("eventName");

		logger.trace("toWhom: " + toWhom);
		logger.trace("ticketId: " + ticketId);
		logger.trace("eventName: " + eventName);
		if (toWhom.equals("")) {
			logger.trace("toWhom is empty.");
			response.sendRedirect("/transfer?status=invalid&ticketId=" + ticketId + "&eventName=" + eventName);
			return;
		} 
		
		// Access MySQL and change the owner of the ticket.  
		boolean success = TicketPurchaseWebApp.getDbManager().transferTicket(ticketId, toWhom);

		if (success) {
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);

			out.println("<html><head>");
			out.println("<title>TRANSFERRED PAGE</title>");
			out.println("</head><body>");
			out.println("<h3>Your ticket (id: " + ticketId + " eventName: " + eventName + 
					    ") has been transferred to " + toWhom + "!</h3><br/>");
			out.println("<a href=\"/main\" >Main Page</a><br/>");
			out.println("<a href=\"/logout\" >Logout</a><br/>");
			out.println("</body></html>");
						
		} else {
			response.sendRedirect("/transfer?status=invalid&ticketId=" + ticketId + "&eventName=" + eventName);
		}
		
		out.close();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
