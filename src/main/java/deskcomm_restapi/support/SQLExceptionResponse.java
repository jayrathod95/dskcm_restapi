package deskcomm_restapi.support;

import org.json.JSONObject;

/**
 * Created by Jay Rathod on 04-02-2017.
 */
public class SQLExceptionResponse extends Response1<JSONObject> {
    public SQLExceptionResponse(String message) {
        super(false, ErrorType.ERROR_KNOWN, ErrorCode.SQL, message, null);
    }
}
