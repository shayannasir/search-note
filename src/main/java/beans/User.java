package beans;

import constants.UserConstants;
import org.bson.Document;

import java.util.Date;

public class User {

    String id;
    String username;
    String password;
    Boolean enabled = true;
    Date createdDate;

    public User(){}

    public User(Document document) {
        id = document.get(UserConstants.id).toString();
        username = document.get(UserConstants.USERNAME, String.class);
        password = document.get(UserConstants.PASSWORD, String.class);
        enabled = document.get(UserConstants.ENABLED, Boolean.class);
        createdDate = document.get(UserConstants.CREATED_DATE, Date.class);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
