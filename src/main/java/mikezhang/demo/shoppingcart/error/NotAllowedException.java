package mikezhang.demo.shoppingcart.error;

public class NotAllowedException extends ApiRuntimeException {

    public NotAllowedException(String msg) {
        super(msg);
    }

    public NotAllowedException(String internalMsg, String externalMsg) {
        super(internalMsg, externalMsg);
    }
}
