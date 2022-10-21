package dev.nyon.roler.utils

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import dev.nyon.roler.RoleEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

private val connectionString = ConnectionString(
    "mongodb://${System.getenv("MONGO_USERNAME")}:${System.getenv("MONGO_PASSWORD")}@${
        System.getenv(
            "MONGO_ADDRESS"
        )
    }:${System.getenv("MONGO_PORT")}/?authSource=${System.getenv("MONGO_DATABASE")}"
)
val mongoClient =
    KMongo.createClient(MongoClientSettings.builder().applyConnectionString(connectionString).build()).coroutine

val db = mongoClient.getDatabase(System.getenv("MONGO_DATABASE"))

lateinit var roleCollection: CoroutineCollection<RoleEntry>

suspend fun initMongoDbs() {
    roleCollection = if (db.listCollectionNames().contains("roleEntries")) db.getCollection("roleEntries")
    else {
        db.createCollection("roleEntries")
        db.getCollection("roleEntries")
    }
}