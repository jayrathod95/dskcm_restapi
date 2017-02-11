package deskcomm_restapi.core;

import deskcomm_restapi.dbconn.DbConnection;
import deskcomm_restapi.exceptions.InvalidParamException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

/**
 * aaaaaaaaaaaaaaa
 * Created by Jay Rathod on 07-01-2017.
 */
public class UserBuilder {
    private String firstname;
    private String lastname;
    private String email;
    private String mobile;
    private String password;
    private String image_url;
    private String uid;

    public UserBuilder(String firstname, String lastname, String email, String mobile, String password, String image_url, String uid) {
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.image_url = image_url;
        this.uid = uid;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public UserBuilder() {

    }

    @SuppressWarnings("Duplicates")
    public boolean build() throws SQLException {

        Connection connection = DbConnection.getConnection();
        connection.setAutoCommit(false);
        PreparedStatement statement = connection.prepareStatement("INSERT INTO " +
                "users(`_uuid`,fname,lname,email,mobile,password,img_url,`_uid`)" +
                "VALUES(?,?,?,?,?,?,?,?)");
        String uuid = UUID.randomUUID().toString();
        statement.setString(1, uuid);
        statement.setString(2, firstname);
        statement.setString(3, lastname);
        statement.setString(4, email);
        statement.setString(5, mobile);
        statement.setString(6, password);
        statement.setString(7, image_url);
        statement.setString(8, uid);
        statement.executeUpdate();
        int updateCount = statement.getUpdateCount();
        if (updateCount > 0) {
            statement.close();
            PreparedStatement statement1 = connection.prepareStatement("INSERT INTO user_status(user_id,  ws_session_id) VALUE (?,?)");
            statement1.setString(1, uuid);
            statement1.setString(2, uuid.substring(0, 30));
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
                return false;
            }
        }
        connection.rollback();
        statement.close();
        connection.close();

        return false;
    }

    public void setFirstname(String firstname) throws InvalidParamException {
        if (firstname.length() > 0)
            this.firstname = firstname;
        else throw new InvalidParamException("Firstname");

    }

    public void setLastname(String lastname) throws InvalidParamException {
        if (lastname.length() > 0)
            this.lastname = lastname;
        else throw new InvalidParamException("Lastname");
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws InvalidParamException {
        if (email.length() > 0)
            this.email = email;
        else throw new InvalidParamException("Email");
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) throws InvalidParamException {
        if (mobile != null && mobile.length() > 0)
            this.mobile = mobile;
        else throw new InvalidParamException("Mobile");

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws InvalidParamException {
        if (password.length() > 0)
            this.password = password;
        else throw new InvalidParamException("Password");

    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) throws InvalidParamException {
        if (image_url.length() > 0)
            this.image_url = image_url;
        else throw new InvalidParamException("Image URL");

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) throws InvalidParamException {
        if (uid.length() > 0)
            this.uid = uid;
        else throw new InvalidParamException("UID");
    }
}
