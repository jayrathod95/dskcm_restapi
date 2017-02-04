package deskcomm_restapi.support;

import org.json.JSONObject;

/**
 * Created by Jay Rathod on 04-02-2017.
 */
public class IncorrectPasswordResponse extends Response1<JSONObject> {
    public IncorrectPasswordResponse() {
        super(false, ErrorType.ERROR_KNOWN, ErrorCode.INCORRECT_PASSWORD, ErrorMessage.INCORRECT_PASSWORD, null);
    }
}
