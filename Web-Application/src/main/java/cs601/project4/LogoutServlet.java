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
 * Handle log out function. 
 * @author Kei Fukutani
 */
public class LogoutServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger();
	private static String BASE_PATH = TicketPurchaseWebApp.getBasePath();


	/**
	 * LogoutSevlet#doGet
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		// Invalidate the current session. 
		HttpSession session = request.getSession();
		logger.trace("session: " + session);
		session.invalidate();
		
		PrintWriter out = response.getWriter();

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);

		out.println("<html><head>");
		out.println("<title>LOGGED OUT PAGE</title>");
		out.println("</head><body>");
		out.println("<h3>You logged out!</h3><br/>");
		out.println("<a href=\"/login\" >Login</a><br/>");
		out.println("</body></html>");
		
		out.close();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
