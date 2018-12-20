package experiments;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		Cookie[] cookies = request.getCookies();
		int visits = 0;
		if(cookies != null) {
			for(Cookie c: cookies) {
				System.out.println("Cookie - Name: " + c.getName() + " Value: " + c.getValue());
				if(c.getName().equals("visits")) {
					//TODO: catch an exception here!
					visits = Integer.parseInt(c.getValue());
				}
			}
		}
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);

		String visitsStr = String.valueOf(++visits);
		response.addCookie(new Cookie("visits", visitsStr));

		PrintWriter out = response.getWriter();
		out.println("<html><title>CookieServlet</title><body>There have been " + 
				visits + " visits.</body></html>");

	}

}
