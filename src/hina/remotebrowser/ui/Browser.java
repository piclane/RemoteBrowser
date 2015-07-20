package hina.remotebrowser.ui;

import java.io.OutputStream;


public interface Browser {
	/**
	 * UI を開始します
	 */
	void start();
	
	/**
	 * 画像をキャプチャします
	 * 
	 * @param out 出力先
	 */
	void capture(OutputStream out);
	
	void click(int x, int y);
	
	/**
	 * ブラウザを再読込します
	 */
	void refresh();
	
	/**
	 * UI を破棄します
	 */
	void dispose();
	
	/**
	 * 既に UI が破棄されたかどうかを返します
	 * 
	 * @return 既に UI が破棄されている時 true
	 */
	boolean isDisposed();
}
