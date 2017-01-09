package deskcomm_restapi.support;

import deskcomm_restapi.core.Keys;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * Created by Jay Rathod on 09-01-2017.
 */
public class Response {
    private JsonObject jsonObject;

    public Response(boolean result, int errorType, String message) {
        jsonObject = Json.createObjectBuilder().add(Keys.JSON_RESULT, result).add(Keys.JSON_ERROR_TYPE, errorType).add(Keys.JSON_MESSAGE, message).build();
    }

    public String toString() {
        return jsonObject.toString();
    }
}
