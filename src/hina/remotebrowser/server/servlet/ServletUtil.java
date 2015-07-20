package hina.remotebrowser.server.servlet;

import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.http.HttpServletResponse;

/**
 * サーブレットで使う用のユーティリティーメソッド達
 * 
 * @author piclane
 */
class ServletUtil {
	/**
	 * キャッシュ撃退用のヘッダーを送出します
	 * 
	 * @param resp {@link HttpServletResponse}
	 */
	public static void sendCacheBuster(HttpServletResponse resp) {
		resp.addHeader("Cache-Control", "no-cache, must-revalidate");
		resp.addHeader("Cache-Control", "post-check=0, pre-check=0");
		resp.addHeader("Pragma", "no-cache");
	}
	
	/**
	 * キャッシュ撃退用のヘッダーを送出します
	 * 
	 * @param resp {@link HttpServletResponse}
	 * @param contentType 一緒に送るコンテントタイプ
	 */
	public static void sendCacheBuster(HttpServletResponse resp, String contentType) {
		resp.setContentType(contentType);
		sendCacheBuster(resp);
	}
	
	/**
	 * 「{"success":true}」という JSON を送出します。
	 * ヘッダとかキャッシュバスターなんかも一緒に送出します。
	 * 
	 * @param resp {@link HttpServletResponse}
	 * @throws IOException 送出に失敗した時
	 */
	public static void sendJsonSuccess(HttpServletResponse resp) throws IOException {
		ServletUtil.sendCacheBuster(resp, "application/json; charset=utf-8");
		
		try(PrintStream ps = new PrintStream(resp.getOutputStream())) {
			ps.print("{\"success\":true}");
		}
	}
}
