package hina.remotebrowser.server;

/**
 * 503 Service Unavailable をクライアントに返す為の例外
 * 
 * @author yohei_hina
 */
public class ServiceUnavailableException extends RuntimeException {
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 */
	public ServiceUnavailableException() {
		super();
	}

	/**
	 * コンストラクタ
	 * 
	 * @param cause 元となる例外
	 */
	public ServiceUnavailableException(Throwable cause) {
		super(cause);
	}
}
