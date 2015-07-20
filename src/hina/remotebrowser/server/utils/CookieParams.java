package hina.remotebrowser.server.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * サーブレットのクッキーパラメーターを取得するユーティリティークラス
 * 
 * @author yohei_hina
 */
public class CookieParams extends RequestParams {
	/** クッキーの配列 */
	private final Cookie[] cookies;
	
	/** 何も返さないダミー用 CookieParams */
	static final CookieParams NOP = new CookieParams();
	
	/**
	 * コンストラクタ
	 */
	private CookieParams() {
		this.cookies = null;
	}
	
	/**
	 * コンストラクタ
	 * 
	 * @param req {@link HttpServletRequest}
	 */
	CookieParams(HttpServletRequest req) {
		Cookie[] cookies = req.getCookies();
		if(cookies == null) {
			cookies = new Cookie[0];
		}
		this.cookies = cookies;
	}
	
	/**
	 * @see hina.remotebrowser.server.utils.RequestParams#getRawParameter(java.lang.String)
	 */
	@Override
	public String getRawParameter(String name) {
		assert name != null;
		
		if(cookies == null) {
			return null;
		}
		
		for(Cookie cookie: cookies) {
			String _name = cookie.getName();
			if(name.equals(_name)) {
				return cookie.getValue();
			}
		}
		return null;
	}

}
