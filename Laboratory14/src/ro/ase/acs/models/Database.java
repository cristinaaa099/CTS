package ro.ase.acs.models;

import java.util.List;
import java.util.Map;

public abstract class Database implements DataAccess {
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public Map<String, List<String>> getTables() {
        return tables;
    }

    public void setTables(Map<String, List<String>> tables) {
        this.tables = tables;
    }

    public void storeTable(String table, List<String> schemaArgs)
    {
        tables.put(table, schemaArgs);
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    private String address;
    private String databaseName;

    private Map<String, List<String>> tables;

    private int port;
}
