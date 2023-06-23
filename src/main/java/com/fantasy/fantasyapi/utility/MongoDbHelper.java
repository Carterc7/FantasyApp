package com.fantasy.fantasyapi.utility;

public class MongoDbHelper 
{
    private final String connectionString;
    private final String databaseName;
    private final String collectionName;

    public MongoDbHelper(String connectionString, String databaseName, String collectionName) {
        this.connectionString = connectionString;
        this.databaseName = databaseName;
        this.collectionName = collectionName;
    }
    
    public String getConnectionString() {
        return connectionString;
    }
    public String getDatabaseName() {
        return databaseName;
    }
    public String getCollectionName() {
        return collectionName;
    }
}

