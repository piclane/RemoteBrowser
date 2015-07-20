package hina.remotebrowser.ui;

import hina.remotebrowser.server.RemoteBrowserServer;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

public class ResourceProvider {
	

	public static URL webUrl() {
		return webUrl(null);
	}
	
	public static URL webUrl(String path) {
		if(path == null || path.isEmpty()) {
			path = "";
		} else if(!path.startsWith("/")){
			path = "/" + path;
		}
		
		try {
			URL jarUrl = RemoteBrowserServer.class.getProtectionDomain().getCodeSource().getLocation();
			URL webUrl;
			String jarPath = URLDecoder.decode(jarUrl.getFile(), "UTF-8");
			if(jarPath.endsWith(".jar")) {
				webUrl = new URL(jarUrl, "web" + path);
			} else {
				webUrl = new URL(jarUrl, "../web" + path);
			}
			
			return webUrl;
		} catch (MalformedURLException | UnsupportedEncodingException e) {
			throw new InternalError(e);
		}
	}
}
