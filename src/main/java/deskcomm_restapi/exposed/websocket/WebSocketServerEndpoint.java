package deskcomm_restapi.exposed.websocket;

import deskcomm_restapi.core.Group;
import deskcomm_restapi.core.Identity;
import deskcomm_restapi.core.Keys;
import deskcomm_restapi.core.User;
import deskcomm_restapi.core.messages.InboundGroupMessage;
import deskcomm_restapi.core.messages.InboundPersonalMessage;
import deskcomm_restapi.core.messages.OutboundPersonalMessage;
import deskcomm_restapi.support.L;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
    //private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());
    private static Set<Session> sessions = new HashSet<>();
    private Object groupCreationSuccessfulResponse;
    private Object groupCreationFailedResponse;

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
                    InboundGroupMessage message = new InboundGroupMessage(webSocketMessage.getData());
                    message.saveToDatabase();
                }
                break;
            case "message/personal":
                if (webSocketMessage.getIdentity().verify()) {
                    InboundPersonalMessage message = new InboundPersonalMessage(webSocketMessage.getData());
                    message.saveToDatabase();
                    message.dispatch();
                }
                break;

            case "group/create":
                if (webSocketMessage.getIdentity().verify()) {
                    JSONObject data = webSocketMessage.getData();
                    String groupId = data.getString(Keys.GROUP_ID);
                    String groupName = data.getString(Keys.GROUP_NAME);
                    String createdBy = data.getString(Keys.GROUP_CREATED_BY);
                    String iconUrl = data.getString(Keys.GROUP_ICON_URL);
                    JSONArray groupMembers = data.getJSONArray(Keys.GROUP_MEMBER_IDS);
                    Group group = new Group(groupId, groupName, createdBy, iconUrl, groupMembers);
                    if (group.saveToDatabase()) {
                        OutboundWebSocketMessage groupCreationSuccessfulResponse = getGroupCreationSuccessfulResponse(group);
                        session.getAsyncRemote().sendText(groupCreationSuccessfulResponse.toString());
                    } else {
                        OutboundWebSocketMessage groupCreationFailedResponse = getGroupCreationFailedResponse(group);
                        session.getAsyncRemote().sendText(groupCreationFailedResponse.toString());
                    }
                }
                break;
            case "request/users":
                if (webSocketMessage.getIdentity().verify()) {
                    try {
                        JSONArray allUsers = User.getAllUsers();
                        JSONObject temp = new JSONObject();
                        temp.put("users", allUsers);
                        OutboundWebSocketMessage webSocketMessage1 = new OutboundWebSocketMessage("bookkeeping/users", temp);
                        webSocketMessage1.setTo(new User(webSocketMessage.getIdentity().getUuid()));
                        webSocketMessage1.dispatch();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "messages/get_undelivered_messages":
                try {
                    if (webSocketMessage.getIdentity().verify()) {
                        User user = webSocketMessage.getIdentity().getUser();
                        if (user.isOnline()) {
                            List<OutboundPersonalMessage> undeliveredMessages = user.getUndeliveredPersonalMessages();
                            Iterator<OutboundPersonalMessage> iterator = undeliveredMessages.iterator();
                            Session session1 = WebSocketServerEndpoint.getSessionById(user.getWsSessionId());
                            if (session1 != null)
                                while (iterator.hasNext()) {
                                    OutboundWebSocketMessage message = new OutboundWebSocketMessage("message/personal", iterator.next().toJSON());
                                    session1.getAsyncRemote().sendText(message.toString());
                                }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
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

    public static Session getSessionById(String wsSessionId) {
        Iterator<Session> iterator = sessions.iterator();
        while (iterator.hasNext()) {
            Session session = iterator.next();
            String id = session.getId();
            if (id.equals(wsSessionId)) {
                return session;
            }
        }
        return null;
    }


    public OutboundWebSocketMessage getGroupCreationSuccessfulResponse(Group group) {
        OutboundWebSocketMessage outboundWebSocketMessage = new OutboundWebSocketMessage();
        outboundWebSocketMessage.setPath("group/create/result");
        JSONObject temp = new JSONObject();
        temp.put(Keys.GROUP_ID, group.getUuid());
        temp.put(Keys.JSON_RESULT, true);
        outboundWebSocketMessage.setData(temp);
        return outboundWebSocketMessage;
    }

    public OutboundWebSocketMessage getGroupCreationFailedResponse(Group group) {
        OutboundWebSocketMessage outboundWebSocketMessage = new OutboundWebSocketMessage();
        outboundWebSocketMessage.setPath("group/create/result");
        JSONObject temp = new JSONObject();
        temp.put(Keys.GROUP_ID, group.getUuid());
        temp.put(Keys.JSON_RESULT, false);
        outboundWebSocketMessage.setData(temp);
        return outboundWebSocketMessage;
    }
}
