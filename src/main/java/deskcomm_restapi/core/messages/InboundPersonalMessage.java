package deskcomm_restapi.core.messages;

import com.sun.istack.internal.Nullable;
import deskcomm_restapi.core.Keys;
import deskcomm_restapi.core.User;
import deskcomm_restapi.dbconn.DbConnection;
import deskcomm_restapi.exposed.websocket.OutboundWebSocketMessage;
import deskcomm_restapi.exposed.websocket.WebSocketServerEndpoint;
import org.json.JSONObject;

import javax.websocket.Session;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import static deskcomm_restapi.core.Keys.*;

/**
 * Created by Jay Rathod on 10-02-2017.
 */
public class InboundPersonalMessage extends Message {

    @Nullable //This will have value only after saveToDatabase() is called
    private Timestamp serverTimeStamp;

    public InboundPersonalMessage(String messageId, String fromUuid, String toUuid, String body) {
        super(messageId, fromUuid, toUuid, body);
    }

    public InboundPersonalMessage(JSONObject jsonObject) {
        super(jsonObject.getString(MESSAGE_ID), jsonObject.getString(MESSAGE_FROM), jsonObject.getString(MESSAGE_TO), jsonObject.getString(MESSAGE_BODY));
    }

    @Override
    public boolean saveToDatabase() {
        try {
            Connection connection = DbConnection.getConnection();
            connection.setAutoCommit(false);
            serverTimeStamp = new Timestamp(new Date().getTime());
            PreparedStatement statement = connection.prepareStatement("INSERT INTO message_data(_uuid,data,created) VALUES(?,?,?) ");
            String randomId = UUID.randomUUID().toString() + UUID.randomUUID().toString();
            statement.setString(1, randomId);
            statement.setString(2, this.body);
            statement.setTimestamp(3, serverTimeStamp);
            statement.executeUpdate();
            int updateCount = statement.getUpdateCount();
            statement.close();
            if (updateCount > 0) {
                PreparedStatement statement1 = connection.prepareStatement("INSERT INTO users_messages(`_uuid`,data_id,`_from`,`_to`,created) VALUES(?,?,?,?,?)");
                statement1.setString(1, messageId);
                statement1.setString(2, randomId);
                statement1.setString(3, this.fromUuid);
                statement1.setString(4, this.toUuid);
                statement1.setTimestamp(5, serverTimeStamp);
                statement1.executeUpdate();
                int updateCount1 = statement1.getUpdateCount();
                if (updateCount1 > 0) {
                    connection.commit();
                    statement1.close();
                    connection.close();
                    return true;
                } else {
                    connection.rollback();
                    statement1.close();
                    connection.close();
                }
            } else {
                connection.rollback();
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public JSONObject toJSON() {
        return new JSONObject()
                .put(Keys.MESSAGE_ID, messageId)
                .put(Keys.MESSAGE_FROM, fromUuid)
                .put(Keys.MESSAGE_TO, toUuid)
                .put(Keys.MESSAGE_BODY, body)
                .put(Keys.SERVER_TIMESTAMP, serverTimeStamp.toString());
    }


    @Override
    public void dispatch() {

        OutboundWebSocketMessage message = new OutboundWebSocketMessage("message/personal", toJSON());


        User toUser = new User(toUuid);
        if (toUser.isOnline()) {
            Session session = WebSocketServerEndpoint.getSessionById(toUser.getWsSessionId());
            if (session != null)
                session.getAsyncRemote().sendText(message.toString());
        }
    }
}
