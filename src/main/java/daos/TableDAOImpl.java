package main.java.daos;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import main.java.beans.Table;
import main.java.constants.PropertyConstants;
import main.java.constants.TableConstants;
import main.resources.persistence.DBManager;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TableDAOImpl implements TableDAO {

    MongoDatabase mongoDatabase = DBManager.getDb();
    MongoCollection<Document> tables = mongoDatabase.getCollection(PropertyConstants.MONGO_COL_TABLE);
    MongoCollection<Document> archivedTables = mongoDatabase.getCollection(PropertyConstants.MONGO_COL_ARCHIVED_TABLE);

    @Override
    public List<Table> findAllByKeyword(String keyword) {
        List<Table> tableList = new ArrayList<>();
        FindIterable<Document> query = tables.find(new Document("name", new Document("$regex", keyword).append("$options", "i"))).sort(new Document("name", 1));
        for (Document document : query) {
            String name = document.get("name").toString();
            String id = document.get("_id").toString();
            tableList.add(new Table(id, name));
        }
        return tableList;
    }

    @Override
    public List<Table> findAll() {
        List<Table> tableList = new ArrayList<>();
        for (Document document : tables.find().sort(new Document("name", 1))) {
            String name = document.get("name").toString();
            String id = document.get("_id").toString();
            tableList.add(new Table(id, name));
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

    @Override
    public Boolean updateTable(Table oldTable, Table newTable) {
        if (Objects.nonNull(oldTable) && Objects.nonNull(newTable)) {
            return tables.updateOne(
                    new Document(TableConstants.ID, new ObjectId(oldTable.getId())),
                    new Document("$set", new Document(TableConstants.NAME, newTable.getName()))
            ).wasAcknowledged();
        }
        return false;
    }

    @Override
    public Boolean deleteTable(Table table) {
        if (Objects.nonNull(table)) {
            if (addToArchive(table))
                return tables.deleteOne(new Document(TableConstants.ID, new ObjectId(table.getId()))).wasAcknowledged();
            System.out.println("Failed To archive table");
        }
        System.out.println("Failed to delete table, empty table.");
        return false;
    }

    private Boolean addToArchive(Table table) {
        if (Objects.nonNull(table)) {
            table.setArchived(true);
            try {
                archivedTables.insertOne(
                        new Document(TableConstants.ID, table.getId())
                                .append(TableConstants.NAME, table.getName())
                                .append(TableConstants.ARCHIVED, table.getArchived())
                );
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
}
