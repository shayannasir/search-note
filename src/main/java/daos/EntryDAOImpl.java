package main.java.daos;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import main.java.beans.Entry;
import main.java.beans.Table;
import main.java.constants.EntryConstants;
import main.java.constants.MongoConstants;
import main.java.constants.PropertyConstants;
import main.resources.persistence.DBManager;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class EntryDAOImpl implements EntryDAO {

    MongoDatabase mongoDatabase = DBManager.getDb();
    MongoCollection<Document> entries =
            mongoDatabase.getCollection(PropertyConstants.MONGO_COL_ENTRY);

    @Override
    public List<Entry> findAllByTable(Table table) {
        List<Entry> entryList = new ArrayList<>();
        for (Document document :
                entries
                        .find(new Document(EntryConstants.TABLE_ID, table.getId()))
                        .sort(new Document(EntryConstants.KEY, 1))) {
            entryList.add(reverseBinder(document));
        }
        return entryList;
    }

    @Override
    public List<Entry> findAllByTableAndKey(Table table, String keyword) {
        List<Entry> entryList = new ArrayList<>();
        for (Document document :
                entries
                        .find(
                                new Document(EntryConstants.TABLE_ID, table.getId())
                                        .append(
                                                EntryConstants.KEY,
                                                new Document(MongoConstants.REGEX, keyword)
                                                        .append(MongoConstants.OPTIONS, "i")))
                        .sort(new Document(EntryConstants.KEY, 1))) {
            entryList.add(reverseBinder(document));
        }
        return entryList;
    }

    @Override
    public Boolean addEntry(Entry entry) {
        try {
            entries.insertOne(forwardBinder(entry));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Entry reverseBinder(Document document) {
        String id = document.get(EntryConstants.ID).toString();
        String key = document.get(EntryConstants.KEY, String.class);
        String value = document.get(EntryConstants.VALUE, String.class);
        String desc = document.get(EntryConstants.DESCRIPTION, String.class);
        String tableId = document.get(EntryConstants.TABLE_ID, String.class);
        return new Entry(id, key, value, desc, tableId);
    }

    private Document forwardBinder(Entry entry) {
        return new Document(EntryConstants.KEY, entry.getKey())
                .append(EntryConstants.VALUE, entry.getValue())
                .append(EntryConstants.DESCRIPTION, entry.getDescription())
                .append(EntryConstants.TABLE_ID, entry.getTableId());
    }
}
