package deskcomm_restapi.core;

import org.json.JSONObject;

import java.sql.SQLException;

/**
 * Created by Jay Rathod on 10-02-2017.
 */
public class Identity {
    private String uuid;
    private String sessionId;

    public Identity() {
    }

    public boolean verify() {
        try {
            return User.verifySession(uuid, sessionId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public Identity(JSONObject jsonObject) {
        uuid = jsonObject.getString(Keys.USER_UUID);
        sessionId = jsonObject.getString(Keys.SESSION_ID);
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public JSONObject toJSON() {
        return new JSONObject().put(Keys.USER_UUID, uuid).put(Keys.SESSION_ID, sessionId);
    }

    @Override
    public String toString() {
        return toJSON().toString();
    }

    public User getUser() {
        User user = new User(this.uuid);
        user.setSessionId(this.sessionId);
        return user;
    }
}
