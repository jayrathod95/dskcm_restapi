package deskcomm_restapi.exposed.websocket;

import deskcomm_restapi.core.Keys;
import deskcomm_restapi.support.PathDecoder;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Jay Rathod on 07-02-2017.
 */
public class WebSocketMessage {

    private JSONObject jsonObject;
    private String path;
    private JSONObject data;
    private Map<Integer, String> pathDecomposed;

    public WebSocketMessage(String wholeMessageJsonString) {
        jsonObject = new JSONObject(wholeMessageJsonString);
        path = jsonObject.getString(Keys.PATH);
        data = jsonObject.getJSONObject(Keys.DATA);
    }

    public WebSocketMessage() {
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

    public Map<Integer, String> getPathDecomposed() {
        return PathDecoder.getPathParams(path);
    }

    public void setPath(String path) {
        this.path = path;
    }


    public void setData(JSONObject data) {
        this.data = data;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Keys.PATH, path);
        jsonObject.put(Keys.DATA, data);
        return jsonObject.toString();
    }
}
