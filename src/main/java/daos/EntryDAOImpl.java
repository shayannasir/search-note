package main.java.daos;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import main.java.beans.Entry;
import main.java.beans.Table;
import main.java.constants.EntryConstants;
import main.java.constants.MessageConstants;
import main.java.constants.MongoConstants;
import main.java.constants.PropertyConstants;
import main.resources.persistence.DBManager;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EntryDAOImpl implements EntryDAO {

    MongoDatabase mongoDatabase = DBManager.getDb();
    MongoCollection<Document> entries =
            mongoDatabase.getCollection(PropertyConstants.MONGO_COL_ENTRY);
    MongoCollection<Document> archivedEntries = mongoDatabase.getCollection(PropertyConstants.MONGO_COL_ARCHIVED_ENTRY);

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
                                        .append("$or", orListGenerator(keyword)))
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

    @Override
    public Boolean updateEntry(Entry oldEntry, Entry newEntry) {
        if (Objects.nonNull(oldEntry) && Objects.nonNull(newEntry)) {
            return entries.updateOne(
                    new Document(EntryConstants.ID, new ObjectId(oldEntry.getId())),
                    new Document(MongoConstants.SET,
                            new Document(EntryConstants.KEY, newEntry.getKey())
                                    .append(EntryConstants.VALUE, newEntry.getValue())
                                    .append(EntryConstants.DESCRIPTION, newEntry.getDescription()))
            ).wasAcknowledged();
        }
        return false;
    }

    @Override
    public Boolean deleteEntry(Entry entry) {
        if (Objects.nonNull(entry)) {
            if (addToArchive(entry))
                return entries.deleteOne(new Document(EntryConstants.ID, new ObjectId(entry.getId()))).wasAcknowledged();
            System.out.println(MessageConstants.ENTRY_ARCHIVED_FAILED);
        }
        System.out.println(MessageConstants.ENTRY_DELETE_FAILED);
        return false;
    }

    private Boolean addToArchive(Entry entry) {
        if (Objects.nonNull(entry)) {
            entry.setArchved(Boolean.TRUE);
            try {
                archivedEntries.insertOne(
                        new Document(EntryConstants.ID, entry.getId())
                        .append(EntryConstants.KEY, entry.getKey())
                        .append(EntryConstants.VALUE, entry.getValue())
                        .append(EntryConstants.DESCRIPTION, entry.getDescription())
                        .append(EntryConstants.TABLE_ID, entry.getTableId())
                        .append(EntryConstants.ARCHIVED, Boolean.TRUE)
                );
                return true;
            } catch(Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
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

    private List<Document> orListGenerator(String keyword) {
        List<Document> list = new ArrayList<>();
        list.add(new Document(EntryConstants.KEY, new Document(MongoConstants.REGEX, keyword)
                        .append(MongoConstants.OPTIONS, "i")));
        list.add(new Document(EntryConstants.VALUE, new Document(MongoConstants.REGEX, keyword)
                .append(MongoConstants.OPTIONS, "i")));
        list.add(new Document(EntryConstants.DESCRIPTION, new Document(MongoConstants.REGEX, keyword)
                .append(MongoConstants.OPTIONS, "i")));
        return list;
    }
}
