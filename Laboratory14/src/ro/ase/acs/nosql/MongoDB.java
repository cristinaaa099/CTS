package ro.ase.acs.nosql;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import ro.ase.acs.models.Database;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

public class MongoDB extends Database {

    private MongoClient mongoClient;
    private MongoDatabase mongoDb;

    @Override
    public void init(String address, int port, String databaseName) {
        setAddress(address);
        setPort(port);
        setDatabaseName(databaseName);

        mongoClient = new MongoClient(address, port);
        mongoDb = mongoClient.getDatabase(databaseName);
    }

    @Override
    public void create(String tableName, List<String> schemaArgs) {
        mongoDb.createCollection(tableName);
        storeTable(tableName, schemaArgs);
    }

    @Override
    public void select(String tableName, String key, String value) {
        FindIterable<Document> result = mongoDb.getCollection(tableName).find(eq(key, value));
        for (Document doc : result) {
            System.out.println(doc);
        }
    }

    @Override
    public void selectAll(String tableName) {
        FindIterable<Document> result = mongoDb.getCollection(tableName).find();
        for (Document doc : result) {
            System.out.println(doc);
        }
    }

    @Override
    public void drop(String table) {
        if (mongoDb.getCollection(table) != null) {
            mongoDb.getCollection(table).drop();
        }
    }

    @Override
    public void insert(String tableName, List<String>... args) {
        Document document = new Document();
        Arrays.stream(args).forEach(argList -> {
            Iterator<String> argsIterator = argList.iterator();
            Iterator<String> schemaArgsIterator = getTables().get(tableName).iterator();

            while (argsIterator.hasNext() && schemaArgsIterator.hasNext()) {
                String currentValue = argsIterator.next();
                String currentSchemaArgValue = schemaArgsIterator.next();

                try {
                    Integer integerValue = Integer.parseInt(currentValue);
                    document.append(currentSchemaArgValue, integerValue);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    try {
                        Double doubleValue = Double.parseDouble(currentValue);
                        document.append(currentSchemaArgValue, doubleValue);
                    } catch (NumberFormatException e2) {
                        e2.printStackTrace();
                        document.append(currentSchemaArgValue, currentValue);
                    }
                }
            }
        });

        MongoCollection<Document> collection = mongoDb.getCollection(tableName);

        collection.insertOne(document);
    }

    public void shutdown() {
        mongoClient.close();
    }
}
