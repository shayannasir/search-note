package daos;

import beans.Entry;
import beans.Table;

import java.util.List;

public interface EntryDAO {
    List<Entry> findAllByTable(Table table);
    List<Entry> findAllByTableAndKey(Table table, String key);
    Boolean addEntry(Entry entry);
    Boolean updateEntry(Entry oldEntry, Entry newEntry);
    Boolean deleteEntry(Entry entry);

}
