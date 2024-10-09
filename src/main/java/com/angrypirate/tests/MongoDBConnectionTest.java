package com.angrypirate.tests;

import com.angrypirate.services.MongoDBConnection;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnectionTest {
    public static void main(String[] args) {
        MongoDatabase database = MongoDBConnection.getDatabase();
        System.out.println("Connected to database: " + database.getName());
    }
}
