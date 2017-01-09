package deskcomm_restapi.exceptions;

/**
 * Created by Jay Rathod on 09-01-2017.
 */
public class LoginException extends Throwable {
    @Override
    public String getMessage() {
        return "Invalid Email/Password";
    }
}
