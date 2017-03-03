package deskcomm_restapi.exposed.websocket;

import deskcomm_restapi.core.*;
import deskcomm_restapi.core.messages.InboundGroupMessage;
import deskcomm_restapi.core.messages.InboundPersonalMessage;
import deskcomm_restapi.core.messages.OutboundPersonalMessage;
import deskcomm_restapi.dbconn.DbConnection;
import deskcomm_restapi.support.L;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Jay Rathod on 04-02-2017.
 */
@SuppressWarnings("ALL")
@ServerEndpoint(
        value = "/ws"
//        decoders = {TextDecoder.class, FileDecoder.class}
)

public class WebSocketServerEndpoint {


    private static ObservableMap<String, Session> observableSessionsMapId = FXCollections.observableHashMap();
    private static ObservableMap<String, Session> observableSessionsMapUserId = FXCollections.observableHashMap();

    static {
        observableSessionsMapUserId.addListener(new MapChangeListener<String, Session>() {
            @Override
            public void onChanged(Change<? extends String, ? extends Session> change) {
                pushOnlineUsers();
            }
        });
        observableSessionsMapUserId.removeListener(new MapChangeListener<String, Session>() {
            @Override
            public void onChanged(Change<? extends String, ? extends Session> change) {
                pushOnlineUsers();
            }
        });
    }

    // TODO: 3/2/2017
    private Object groupCreationSuccessfulResponse;
    private Object groupCreationFailedResponse;

    private static void pushOnlineUsers() {
        JSONArray array = new JSONArray(observableSessionsMapUserId.keySet());
        observableSessionsMapUserId.forEach((s, session) -> session.getAsyncRemote().sendText(new OutboundWebSocketMessage("users/online", new JSONObject().put("data", array)).toString()));
    }

    public static Session getSessionBySessionId(String wsSessionId) {

        return observableSessionsMapId.get(wsSessionId);
    }

    public static void pushEvents() {
        List<Event> all = Event.getAll();
        JSONArray array = new JSONArray();
        for (Event event : all) {
            JSONObject eventJsonObj = event.toJSON();
            eventJsonObj.put(Keys.INTERESTED_USERS_COUNT, event.getInterestedUsersCount());
            array.put(eventJsonObj);
        }
        observableSessionsMapUserId.forEach((s, session) -> session.getAsyncRemote().sendText(new OutboundWebSocketMessage("event/get/all", new JSONObject().put("data", array)).toString()));
    }

    public static void pushEvent(String eventId) {
        Event event = new Event(eventId);
        event.fetchFromDb();
        observableSessionsMapUserId.forEach((s, session) -> session.getAsyncRemote().sendText(new OutboundWebSocketMessage("event/new", event.toJSON().put(Keys.INTERESTED_USERS_COUNT, event.getInterestedUsersCount())).toString()));
    }

    @OnOpen
    public void onOpen(Session session) {
        observableSessionsMapId.put(session.getId(), session);
        System.out.println("OnOpen" + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        observableSessionsMapId.remove(session.getId());
        Iterator<Session> sessionIterator = observableSessionsMapUserId.values().iterator();
        Iterator<String> userIdIterator = observableSessionsMapUserId.keySet().iterator();
        while (sessionIterator.hasNext()) {
            if (sessionIterator.next().getId().equals(session.getId())) {
                observableSessionsMapUserId.remove(userIdIterator.next());
                break;
            }
            userIdIterator.next();
        }
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
                        OutboundWebSocketMessage webSocketMessage1 = new OutboundWebSocketMessage("bookkeeping/users", temp, webSocketMessage.getIdentity().getUser());
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
                            Session session1 = WebSocketServerEndpoint.getSessionBySessionId(user.getWsSessionId());
                            if (session1 != null)
                                while (iterator.hasNext()) {
                                    OutboundWebSocketMessage message = new OutboundWebSocketMessage("message/personal", iterator.next().toJSON(), user);
                                    session1.getAsyncRemote().sendText(message.toString());
                                }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;

            case "event/create":
                if (webSocketMessage.getIdentity().verify()) {
                    Event event = new Event(webSocketMessage.getData());
                    try {
                        event.saveToDatabase();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case "event/get/all":
                if (webSocketMessage.getIdentity().verify()) {
                    List<Event> all = Event.getAll();
                    JSONArray array = new JSONArray();
                    for (Event event : all) {
                        JSONObject eventJsonObj = event.toJSON();
                        eventJsonObj.put(Keys.INTERESTED_USERS_COUNT, event.getInterestedUsersCount());
                        array.put(eventJsonObj);
                    }
                    OutboundWebSocketMessage webSocketMessage1 = new OutboundWebSocketMessage("event/get/all", new JSONObject().put("data", array), webSocketMessage.getIdentity().getUser());
                    webSocketMessage1.send(session);
                }
                break;

            case "event/get/int_users":
                if (webSocketMessage.getIdentity().verify())
                    new OutboundWebSocketMessage("event/int_users", new JSONObject().put("data", new Event(webSocketMessage.getData().getString(Keys.EVENT_ID)).getInterestedUsers())).send(session);
                break;

            case "message/personal/received":
                processMessageAcknowlegent(webSocketMessage.getData().getString("id"));
                break;
            case "users/online":
                pushOnlineUsers(session);
                break;
            case "event/interest/0":
                if (webSocketMessage.getIdentity().verify()) {
                    Event event = new Event(webSocketMessage.getData().getString(Keys.EVENT_ID));
                    event.setInterested(webSocketMessage.getIdentity().getUser(), false);
                    pushInterestedUsersCount(event);
                }
                break;
            case "event/interest/1":
                if (webSocketMessage.getIdentity().verify()) {
                    Event event = new Event(webSocketMessage.getData().getString(Keys.EVENT_ID));
                    event.setInterested(webSocketMessage.getIdentity().getUser(), true);
                    pushInterestedUsersCount(event);
                }
                break;
        }
    }

    private void pushInterestedUsersCount(Event event) {
        JSONObject object = new JSONObject().put(Keys.EVENT_ID, event.getUuid())
                .put(Keys.INTERESTED_USERS_COUNT, event.getInterestedUsersCount());
        observableSessionsMapUserId.forEach((s, session) -> session.getAsyncRemote().sendText(new OutboundWebSocketMessage("int_users_count", object).toString()));
    }

    private void pushOnlineUsers(Session session) {
        Iterator<String> iterator = observableSessionsMapId.keySet().iterator();
        JSONArray array = new JSONArray();
        try {
            while (iterator.hasNext()) {
                array.put(User.getUserWithWsSessionId(iterator.next()).getUuid());
            }
            OutboundWebSocketMessage webSocketMessage = new OutboundWebSocketMessage("users/online", new JSONObject().put("data", array));
            webSocketMessage.send(session);
        } catch (User.NoUserFoundException e) {
            e.printStackTrace();
        }
    }

    public void processMessageAcknowlegent(String id) {
        try {
            Connection connection = DbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE  FROM users_messages  WHERE `_uuid`=?");

            statement.setString(1, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleHandShakeMessage(Identity identity, Session session) {
        if (identity.verify()) {
            User user = identity.getUser();
            observableSessionsMapUserId.put(user.getUuid(), session);
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
