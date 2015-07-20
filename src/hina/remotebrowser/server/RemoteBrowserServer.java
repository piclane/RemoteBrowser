package hina.remotebrowser.server;

import hina.remotebrowser.server.servlet.ClickServlet;
import hina.remotebrowser.server.servlet.ImageServlet;
import hina.remotebrowser.server.servlet.RefreshServlet;
import hina.remotebrowser.ui.Browser;
import hina.remotebrowser.ui.ResourceProvider;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class RemoteBrowserServer {
	/** サーバー */
	private final Server server;
	
	/** コンテキストを保持する為の {@link HandlerCollection} */
	private final HandlerCollection handlers;
	
	/**
	 * コンストラクタ
	 */
	public RemoteBrowserServer() {
		Server server = new Server(new InetSocketAddress(10080));
		
		HandlerCollection handlers = new HandlerCollection();
		
		server.setHandler(handlers);
		
        this.server = server;
        this.handlers = handlers;
	}
	
	/**
	 * ブラウザを追加します
	 * 
	 * @param contextName コンテキスト名
	 * @param browser ブラウザのインスタンス
	 */
	public void addBrowser(String contextName, Browser browser) {
		ServletContextHandler scHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
		scHandler.setContextPath("/" + contextName);
		scHandler.addFilter(new FilterHolder(new ExceptionFilter()), "/*", null);
		scHandler.addServlet(ImageServlet.class, "/image");
		scHandler.addServlet(ClickServlet.class, "/click");
		scHandler.addServlet(RefreshServlet.class, "/refresh");
		scHandler.setAttribute("browser", browser);
		
		ResourceHandler resHandler = new ResourceHandler();
		resHandler.setResourceBase(ResourceProvider.webUrl().toExternalForm());
		resHandler.setWelcomeFiles(new String[] { "index.html" });
		scHandler.insertHandler(resHandler);
		
		handlers.addHandler(scHandler);
	}
	
	/**
	 * ブラウザを削除します
	 * 
	 * @param contextName コンテキスト名
	 */
	public void removeBrowser(String contextName) {
		String contextPath = "/" + contextName;
		Handler[] _handlers;
		_handlers = handlers.getHandlers();
		_handlers = Arrays.copyOf(_handlers, _handlers.length);
		for(Handler handler: _handlers) {
			if(handler instanceof ContextHandler) {
				ContextHandler h = (ContextHandler)handler;
				if(contextPath.equals(h.getContextPath())) {
					handlers.removeHandler(handler);
					return;
				}
			}
		}
	}
	
	/**
	 * サーバーを開始します
	 * 
	 * @throws Exception サーバーの開始に失敗した時
	 */
	public void start() throws Exception {
		server.start();
	}
	
	/**
	 * サーバーを停止します
	 * 
	 * @throws Exception サーバーの停止に失敗した時
	 */
	public void stop() throws Exception {
		server.stop();
	}
	
	/**
	 * 各種例外をステータスコードとしてクライアントに応答するためのフィルタ
	 */
	private static class ExceptionFilter implements Filter {
		/**
		 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
		 */
		@Override
		public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
			HttpServletResponse _resp = (HttpServletResponse)resp;
			
			try {
				chain.doFilter(req, resp);
			} catch (BadRequestException e) {
				_resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			} catch (ServiceUnavailableException e) {
				_resp.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
			}
		}

		/**
		 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
		 */
		@Override
		public void init(FilterConfig arg0) throws ServletException {
			// nothing to do
		}

		/**
		 * @see javax.servlet.Filter#destroy()
		 */
		@Override
		public void destroy() {
			// nothing to do
		}
	}
}
