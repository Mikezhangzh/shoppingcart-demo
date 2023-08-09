package mikezhang.demo.shoppingcart.error;

import mikezhang.demo.shoppingcart.BasicApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {
	
	@ExceptionHandler(NotExistException.class)
	public ResponseEntity<BasicApiResponse> notExist(NotExistException ex){
		ex.printStackTrace();
		return new ResponseEntity<BasicApiResponse>(new BasicApiResponse(false, "Resource not found - " + ex.getExternalMassage()), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(NotAllowedException.class)
	public ResponseEntity<BasicApiResponse> notAllowed(NotAllowedException ex){
		ex.printStackTrace();
		return new ResponseEntity<BasicApiResponse>(new BasicApiResponse(false, "Not allowed: " + ex.getExternalMassage()), HttpStatus.METHOD_NOT_ALLOWED);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<BasicApiResponse> unhandledExceptions(MethodArgumentNotValidException ex){
		ex.printStackTrace();
		return new ResponseEntity<BasicApiResponse>(new BasicApiResponse(false, "invalid request"), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<BasicApiResponse> unhandledExceptions(Exception ex){
		ex.printStackTrace();
		return new ResponseEntity<BasicApiResponse>(new BasicApiResponse(false, "Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
