package hina.remotebrowser.server.servlet;

import hina.remotebrowser.server.BadRequestException;

import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class ServletUtil {
	public static void sendCacheBuster(HttpServletResponse resp, String contentType) {
		resp.setContentType(contentType);
		resp.addHeader("Cache-Control", "no-cache, must-revalidate");
		resp.addHeader("Cache-Control", "post-check=0, pre-check=0");
		resp.addHeader("Pragma", "no-cache");
	}
	
	public static void sendJsonSuccess(HttpServletResponse resp) throws IOException {
		ServletUtil.sendCacheBuster(resp, "application/json; charset=utf-8");
		
		try(PrintStream ps = new PrintStream(resp.getOutputStream())) {
			ps.print("{\"success\":true}");
		}
	}
	
	public static int parseIntParam(HttpServletRequest req, String name) throws BadRequestException {
		String value = req.getParameter(name);
		if(value == null) {
			throw new BadRequestException();
		}
		
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw new BadRequestException(e);
		}
	}
}
