package cs601.project4;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.util.DateCache.Tick;


/**
 * Handle log-in function. 
 * @author Kei Fukutani
 */
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger();
	private static String BASE_PATH = TicketPurchaseWebApp.getBasePath();

	
	/**
	 * LoginServlet#doGet
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		logger.trace("doGet username: " + username);
		
		// If user is authenticated, go to Main Page. 
		if (username != null) {
			response.sendRedirect("/main");
			return;
		}
		
		String path = "";
		String status = request.getParameter("status");
		if (status != null && status.equals("invalid")) {
			path = BASE_PATH + "/" + "login_failed.html";
		} else {
			path = BASE_PATH + "/" + "login.html";
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
	 * LoginServlet#doPost
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String username = request.getParameter("username").trim();
		String password = request.getParameter("password").trim();
		logger.trace("doPost username: " + username);
		logger.trace("doPost password: " + password);

		// Access MySQL and authenticate the user. 
		boolean success = TicketPurchaseWebApp.getDbManager().authenticate(username, password);
		
		if (success) {
			// Attach username to the session. 
			HttpSession session = request.getSession();
			session.setAttribute("username", username);
			
			response.sendRedirect("/main");
			
		} else {
			response.sendRedirect("/login?status=invalid");
		}

	}


	



























}
