package deskcomm_restapi.exposed.websocket;

import deskcomm_restapi.core.Identity;
import deskcomm_restapi.core.Keys;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Jay Rathod on 07-02-2017.
 */
public class InboundWebSocketMessage {

    private JSONObject jsonObject;
    private String path;
    private Identity identity;
    private JSONObject data;

    private Map<Integer, String> pathDecomposed;

    public InboundWebSocketMessage(String wholeMessageJsonString) {
        jsonObject = new JSONObject(wholeMessageJsonString);
        path = jsonObject.getString(Keys.PATH);
        if (jsonObject.has(Keys.WS_IDENTITY)) {
            JSONObject identityJSON = jsonObject.getJSONObject(Keys.WS_IDENTITY);
            identity = new Identity(identityJSON);
        }
        if (jsonObject.has(Keys.DATA))
            data = jsonObject.getJSONObject(Keys.DATA);
    }

    public InboundWebSocketMessage() {
    }


    public JSONObject toJsonObject() {
        return jsonObject;
    }

    public String getPath() {
        return path;
    }

    public JSONObject getData() {
        return data;
    }


    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Keys.PATH, path);
        jsonObject.put(Keys.DATA, data);
        return jsonObject.toString();
    }

    public Identity getIdentity() {
        return identity;
    }
}
