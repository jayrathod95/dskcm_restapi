package deskcomm_restapi.exposed.websocket;

import deskcomm_restapi.core.Keys;
import deskcomm_restapi.core.User;
import deskcomm_restapi.support.L;
import org.json.JSONObject;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Jay Rathod on 04-02-2017.
 */
@ServerEndpoint(
        value = "/ws"
//        decoders = {TextDecoder.class, FileDecoder.class}
)

public class WebSocketServerEndpointTest {
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        System.out.println("OnOpen" + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("OnClose" + session.getId());
        sessions.remove(session);


    }

    @OnMessage
    public void onMessage(String s, Session session) {
        L.P("OnMessage: WebSocketPacket : ", session.getId(), s);
        WebSocketMessage webSocketMessage = new WebSocketMessage(s);
        switch (webSocketMessage.getPath()) {
            case "request/" + Keys.HANDSHAKE_REQ:
                handleHandShakeMessage(webSocketMessage.getData(), session);
                break;
        }

    }

    private void handleHandShakeMessage(JSONObject data, Session session) {
        try {
            String uuid = data.getString(Keys.USER_UUID);
            String sessionId = data.getString(Keys.SESSION_ID);
            boolean b = User.verifySession(uuid, sessionId);
            WebSocketMessage webSocketMessage = new WebSocketMessage();
            webSocketMessage.setPath("response/" + Keys.HANDSHAKE_REQ);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Keys.JSON_RESULT, b);
            webSocketMessage.setData(jsonObject);
            session.getAsyncRemote().sendText(webSocketMessage.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @OnMessage
    public void onMessage(ByteBuffer byteBuffer, Session session1) {
        System.out.println("OnMessage" + session1.getId());

        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendBinary(byteBuffer);
            } catch (IOException ex) {
                Logger.getLogger(WebSocketServerEndpointTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
