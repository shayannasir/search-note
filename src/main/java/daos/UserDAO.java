package daos;

import beans.User;

import java.security.NoSuchAlgorithmException;

public interface UserDAO {
    User findUserByUsername(String username);
    Boolean createNewUser(User user);
}
