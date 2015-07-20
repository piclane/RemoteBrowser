package hina.remotebrowser.server.utils;

import java.io.UnsupportedEncodingException;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * サーブレットのリクエストパラメーターを取得するユーティリティークラス
 * 
 * @author yohei_hina
 */
public class ServletRequestParams extends RequestParams {

	/** ServletRequest */
	private final ServletRequest req;
	
	/** クッキーパラメータ */
	private CookieParams cookieParams;
	
	/**
	 * コンストラクタ
	 * 
	 * @param req {@link ServletRequest}
	 * @param charSet 文字セット
	 * @throws UnsupportedEncodingException 
	 */
	public ServletRequestParams(ServletRequest req, String charSet) throws UnsupportedEncodingException {
		this.req = req;
		this.req.setCharacterEncoding(charSet);
	}
	
	/**
	 * コンストラクタ
	 * 文字セットはUTF-8になります
	 * 
	 * @param req {@link ServletRequest}
	 */
	public ServletRequestParams(ServletRequest req) {
		try {
			this.req = req;
			this.req.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new InternalError(e);
		}
	}
	
	/**
	 * {@link ServletRequest} からパラメーターを取得します。
	 * {@link ServletRequest} から生の値を返す点で {@link #getParameter(String)} と異なります。
	 * 
	 * @param name パラメーター名
	 * @return 指定されたパラメーター名の値
	 */
	public String getRawParameter(String name) {
		return req.getParameter(name);
	}
	
	/**
	 * クッキーパラメーターを取得します
	 * 
	 * @return クッキーパラメーター
	 */
	public CookieParams getCookieParams() {
		if(cookieParams == null) {
			if(req instanceof HttpServletRequest) {
				cookieParams = new CookieParams((HttpServletRequest)req);
			} else {
				cookieParams = CookieParams.NOP;
			}
		}
		return cookieParams;
	}
}
