package deskcomm_restapi.exposed.websocket;

import deskcomm_restapi.core.Keys;
import org.json.JSONObject;

import javax.websocket.Session;

/**
 * Created by Jay Rathod on 10-02-2017.
 */
public class OutboundWebSocketMessage {
    private String path;
    private JSONObject data;

    public OutboundWebSocketMessage(String path, JSONObject data) {
        this.path = path;
        this.data = data;
    }

    public OutboundWebSocketMessage() {

    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public JSONObject toJSON() {
        return new JSONObject().put(Keys.WS_PATH, path).put(Keys.WS_DATA, data);
    }

    @Override
    public String toString() {
        return toJSON().toString();
    }

    public void send(Session session) {
        session.getAsyncRemote().sendText(toString());
    }
}
