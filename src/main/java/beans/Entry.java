package main.java.beans;

public class Entry {
    private String id;
    private String key;
    private String value;
    private String description;
    private String tableId;

    public Entry(String id, String key, String value, String description, String tableId) {
        this.id = id;
        this.key = key;
        this.value = value;
        this.description = description;
        this.tableId = tableId;
    }

    public Entry(String key, String value, String description) {
        this.key = key;
        this.value = value;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "id='" + id + '\'' +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", description='" + description + '\'' +
                ", tableId='" + tableId + '\'' +
                '}';
    }
}
