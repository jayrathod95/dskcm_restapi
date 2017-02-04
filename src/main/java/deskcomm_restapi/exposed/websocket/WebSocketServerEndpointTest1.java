package deskcomm_restapi.exposed.websocket;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Jay Rathod on 04-02-2017.
 */
@ServerEndpoint("/wsconn/image/image1")
public class WebSocketServerEndpointTest1 {
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        System.out.println("OnOpen1" + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        System.out.println("OnClose1" + session.getId());
    }

    @OnMessage
    public void onMessage(ByteBuffer byteBuffer, Session session1) {
        System.out.println("OnMessage1" + session1.getId());
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendBinary(byteBuffer);
            } catch (IOException ex) {
                Logger.getLogger(WebSocketServerEndpointTest1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


}
