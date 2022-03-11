package ro.ase.acs.sql;

import ro.ase.acs.models.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class SQL extends Database {
    private Connection connection;
    private final int DEFAULT_PORT = 1443;

    @Override
    public void create(String tableName, List<String> schemaArgs) {
        String sqlCreate = "CREATE TABLE " + tableName + "(id INTEGER PRIMARY KEY AUTOINCREMENT";
        int schemaSize = schemaArgs.size();
        if (schemaSize > 0) {
            for (int i = 0; i < schemaSize; ++i) {
                sqlCreate += schemaArgs.get(i);
                if (i < schemaSize - 1) {
                    sqlCreate += ", ";
                }
            }
        }
        sqlCreate += ")";

        executeQuery(sqlCreate);

        storeTable(tableName, schemaArgs);
    }

    @Override
    public void init(String address, int port, String databaseName) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String _address = "";
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + databaseName + ".db");
            _address = "jdbc:sqlite:" + databaseName + ".db";
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            connection.setAutoCommit(false);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        setAddress(address + "|" + _address);
        setPort(DEFAULT_PORT);
        setDatabaseName(databaseName);
    }

    private void getQueryResults(String query) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        ResultSet rs = null;
        try {
            rs = statement.executeQuery(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        ResultSetMetaData rsmd = null;
        try {
            rsmd = rs.getMetaData();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        int columnsNumber = 0;
        try {
            columnsNumber = rsmd.getColumnCount();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        while (true) {
            try {
                if (!rs.next()) break;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) System.out.print(",  ");
                String columnValue = null;
                try {
                    columnValue = rs.getString(i);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            System.out.println("");
        }
        try {
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void selectAll(String tableName) {
        String sqlSelect = "SELECT * FROM " + tableName;
        getQueryResults(sqlSelect);
    }

    @Override
    public void select(String tableName, String key, String value) {
        String sqlSelect = "SELECT * FROM " + tableName + " WHERE " + key + " = " + value;
        getQueryResults(sqlSelect);
    }

    public void executeQuery(String query) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            statement.executeUpdate(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            connection.commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void drop(String table) {
        String sqlDrop = "DROP TABLE IF EXISTS " + table;
        executeQuery(sqlDrop);
    }

    private void formatList(String initialString, List<String> list) {
        int listSize = list.size();
        for (int i = 0; i < listSize; ++i) {
            initialString += list.get(i);
            if (i < listSize - 1) {
                initialString += ",";
            }
        }
    }

    @Override
    public void insert(String tableName, List<String>... args) {
        String sqlInsert = "INSERT INTO " + tableName;

        List<String> columnNames = new ArrayList<>();
        List<String> columnValues = new ArrayList<>();

        Arrays.stream(args).forEach(argList -> {
            columnNames.add(argList.get(0));
            columnValues.add(argList.get(1));
        });

        String columnNamesQuery = "";
        formatList(columnNamesQuery, columnNames);

        String columnValuesQuery = "";
        formatList(columnValuesQuery, columnValues);

        sqlInsert += "(" + columnNamesQuery + ") VALUES(" + columnValuesQuery + ")";
        executeQuery(sqlInsert);
    }

    @Override
    public void shutdown() {
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
