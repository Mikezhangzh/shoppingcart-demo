package mikezhang.demo.shoppingcart;

import java.time.LocalDateTime;

public class BasicApiResponse {
	private final boolean success;
	private final String message;

	public BasicApiResponse(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getMessage() {
		return message;
	}
	
	public String getTimestamp() {
		return LocalDateTime.now().toString();
	}
}
