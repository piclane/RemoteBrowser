package hina.remotebrowser.ui;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

/**
 * 抽象ブラウザ
 * 
 * @author yohei_hina
 */
abstract class AbstractBrowser implements hina.remotebrowser.ui.Browser {
	
	/** JQUERY ソースコード */
	private static final String JQUERY;
	
	/** DOM の準備ができた時に呼び出されるグローバル関数の名前 */
	private static final String READY_FUNCTION_NAME = "__ready";
	
	static {
		try(StringWriter writer = new StringWriter();
			Reader reader = new InputStreamReader(
					ResourceProvider.webUrl("jquery-2.1.4.js").openStream(), "UTF-8")) {
			int len;
			char[] buf = new char[4096];
			while((len = reader.read(buf)) != -1) {
				writer.write(buf, 0, len);
			}
			
			writer.write("\n");
			writer.write("jQuery.noConflict();\n");
			writer.write("jQuery(function() { " + READY_FUNCTION_NAME + "(); });");
			
			JQUERY = writer.toString();
		} catch (UnsupportedEncodingException e) {
			throw new InternalError(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/** {@link Display} */
	protected final Display display;
	
	/** メインウィンドウ */
	protected final Shell shell;
	
	/** メインブラウザ */
	protected final Browser browser;
	
	/**
	 * コンストラクタ
	 * 
	 * @param display {@link Display}
	 */
	public AbstractBrowser(Display display) {
		Shell shell = new Shell(display, SWT.TITLE | SWT.CLOSE);
		shell.setLayout(new FillLayout());
		shell.setText("とうらぶ鯖");
		
		Browser browser = new Browser(shell, SWT.NONE);
		
		this.display = display;
		this.shell = shell;
		this.browser = browser;
	}
	
	/**
	 * @see hina.remotebrowser.ui.Browser#start()
	 */
	@Override
	public void start() {
		// URLが読み込まれた直後に呼び出されるハンドラを追加
		browser.addProgressListener(new ProgressListener() {
			@Override
			public void completed(ProgressEvent event) {
				onReadyStateComplete();
			}
			@Override
			public void changed(ProgressEvent event) { }
		});
		
		// DOM の準備が出来た時に呼び出される function をブラウザに登録
		new BrowserFunction(browser, READY_FUNCTION_NAME) {
			@Override
			public Object function(Object[] arguments) {
				super.function(arguments);
				
				onReady();
				
				return null;
			}
		};
		
		shell.open();
	}

	/**
	 * 画面をキャプチャします
	 * 
	 * @return 画面イメージ
	 */
	public Image captureAsImage() {
		Browser browser = this.browser;
		
        GC gc = null;
    	Point size = browser.getSize();
        Image image = new Image(display, size.x, size.y);
        try{
        	gc = new GC(image);
        	browser.print(gc);
            return image;
        } finally {
        	if(gc != null) {
        		gc.dispose();
        	}
        }
	}
	
	/**
	 * @see hina.remotebrowser.ui.Browser#capture(java.io.OutputStream)
	 */
	@Override
	public void capture(OutputStream out) {
		Image image = captureAsImage();
		ImageData data = image.getImageData();
		ImageLoader loader = new ImageLoader();
		loader.data = new ImageData[]{ data };
		loader.save(out, SWT.IMAGE_PNG);
	}
	
	/**
	 * @see hina.remotebrowser.ui.Browser#click(int, int)
	 */
	@Override
	public void click(int x, int y) {
		shell.forceActive();
		
		Point oldCursorPos = display.getCursorLocation();
		Point newCursorPos = display.map(browser, null, x, y);
		display.setCursorLocation(newCursorPos);
		
		{ // マウスクリックをシミュレート
			Event event = new Event();
			event.display = display;
			event.widget = browser;
			event.detail = SWT.NONE;
			event.widget = browser;
			event.count = 1;
			event.button = 1;
			
			event.type = SWT.MouseDown;
			display.post(event);
			event.type = SWT.MouseUp;
			display.post(event);
		}
		
		display.setCursorLocation(oldCursorPos);
	}
	
	/**
	 * @see hina.remotebrowser.ui.Browser#refresh()
	 */
	@Override
	public void refresh() {
		browser.refresh();
	}
	
	/**
	 * @see hina.remotebrowser.ui.Browser#dispose()
	 */
	@Override
	public void dispose() {
		if(shell != null) {
			shell.close();
			shell.dispose();
		}
	}
	
	/**
	 * @see hina.remotebrowser.ui.Browser#isDisposed()
	 */
	@Override
	public boolean isDisposed() {
		return shell == null || shell.isDisposed();
	}
	
	/**
	 * document.readyState が complete になったタイミングで呼び出されます
	 */
	protected void onReadyStateComplete() {
		Browser browser = this.browser;

		browser.execute(JQUERY);
	}
	
	/**
	 * $(document).ready() なタイミングで呼び出されます
	 */
	protected abstract void onReady();
}
