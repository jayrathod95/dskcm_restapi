package deskcomm_restapi.exposed.websocket;

import deskcomm_restapi.core.Keys;
import deskcomm_restapi.core.User;
import org.json.JSONObject;

import javax.websocket.Session;

/**
 * Created by Jay Rathod on 10-02-2017.
 */
public class OutboundWebSocketMessage {
    private String path;
    private JSONObject data;
    private User user;
    private User toUser;

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

    public void dispatch(Session session) {

    }

    public void dispatch() {
        if (toUser.isOnline()) {
            Session session = WebSocketServerEndpoint.getSessionById(toUser.getWsSessionId());
            if (session != null)
                session.getAsyncRemote().sendText(this.toString());
        }
    }

    public void setTo(User toUser) {
        this.toUser = toUser;

    }
}
