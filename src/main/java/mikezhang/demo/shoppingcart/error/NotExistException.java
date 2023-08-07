package mikezhang.demo.shoppingcart.error;

public class NotExistException extends ApiRuntimeException {


    public NotExistException(String msg) {
        super(msg);
    }

    public NotExistException(String internalMsg, String externalMsg) {
        super(internalMsg, externalMsg);
    }
}
