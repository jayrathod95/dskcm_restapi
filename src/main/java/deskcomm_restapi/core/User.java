package deskcomm_restapi.core;

import com.sun.org.glassfish.gmbal.Description;
import deskcomm_restapi.dbconn.DBConnection;
import deskcomm_restapi.exceptions.LoginException;

import javax.json.Json;
import javax.json.JsonObject;
import java.security.Key;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    private String created;


    public static UserBuilder getUserBuilder() {
        return new UserBuilder();
    }

    public static User login(String email, String password) throws SQLException, LoginException {
        Connection connection = DBConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT _uuid,fname,lname,email,mobile,password,img_url,created,_uid FROM users WHERE email=? AND password=?");
        statement.setString(1, email);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            User user = new User();
            user.setUuid(resultSet.getString(Keys.USER_UUID));
            user.setFirstname(resultSet.getString(Keys.USER_FIRSTNAME));
            user.setLastname(resultSet.getString(Keys.USER_LASTNAME));
            user.setEmail(resultSet.getString(Keys.USER_EMAIL));
            user.setMobile(resultSet.getString(Keys.USER_MOBILE));
            user.setPassword(resultSet.getString(Keys.USER_PASSWORD));
            user.setImage_url(resultSet.getString(Keys.USER_IMG_URL));
            user.setCreated(resultSet.getString(Keys.USER_CREATED));
            user.setUid(resultSet.getString(Keys.USER_UID));
            return user;
        } else {
            throw new LoginException();
        }
    }

    @Override
    @Description("Outputs JSONObject String")
    public String toString() {
        JsonObject jsonObject = Json.createObjectBuilder().add(Keys.USER_UUID, uuid)
                .add(Keys.USER_FIRSTNAME, firstname)
                .add(Keys.USER_LASTNAME, lastname)
                .add(Keys.USER_EMAIL, email)
                .add(Keys.USER_MOBILE, mobile)
                .add(Keys.USER_IMG_URL, image_url)
                .add(Keys.USER_CREATED, created)
                .add(Keys.USER_UID, uid)
                .build();
        return jsonObject.toString();
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
}
