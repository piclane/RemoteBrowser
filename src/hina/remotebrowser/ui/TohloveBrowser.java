package hina.remotebrowser.ui;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

/**
 * 刀剣乱舞 UI
 * 
 * @author piclane
 */
public class TohloveBrowser extends AbstractBrowser {
	/** 刀剣乱舞メイン URL */
	private static final String TOHLOVE_URL = "http://www.dmm.com/netgame/social/-/gadgets/=/app_id=825012/";
	
	/** 刀剣乱舞のメイン Flash のある Iframe URL の接頭 */
	private static final String TOHLOVE_IFRAME_URL_PREFIX = "http://osapi.dmm.com/gadgets/ifr?";

	/** DMM ログイン URL の接頭 */
	private static final String DMM_LOGIN_URL_PREFIX = "https://www.dmm.com/my/-/login/";
	
	/** Flash の幅 */
	private static final int FLASH_WIDTH = 960;
	
	/** Flash の高さ */
	private static final int FLASH_HEIGHT = 580;
	
	/**
	 * コンストラクタ
	 * 
	 * @param display {@link Display}
	 */
	public TohloveBrowser(Display display) {
		super(display);
		
		shell.setText("とうらぶ鯖");
		shell.setSize(FLASH_WIDTH, FLASH_HEIGHT);
	}
	
	/**
	 * UI を開始します
	 */
	public void start() {
		super.start();
		
		// 初期 URL を設定
		browser.setUrl(TOHLOVE_URL);
	}
	
	/**
	 * @see hina.remotebrowser.ui.AbstractBrowser#onReady()
	 */
	@Override
	protected void onReady() {
		String url = browser.getUrl();
		if(url.startsWith(DMM_LOGIN_URL_PREFIX)) {
			handleLogin();
		} else if(url.startsWith(TOHLOVE_URL)) {
			handleMainUI();
		} else if(url.startsWith(TOHLOVE_IFRAME_URL_PREFIX)) {
			handleIframeUI();
		}		
	}
	
	/**
	 * ログイン画面が表示された時の処理
	 */
	private void handleLogin() {
//		Browser browser = this.browser;
//		
//		browser.execute(
//				"jQuery('#login_id').val('piclane@gmail.com');" +
//				"jQuery('#password').val('178594');" +
//				"jQuery('form.validator.login').submit();");
	}
	
	/**
	 * 刀剣乱舞メイン画面が表示された時の処理
	 */
	private void handleMainUI() {
		Browser browser = this.browser;
		Object iframeUrl = browser.evaluate("return jQuery('#game_frame').prop('src');");
		if(iframeUrl != null) {
			browser.setUrl(iframeUrl.toString());
		} else {
			System.err.println("Iframe is missing.");
		}
	}
	
	/**
	 * 刀剣乱舞Iframeが表示された時の処理
	 */
	private void handleIframeUI() {
		Browser browser = this.browser;
		
		browser.execute(
				"jQuery('html,body').css({margin:0, padding: 0, overflow: 'hidden'});" + 
				"jQuery('#contents').css({margin:0, padding: 0, minHeight: 0});" +
				"jQuery('#copyright,#html_contents,#faq_banner,#footer_contents').detach();");

		Rectangle outer = shell.getBounds();
        Rectangle inner = shell.getClientArea();
        shell.setSize(FLASH_WIDTH, FLASH_HEIGHT + (outer.height - inner.height));
	}
}
