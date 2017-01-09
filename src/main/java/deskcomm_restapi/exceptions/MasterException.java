package deskcomm_restapi.exceptions;

/**
 * Created by Jay Rathod on 07-01-2017.
 */
public class MasterException extends Throwable {
    public String message;

    public MasterException() {
    }

    public MasterException(String message) {
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
