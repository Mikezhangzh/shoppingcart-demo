package mikezhang.demo.shoppingcart.error;

public class ApiRuntimeException extends RuntimeException {
    /**
     *  Error msg that can be shown to clients
     */
    protected String externalMassage;

    public ApiRuntimeException(String msg) {
        super(msg);
        this.externalMassage = msg;
    }
    public ApiRuntimeException(String internalMsg, String externalMsg) {
        super(internalMsg);
        this.externalMassage = externalMsg;
    }

    public String getExternalMassage() {
        return externalMassage;
    }
}
