package limeng32.web.core.exception;

public class WebCoreException extends Exception {

	private static final long serialVersionUID = 1L;

	public WebCoreException(String message) {
		super(message);
	}

	public WebCoreException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
