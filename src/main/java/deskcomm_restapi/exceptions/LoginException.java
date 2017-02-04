package deskcomm_restapi.exceptions;

/**
 * Created by Jay Rathod on 09-01-2017.
 */
public class LoginException extends Throwable {

    private String localisedMessage;
    private String technicalMessage;

    public LoginException(String technicalMessage, String localisedMessage) {
        this.localisedMessage = localisedMessage;
        this.technicalMessage = technicalMessage;
    }

    public LoginException(String technicalMessage) {
        this.technicalMessage = technicalMessage;
    }


    @Override
    public String getMessage() {
        return technicalMessage;
    }

    @Override
    public String getLocalizedMessage() {
        return localisedMessage;
    }
}
