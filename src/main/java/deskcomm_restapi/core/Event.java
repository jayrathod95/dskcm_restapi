package deskcomm_restapi.core;

import deskcomm_restapi.dbconn.DbConnection;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static deskcomm_restapi.core.Keys.*;

/**
 * Created by Jay Rathod on 06-01-2017.
 */
public class Event {


    private String title;
    private String uuid;
    private String startDate;
    private String endDate;
    private String venue;
    private String created;
    private String createdBy;
    private String imageUrl;
    private String description;

    public Event(String title, String uuid, String startDate, String endDate, String venue, User[] organisers, String created, String createdBy, String imageUrl, String description) {
        this.title = title;
        this.uuid = uuid;
        this.startDate = startDate;
        this.endDate = endDate;
        this.venue = venue;
        //this.organisers = organisers;
        this.created = created;
        this.createdBy = createdBy;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public Event(String uuid) {
        this.uuid = uuid;
    }

    public Event(JSONObject data) {
        uuid = data.getString(EVENT_ID);
        title = data.getString(EVENT_TITLE);
        startDate = data.getString(EVENT_STARTS);
        endDate = data.getString(EVENT_ENDS);
        venue = data.getString(EVENT_VENUE);
        description = data.getString(EVENT_DESC);
        imageUrl = data.getString(EVENT_IMAGE_URL);
        if (data.has(EVENT_CREATED_BY))
            createdBy = data.getString(EVENT_CREATED_BY);
        //data.getJSONArray(Keys.EVENT_ORGS);

    }

    public static List<Event> getAll() {
        return getAll(0, 50);
    }

    public static List<Event> getAll(int skip, int limit) {
        List<Event> eventList = new ArrayList<>();
        try {
            Connection connection = DbConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT `_uuid`,event_title,starts,ends,venue,description,image_url,created_by,created FROM events ORDER BY created DESC LIMIT ?,?");
            statement.setInt(1, skip);
            statement.setInt(2, limit);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Event event = new Event(resultSet.getString(1));
                event.setTitle(resultSet.getString(2));
                event.setStartDate(resultSet.getString(3));
                event.setEndDate(resultSet.getString(4));
                event.setVenue(resultSet.getString(5));
                event.setDescription(resultSet.getString(6));
                event.setImageUrl(resultSet.getString(7));
                event.setCreatedBy(resultSet.getString(8));
                event.setCreated(resultSet.getString(9));
                eventList.add(event);
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public JSONObject toJSON() {
        return new JSONObject().put(EVENT_ID, uuid).put(EVENT_TITLE, title)
                .put(EVENT_STARTS, startDate)
                .put(EVENT_ENDS, endDate)
                .put(EVENT_VENUE, venue)
                .put(SERVER_TIMESTAMP, created)
                .put(EVENT_CREATED_BY, createdBy)
                .put(EVENT_IMAGE_URL, imageUrl)
                .put(EVENT_DESC, description);
    }

    public boolean saveToDatabase() throws SQLException {
        boolean b;
        Connection connection = DbConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO events(`_uuid`,event_title,starts,ends,venue,description,image_url,created_by) VALUES (?,?,?,?,?,?,?,?)");
        statement.setString(1, uuid);
        statement.setString(2, title);
        statement.setString(3, startDate);
        statement.setString(4, endDate);
        statement.setString(5, venue);
        statement.setString(6, description);
        statement.setString(7, imageUrl);
        statement.setString(8, createdBy);
        statement.executeUpdate();
        int updateCount = statement.getUpdateCount();
        b = updateCount > 0;
        statement.close();
        connection.close();
        return b;
    }

    public boolean fetchFromDb() {
        try {
            Connection connection = DbConnection.getConnection();
            PreparedStatement statement1 = connection.prepareStatement("SELECT event_title,starts,ends,venue,description,img_url,created_by,created FROM events WHERE `_uuid`=?");
            statement1.setString(1, uuid);
            ResultSet resultSet = statement1.executeQuery();
            if (resultSet.next()) {
                title = resultSet.getString(1);
                startDate = resultSet.getString(2);
                endDate = resultSet.getString(3);
                venue = resultSet.getString(4);
                description = resultSet.getString(5);
                imageUrl = resultSet.getString(6);
                createdBy = resultSet.getString(7);
                created = resultSet.getString(8);
                resultSet.close();
                statement1.close();
                connection.close();
                return true;
            }
            resultSet.close();
            statement1.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

