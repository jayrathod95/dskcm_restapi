package deskcomm_restapi.core;

import com.sun.istack.internal.Nullable;
import com.sun.org.glassfish.gmbal.Description;
import deskcomm_restapi.core.messages.OutboundPersonalMessage;
import deskcomm_restapi.dbconn.DbConnection;
import deskcomm_restapi.exceptions.LoginException;
import deskcomm_restapi.exceptions.OperationFailedException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Jay Rathod on 03-01-2017.
 */
public class User {
    private String uuid;
    private String firstname;
    private String lastname;
    private String email;
    private String mobile;
    private String password;
    private String image_url;
    private String uid;
    private String sessionId;
    private String gender;
    private String created;
    private JSONObject jsonObject;
    private String status;
    private String wsSessionId;
    private String statusAsOnline;
    private Object unreadPersonalMessages;


    public User(@Nullable String uuid) {
        this.uuid = uuid;
    }


    public boolean changePassword(String oldPassword, String newPassword) throws SQLException {
        Connection connection = DbConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement("UPDATE users SET password=? WHERE `_uuid`=? AND password=?");
        statement.setString(1, newPassword);
        statement.setString(2, uuid);
        statement.setString(3, oldPassword);
        boolean b = statement.executeUpdate() > 0;
        statement.close();
        connection.close();
        return b;
    }

    public User(String uuid, String sessionId) {
        this.uuid = uuid;
        this.sessionId = sessionId;
    }


    public static UserBuilder getUserBuilder() {
        return new UserBuilder();
    }

