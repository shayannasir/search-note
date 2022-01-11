package persistence;

import com.mongodb.ConnectionString;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import constants.PropertyConstants;

import java.util.Objects;

public class DBManager {
    private static MongoDatabase searchNoteDb = null;

    private DBManager(){}

    public static MongoDatabase getDb ()  {

        if (Objects.isNull(searchNoteDb)) {
            try {
                System.out.println("Initiating Database connection");
                MongoClient mongoClient = MongoClients.create(new ConnectionString(PropertyConstants.REMOTE_CONNECTION_STRING));
                searchNoteDb = mongoClient.getDatabase(PropertyConstants.MONGO_DB);
                System.out.println("Connected to " + searchNoteDb.getName() + " database");
            } catch (Exception e) {
                System.out.println("Unable to Connect to Database");
                e.printStackTrace();
            }
        }

        return searchNoteDb;
    }
}
