package hina.remotebrowser;

import hina.remotebrowser.server.RemoteBrowserServer;
import hina.remotebrowser.server.ServiceUnavailableException;
import hina.remotebrowser.ui.Browser;
import hina.remotebrowser.ui.TohloveBrowser;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Display;

public class Main {
	
	/**
	 * エントリポイント
	 * 
	 * @param args 引数(使用しません)
	 */
	public static void main(String[] args) throws Exception {
		Main main = new Main();
		main.start();
		main.dispose();
	}
	
	/** {@link Display} */
	private final Display display;
	
	/** サーバー */
	private final RemoteBrowserServer server;
	
	/** ユーザーインターフェイス */
	private final Browser browser;
	
	/**
	 * コンストラクタ
	 * 
	 * @throws IOException
	 */
	public Main() throws IOException {
		Display display = createDisplay();
		TohloveBrowser browserTohlove = new TohloveBrowser(display);
		RemoteBrowserServer server = new RemoteBrowserServer();
		
		server.addBrowser("tohlove", (Browser)createDisplaySyncProxy(display, new Class<?>[] {Browser.class}, browserTohlove));
		
		this.display = display;
		this.server = server;
		this.browser = browserTohlove;
	}
	
	/**
	 * {@link Display} を作成します
	 * 
	 * @return 作成された {@link Display}
	 */
	private static Display createDisplay() {
		try {
			return Display.getDefault();
		} catch (SWTException e) {
			if("Mac OS X".equalsIgnoreCase(System.getProperty("os.name"))) {
				throw new RuntimeException("VM引数に -XstartOnFirstThread を付けて再起動してください", e);
			} else {
				throw e;
			}
		}
	}

	/**
	 * 処理を開始します
	 */
	private void start() {
		try {
			server.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		browser.start();
		while (!browser.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	/**
	 * 破棄します
	 */
	private void dispose() {
		display.dispose();
		
		try {
			server.stop();
		} catch (Exception e) {
			// TODO Log
			throw new RuntimeException(e);
		}
	}

	/**
	 * メソッド呼出しが実行される度に {@link Display#syncExec(Runnable)} によるキューの解決を待機する、
	 * 指定されたインタフェースのプロキシ・クラスのインスタンスを返します。
	 * 
	 * @param display 同期するための {@link Display}
	 * @param interfaces プロキシ・クラスが実装するインタフェースのリスト
	 * @param instance プロキシされる先の実装のインスタンス
	 * @return プロキシ・クラスのインスタンス
	 */
	private static Object createDisplaySyncProxy(final Display display, final Class<?>[] interfaces, final Object instance) {
		return Proxy.newProxyInstance(
				Main.class.getClassLoader(), 
				interfaces,
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						if(display == null || display.isDisposed()) {
							throw new ServiceUnavailableException();
						}
						
						Object[] result = new Object[1];
						Throwable[] exception = new Throwable[1];
						display.syncExec(new Runnable() {
							@Override
							public void run() {
								try {
									result[0] = method.invoke(instance, args);
								} catch (IllegalAccessException | IllegalArgumentException e) {
									throw new InternalError(e);
								} catch (InvocationTargetException e) {
									exception[0] = e.getTargetException();
								}
							}
						});
						
						if(result[0] != null) {
							return result[0];
						}
						if(exception[0] != null) {
							if(exception[0] instanceof SWTException) {
								throw new ServiceUnavailableException(exception[0]);
							}
							throw exception[0];
						}
						return null;
					}
				});
	}
}
