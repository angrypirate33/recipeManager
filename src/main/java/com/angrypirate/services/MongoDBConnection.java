package com.angrypirate.services;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoDBConnection {
    private static MongoClient mongoClient;

    public static MongoDatabase getDatabase() {
        String mongoString = System.getenv("MONGO_STRING").trim();
        if (mongoClient == null) {
            ConnectionString connectionString = new ConnectionString(mongoString);
            CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                    fromProviders(PojoCodecProvider.builder().automatic(true).build()));
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .codecRegistry(pojoCodecRegistry)
                    .build();
            mongoClient = MongoClients.create(settings);
        }
        return mongoClient.getDatabase("recipe_manager");
    }

}
