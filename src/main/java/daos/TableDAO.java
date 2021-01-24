package daos;

import beans.Table;

import java.util.List;

public interface TableDAO {
    List<Table> findAllByKeyword(String keyword);
    List<Table> findAll();
    Table findById(int id);
    Boolean addTable(Table newTable);
    Boolean updateTable(Table oldTable, Table newTable);
    Boolean deleteTable(Table table);
//    Boolean addToArchive(Table table);
}
