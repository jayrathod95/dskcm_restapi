package deskcomm_restapi.exposed.websocket;

import deskcomm_restapi.core.Identity;
import deskcomm_restapi.core.Keys;
import deskcomm_restapi.core.User;
import deskcomm_restapi.core.messages.GroupMessage;
import deskcomm_restapi.core.messages.PersonalMessage;
import deskcomm_restapi.support.L;
import org.json.JSONObject;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jay Rathod on 04-02-2017.
 */
@SuppressWarnings("ALL")
@ServerEndpoint(
        value = "/ws"
//        decoders = {TextDecoder.class, FileDecoder.class}
)

public class WebSocketServerEndpoint {
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
        User.updateStatusAsOffline(session.getId());
    }

    @OnMessage
    public void onMessage(String s, Session session) {
        L.P("OnMessage: WebSocketPacket : ", session.getId(), s);
        InboundWebSocketMessage webSocketMessage = new InboundWebSocketMessage(s);
        switch (webSocketMessage.getPath()) {
            case "request/" + Keys.HANDSHAKE_REQ:
                handleHandShakeMessage(webSocketMessage.getIdentity(), session);
                break;

            case "message/group":
                if (webSocketMessage.getIdentity().verify()) {
                    GroupMessage message = new GroupMessage(webSocketMessage.getData());
                    message.saveToDatabase();
                }
                break;
            case "message/personal":
                if (webSocketMessage.getIdentity().verify()) {
                    PersonalMessage message = new PersonalMessage(webSocketMessage.getData());
                    message.saveToDatabase();
                }
                break;
            case "test":
                System.out.println("Test: " + webSocketMessage.getData());
        }

    }

    private void handleHandShakeMessage(Identity identity, Session session) {
        if (identity.verify()) {
            User user = identity.getUser();
            boolean b = user.updateStatusAsOnline(session.getId());
            OutboundWebSocketMessage webSocketMessage = new OutboundWebSocketMessage();
            webSocketMessage.setPath("response/" + Keys.HANDSHAKE_REQ);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Keys.JSON_RESULT, b);
            webSocketMessage.setData(jsonObject);
            session.getAsyncRemote().sendText(webSocketMessage.toString());
        }
    }


    @OnMessage
    public void onMessage(ByteBuffer byteBuffer, Session session1) {
        System.out.println("OnMessage" + session1.getId());
/*
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendBinary(byteBuffer);
            } catch (IOException ex) {
                Logger.getLogger(WebSocketServerEndpoint.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        */
    }


}
