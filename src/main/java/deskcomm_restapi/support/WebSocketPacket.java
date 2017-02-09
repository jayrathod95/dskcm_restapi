package deskcomm_restapi.support;

import deskcomm_restapi.core.Keys;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jay Rathod on 05-02-2017.
 */
public class WebSocketPacket {
    private String path;
    private JSONObject body;

    public WebSocketPacket() {
    }

    public WebSocketPacket(JSONObject jsonObject) throws JSONException {
        path = jsonObject.getString(Keys.WS_PATH);
        body = jsonObject.getJSONObject(Keys.WS_BODY);
    }

    public WebSocketPacket(String s) throws JSONException {
        new WebSocketPacket(new JSONObject(s));
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public JSONObject getBody() {
        return body;
    }

    public void setBody(JSONObject body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return toJSONObject().toString();

    }

    private JSONObject toJSONObject() {
        return new JSONObject().put(Keys.WS_PATH,path).put(Keys.WS_BODY,body);
    }
}
