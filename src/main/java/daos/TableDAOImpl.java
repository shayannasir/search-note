package main.java.daos;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import main.java.beans.Table;
import main.java.constants.PropertyConstants;
import main.resources.persistence.DBManager;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class TableDAOImpl implements TableDAO {

    MongoDatabase mongoDatabase = DBManager.getDb();
    MongoCollection<Document> tables = mongoDatabase.getCollection(PropertyConstants.MONGO_PRIMARY_TABLE);

    @Override
    public List<Table> findAllByKeyword(String keyword) {
        List<Table> tableList = new ArrayList<>();
        FindIterable<Document> query = tables.find(new Document("name", new Document("$regex", keyword).append("$options", "i"))).sort(new Document("name", 1));
        for (Document document : query) {
            String name = document.get("name").toString();
            tableList.add(new Table(name));
        }
        return tableList;
    }

    @Override
    public List<Table> findAll() {
        List<Table> tableList = new ArrayList<>();
        for (Document document : tables.find().sort(new Document("name", 1))) {
            String name = document.get("name").toString();
            tableList.add(new Table(name));
        }
        return tableList;
    }

    @Override
    public Table findById(int id) {
        return null;
    }

    @Override
    public Boolean addTable(Table newTable) {
        try {
            tables.insertOne(new Document("name", newTable.getName()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
