package main.java.daos;

import main.java.beans.Entry;
import main.java.beans.Table;

import java.util.List;

public interface EntryDAO {
    List<Entry> findAllByTable(Table table);
    List<Entry> findAllByTableAndKey(Table table, String key);
    Boolean addEntry(Entry entry);
}
