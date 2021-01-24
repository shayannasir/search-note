package main.java.constants;

public class PropertyConstants {
    public static final String MONGO_HOST = "localhost";
    public static final int MONGO_PORT = 27017;
    public static final String MONGO_CONNECTION_STRING = "mongodb://" + MONGO_HOST + ":" + MONGO_PORT;
    public static final String MONGO_DB = "search-note";
    public static final String MONGO_COL_TABLE = "tables";
    public static final String MONGO_COL_ARCHIVED_TABLE = "archived_tables";
    public static final String MONGO_COL_ENTRY = "entries";
    public static final String MONGO_COL_ARCHIVED_ENTRY = "archived_entries";
}
