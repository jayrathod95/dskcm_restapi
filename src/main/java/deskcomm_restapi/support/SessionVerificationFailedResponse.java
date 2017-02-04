package deskcomm_restapi.support;

import org.json.JSONObject;

/**
 * Created by Jay Rathod on 04-02-2017.
 */
public class SessionVerificationFailedResponse extends Response1<JSONObject> {
    public SessionVerificationFailedResponse() {
        super(false, ErrorType.ERROR_KNOWN, ErrorCode.SESSION_VER_FAIL, ErrorMessage.SESSION_VER_FAIL, null);
    }
}
