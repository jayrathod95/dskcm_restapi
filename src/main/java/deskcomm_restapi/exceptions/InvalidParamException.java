package deskcomm_restapi.exceptions;

/**
 * Created by Jay Rathod on 07-01-2017.
 */
public class InvalidParamException extends Throwable {
    String paramName;

    public InvalidParamException(String paramName) {
        this.paramName = paramName;
    }

    @Override
    public String getMessage() {
        return "Invalid Parameter: " + paramName;
    }
}

