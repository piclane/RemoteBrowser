package hina.remotebrowser.server.servlet;

import hina.remotebrowser.server.utils.ServletRequestParams;
import hina.remotebrowser.ui.Browser;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ClickServlet extends HttpServlet {
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	/**
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ServletContext context = getServletContext();
		Browser browser = (Browser) context.getAttribute("browser");
		ServletRequestParams params = new ServletRequestParams(req);
		
		int x = params.getInt("x");
		int y = params.getInt("y");

		browser.click(x, y);
		
		ServletUtil.sendJsonSuccess(resp);
	}
	
}
