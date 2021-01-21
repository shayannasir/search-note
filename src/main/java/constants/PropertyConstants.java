package main.java.constants;

public class PropertyConstants {
    public static final String MONGO_HOST = "localhost";
    public static final int MONGO_PORT = 27017;
    public static final String MONGO_CONNECTION_STRING = "mongodb://" + MONGO_HOST + ":" + MONGO_PORT;
    public static final String MONGO_DB = "search-note";
    public static final String MONGO_PRIMARY_TABLE = "tables";
    public static final String MONGO_SECONDARY_TABLE = "entries";
}
