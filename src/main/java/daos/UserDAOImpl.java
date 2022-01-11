package daos;

import beans.User;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import constants.PropertyConstants;
import constants.UserConstants;
import org.bson.Document;
import persistence.DBManager;

public class UserDAOImpl implements UserDAO {
    MongoDatabase mongoDatabase = DBManager.getDb();
    MongoCollection<Document> users =
            mongoDatabase.getCollection(PropertyConstants.MONGO_COL_USER);


    @Override
    public User findUserByUsername(String username) {
        for (Document document : users.find(new Document(UserConstants.USERNAME, username)))
            return new User(document);
        return null;
    }

    @Override
    public Boolean createNewUser(User user) {
        try {
            users.insertOne(forwardBinder(user));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Document forwardBinder (User user) {
        return new Document(UserConstants.USERNAME, user.getUsername())
                .append(UserConstants.PASSWORD, user.getPassword())
                .append(UserConstants.ENABLED, user.getEnabled())
                .append(UserConstants.CREATED_DATE, user.getCreatedDate());
    }
}
