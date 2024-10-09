package com.angrypirate.services;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {
    private static MongoClient mongoClient;

    public static MongoDatabase getDatabase() {
        String mongoString = System.getenv("MONGO_STRING").trim();
        if (mongoClient == null) {
            ConnectionString connectionString = new ConnectionString(mongoString);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .build();
            mongoClient = MongoClients.create(settings);
        }
        return mongoClient.getDatabase("recipe_manager");
    }

}
