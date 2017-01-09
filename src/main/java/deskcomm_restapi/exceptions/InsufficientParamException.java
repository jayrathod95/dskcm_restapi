package deskcomm_restapi.exceptions;

/**
 * Created by Jay Rathod on 07-01-2017.
 */
public class InsufficientParamException extends Throwable {
    private String message;

    public InsufficientParamException(){

    }
    public InsufficientParamException(String message){
        this.message=message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
