package hina.remotebrowser.server;

/**
 * 400 Bad Request をクライアントに返す為の例外
 * 
 * @author yohei_hina
 */
public class BadRequestException extends RuntimeException {
	/** serialVersionUID */
	private static final long serialVersionUID = -3690385434954349095L;

	/**
	 * コンストラクタ
	 */
	public BadRequestException() {
	}
	
	/**
	 * コンストラクタ
	 * 
	 * @param message メッセージ
	 */
	public BadRequestException(String message) {
		super(message);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param cause 元となる例外
	 */
	public BadRequestException(Throwable cause) {
		super(cause);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param message メッセージ
	 * @param cause 元となる例外
	 */
	public BadRequestException(String message, Throwable cause) {
		super(message, cause);
	}
}