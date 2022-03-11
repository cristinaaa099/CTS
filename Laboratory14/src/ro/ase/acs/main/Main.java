package ro.ase.acs.main;

import ro.ase.acs.models.Database;
import ro.ase.acs.nosql.MongoDB;
import ro.ase.acs.sql.SQL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Database> databases = new ArrayList<>();

        Database sqlDatabase = new SQL();
        sqlDatabase.init("localhost", 9999, "dbSQL");
        databases.add(sqlDatabase);

        Database mongoDatabase = new MongoDB();
        mongoDatabase.init("localhost", 7777, "dbMongo");
        databases.add(mongoDatabase);

        databases.forEach(database -> {
            database.create("studenti", Arrays.asList("nume", "prenume", "domeniu"));
            database.insert("studenti", Arrays.asList("John", "Johnson", "Cibernetica"),
                    Arrays.asList("Melon", "Usk", "Management"),
                    Arrays.asList("Jim", "Bean", "Trading"));
            database.selectAll("studenti");
        });
    }
}
