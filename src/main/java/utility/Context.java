package utility;

import beans.User;

public class Context {
    private static final Context instance = new Context();

    public static Context getInstance() {
        return instance;
    }

    private Context(){}

    private User user = null;

    public User getUser() {
        return user;
    }

    public void setUser(User loggedInUser) {
        user = loggedInUser;
    }

    public void removeUser() {
        user = null;
    }
}
