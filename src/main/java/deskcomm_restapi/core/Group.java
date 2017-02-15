package deskcomm_restapi.core;

import deskcomm_restapi.dbconn.DbConnection;
import org.json.JSONArray;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Jay Rathod on 06-01-2017.
 */
public class Group {
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public JSONArray getMembers() {
        return members;
    }

    public void setMembers(JSONArray members) {
        this.members = members;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    private String uuid;
    private String name;
    private String createdBy;
    private JSONArray members;
    private String iconUrl;

    public Group(String uuid, String name, String createdBy, String iconUrl, JSONArray members) {

        this.uuid = uuid;
        this.name = name;
        this.createdBy = createdBy;
        this.iconUrl = iconUrl;
        this.members = members;
    }

    public boolean saveToDatabase() {

        try {
            Connection connection = DbConnection.getConnection();
            connection.setAutoCommit(false);

            PreparedStatement statement1 = connection.prepareStatement("INSERT INTO `_groups`(`_uuid`,name,created_by,icon_url) VALUES (?,?,?,?)");
            statement1.setString(1, uuid);
            statement1.setString(2, name);
            statement1.setString(3, createdBy);
            statement1.setString(4, iconUrl);
            statement1.executeUpdate();
            int updateCount = statement1.getUpdateCount();
            if (updateCount > 0) {
                statement1.close();
                PreparedStatement statement2 = connection.prepareStatement("INSERT INTO group_members(user_id,group_id) VALUES(?,?)");
                boolean hasFailed = false;
                for (int i = 0; i < members.length(); i++) {
                    statement2.setString(1, members.getString(i));
                    statement2.setString(2, uuid);
                    statement2.executeUpdate();
                    if (statement2.getUpdateCount() <= 0) hasFailed = true;
                }
                if (!hasFailed) {
                    connection.commit();
                    statement2.close();
                    connection.close();
                    return true;
                } else {
                    connection.rollback();
                }
                statement2.close();
                connection.close();
            } else {
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;

    }

}