    public static User login(String email, String password) throws SQLException, LoginException {
        Connection connection = DbConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT _uuid,fname,lname,gender,email,mobile,password,img_url,created,_uid FROM users WHERE email=? AND password=?");
        statement.setString(1, email);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            String sessionId = UUID.randomUUID().toString();
            User user = new User(null);
            user.setUuid(resultSet.getString(Keys.USER_UUID));
            user.setFirstname(resultSet.getString(Keys.USER_FIRSTNAME));
            user.setLastname(resultSet.getString(Keys.USER_LASTNAME));
            user.setEmail(resultSet.getString(Keys.USER_EMAIL));
            user.setMobile(resultSet.getString(Keys.USER_MOBILE));
            user.setPassword(resultSet.getString(Keys.USER_PASSWORD));
            user.setImage_url(resultSet.getString(Keys.USER_IMG_URL));
            user.setCreated(resultSet.getString(Keys.USER_CREATED));
            user.setUid(resultSet.getString(Keys.USER_UID));
            user.setGender(resultSet.getString(Keys.GENDER));
            if (user.updateSessionId(sessionId)) {
                user.setSessionId(sessionId);
                return user;
            } else {
                throw new LoginException("Could not update table", "Something Went Wrong");
            }
        } else {
            throw new LoginException("Invalid Username/Password provided");
        }
    }

    private boolean updateSessionId(String sessionId) throws SQLException {
        Connection connection = DbConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement("UPDATE users SET session_id=? WHERE `_uuid`=?");
        statement.setString(1, sessionId);
        statement.setString(2, this.uuid);
        int i = statement.executeUpdate();
        statement.close();
        connection.close();
        return i > 0;
    }

    @Override
    @Description("Outputs JSONObject String")
    public String toString() {
        return toJSONObject().toString();
    }

    public JSONObject toJSONObject() {

        JSONObject jsonObject = new JSONObject().accumulate(Keys.USER_UUID, uuid)
                .accumulate(Keys.USER_FIRSTNAME, firstname)
                .accumulate(Keys.USER_LASTNAME, lastname)
                .accumulate(Keys.GENDER, gender)
                .accumulate(Keys.USER_EMAIL, email)
                .accumulate(Keys.USER_MOBILE, mobile)
                .accumulate(Keys.USER_IMG_URL, image_url)
                .accumulate(Keys.USER_CREATED, created)
                .accumulate(Keys.USER_UID, uid)
                .accumulate(Keys.JSON.SESSION_ID, sessionId);
        this.jsonObject = jsonObject;
        return jsonObject;
    }

    public static JSONArray getAllUsers() throws SQLException {
        Connection connection = DbConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(
                "SELECT `_uuid`,fname,lname,gender,email,img_url FROM users");
        ResultSet resultSet = statement.executeQuery();
        JSONArray jsonArray = new JSONArray();
        while (resultSet.next()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate(Keys.USER_UUID, resultSet.getString(Keys.USER_UUID));
            jsonObject.accumulate(Keys.USER_FIRSTNAME, resultSet.getString(Keys.USER_FIRSTNAME));
            jsonObject.accumulate(Keys.USER_LASTNAME, resultSet.getString(Keys.USER_LASTNAME));
            jsonObject.accumulate(Keys.GENDER, resultSet.getString(Keys.GENDER));
            jsonObject.accumulate(Keys.USER_EMAIL, resultSet.getString(Keys.USER_EMAIL));
            jsonObject.accumulate(Keys.USER_IMG_URL, resultSet.getString(Keys.USER_IMG_URL));
            jsonObject.accumulate(Keys.USER_MOBILE, "");
            jsonArray.put(jsonObject);
        }
        resultSet.close();
        statement.close();
        //System.out.print(arrayBuilder);
        return jsonArray;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public boolean updateEmail(String uuid, String password, String newEmail) throws SQLException {
        Connection connection = DbConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement("UPDATE users SET email=? WHERE `_uuid`=? AND password=?");
        statement.setString(1, newEmail);
        statement.setString(2, uuid);
        statement.setString(3, password);
        return statement.executeUpdate() > 0;
    }


    public String getSessionId() {
        return sessionId;
    }

    protected void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public static boolean verifySession(String uuid, String sessionId) throws SQLException {
        Connection connection = DbConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT count(1) AS result FROM users WHERE `_uuid`=? AND session_id=?");
        statement.setString(1, uuid);
        statement.setString(2, sessionId);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        boolean result = resultSet.getInt(1) > 0;
        statement.close();
        connection.close();
        return result;
    }

    public boolean fetch() throws SQLException {
        Connection connection = DbConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT fname,lname,email,mobile,img_url,created FROM users WHERE `_uuid`=?");
        statement.setString(1, uuid);
        ResultSet resultSet = statement.executeQuery();
        boolean next = resultSet.next();
        if (next) {
            this.firstname = resultSet.getString(Keys.USER_FIRSTNAME);
            this.lastname = resultSet.getString(Keys.USER_LASTNAME);
            this.email = resultSet.getString(Keys.USER_EMAIL);
            this.mobile = resultSet.getString(Keys.USER_MOBILE);
            this.image_url = resultSet.getString(Keys.USER_IMG_URL);
            this.gender = resultSet.getString(Keys.GENDER);
        }
        resultSet.close();
        statement.close();
        connection.close();
        return next;
    }

    public void updateImageURL(String newImageUrl) throws SQLException, OperationFailedException {
        Connection connection = DbConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement("UPDATE users SET img_url=? WHERE `_uuid`=? AND session_id=?");
        statement.setString(1, newImageUrl);
        statement.setString(2, uuid);
        statement.setString(3, sessionId);
        int i = statement.executeUpdate();
        statement.close();
        connection.close();
        if (i <= 0) {
            if (sessionId == null)
                throw new OperationFailedException("Session Id not provided");
            else
                throw new OperationFailedException("Unable to update image url");
        }
    }


    public boolean isOnline() {
        boolean b = false;
        try {
            Connection connection = DbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT status,ws_session_id FROM user_status WHERE user_id=?");
            statement.setString(1, uuid);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                b = resultSet.getString(1).equals("1");
                this.wsSessionId = resultSet.getString(2);
            }
            resultSet.close();
            statement.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return b;
    }


    public String getWsSessionId() {
        return wsSessionId;
    }

    public void setWsSessionId(String wsSessionId) {
        this.wsSessionId = wsSessionId;
    }

    public boolean updateStatusAsOnline(String wsSessionId) {
        this.wsSessionId = wsSessionId;
        Connection connection = null;
        try {
            connection = DbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE user_status SET status='1',ws_session_id=? WHERE user_id=?");
            statement.setString(1, wsSessionId);
            statement.setString(2, uuid);
            statement.executeUpdate();
            int updateCount = statement.getUpdateCount();
            statement.close();
            connection.close();
            return updateCount > 0;


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static User getUserWithWsSessionId(String wsSessionId) throws NoUserFoundException {
        try {
            Connection connection = DbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT user_id FROM user_status WHERE ws_session_id=?");
            statement.setString(1, wsSessionId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new User(resultSet.getString(1));
            } else throw new NoUserFoundException("No user found with wsSessionId " + wsSessionId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean updateStatusAsOffline(String wsSessionId) {
        try {
            Connection connection = DbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE user_status SET status='0',ws_session_id=SUBSTR(ws_session_id,1,30) WHERE ws_session_id=?");
            statement.setString(1, wsSessionId);
            statement.executeUpdate();
            int updateCount = statement.getUpdateCount();
            statement.close();
            connection.close();
            return updateCount > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<OutboundPersonalMessage> getUndeliveredPersonalMessages() throws SQLException {

        List<OutboundPersonalMessage> messageList = new ArrayList<>();
        Connection connection = DbConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT users_messages.`_uuid`," +
                "  message_data.data," +
                "  users_messages.`_from`," +
                "  users_messages.created " +
                "FROM users_messages JOIN message_data " +
                "ON users_messages.data_id = message_data.`_uuid` " +
                "WHERE users_messages.`_to`=? AND users_messages.delivery_status='0'" +
                " ORDER BY created ASC ");
        statement.setString(1, uuid);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            String messageUuid = resultSet.getString(1);
            String messageData = resultSet.getString(2);
            String messageFromUuid = resultSet.getString(3);
            String serverTimeStamp = resultSet.getString(4);
            OutboundPersonalMessage message = new OutboundPersonalMessage(messageUuid, this.uuid, messageFromUuid, messageData, serverTimeStamp);
            messageList.add(message);
        }
        statement.close();
        connection.close();
        return messageList;
    }

    public static class NoUserFoundException extends Throwable {
        private String message;

        public NoUserFoundException(String message) {

            this.message = message;
        }

        @Override
        public String getMessage() {
            return message;
        }
    }
/*
    public DispatchQueue getDispatchQueue() {
        return new DispatchQueue();
    }


    private class DispatchQueue {

        DispatchQueue() {
        }


        public void add(Message message) {
            message.saveToDatabase();
            message.send();
        }


    }
    */
}
