package cs601.project4;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Handle Sign up function. 
 * @author Kei Fukutani
 */
public class SignUpServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static String BASE_PATH = TicketPurchaseWebApp.getBasePath();
	private static Logger logger = LogManager.getLogger();

	
	/**
	 * SignUpServlet#doGet
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String path = "";
		String status = request.getParameter("status");
		if (status != null && status.equals("invalid")) {
			path = BASE_PATH + "/" + "signup_failed.html";
		} else {
			path = BASE_PATH + "/" + "signup.html";
		}

		// Respond to client. 
		try (InputStream fis = new FileInputStream(path);
			 OutputStream outputStream = response.getOutputStream()) {

			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);

			// Send HTTP Response. 
			int byteRead;
			while ((byteRead = fis.read()) != -1) {
				outputStream.write(byteRead);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} 

	}


	/**
	 * SignUpServlet#doPost
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		String username = request.getParameter("username").trim();
		String password = request.getParameter("password").trim();
		logger.trace("username: " + username);
		logger.trace("password: " + password);
		if (username.equals("") || password.equals("")) {
			logger.trace("Either username or password is empty.");
			response.sendRedirect("/signup?status=invalid");
			return;
		} 
		
		// Access MySQL and sign up the user. 
		boolean success = TicketPurchaseWebApp.getDbManager().signUp(username, password);

		if (success) {
			// Attach username to session. 
			HttpSession session = request.getSession();
			session.setAttribute("username", username);

			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);

			out.println("<html><head>");
			out.println("<title>REGISTERED PAGE</title>");
			out.println("</head><body>");
			out.println("<h3>Your account has been created!</h3><br/>");
			out.println("<a href=\"/main\" >Main Page</a><br/>");
			out.println("<a href=\"/logout\" >Logout</a><br/>");
			out.println("</body></html>");
						
		} else {
			response.sendRedirect("/signup?status=invalid");
		}
		
		out.close();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
}
