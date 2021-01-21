package main.java.daos;

import main.java.beans.Table;

import java.util.List;

public interface TableDAO {
    List<Table> findAllByKeyword(String keyword);
    List<Table> findAll();
    Table findById(int id);
    Boolean addTable(Table newTable);
}
