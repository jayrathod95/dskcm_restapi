package deskcomm_restapi.exceptions;

/**
 * Created by Jay Rathod on 03-02-2017.
 */
public class OperationFailedException extends Throwable {
    private String message;

    public OperationFailedException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
