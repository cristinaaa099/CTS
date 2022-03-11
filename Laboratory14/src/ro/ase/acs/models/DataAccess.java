package ro.ase.acs.models;

import java.util.List;

public interface DataAccess {
    void create(String tableName, List<String> schemaArgs);

    void init(String address, int port, String databaseName);

    void selectAll(String tableName);

    void select(String tableName, String key, String value);

    void drop(String table);

    void insert(String tableName, List<String>...args);

    void shutdown();
}
