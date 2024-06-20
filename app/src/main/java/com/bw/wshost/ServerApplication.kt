package com.bw.wshost

import android.app.Application
import android.util.Log
import androidx.core.app.PendingIntentCompat.send
import io.ktor.http.*
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readBytes
import io.ktor.websocket.readText
import kotlinx.coroutines.runBlocking
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

data class User(val name: String, val age: Int)

class ServerApplication : Application() {

    private var server: NettyApplicationEngine? = null
    private lateinit var database: CoroutineDatabase
    private lateinit var userCollection: CoroutineCollection<User>

    override fun onCreate() {
        super.onCreate()
        setupDatabase()
        startServer()
    }

    private fun setupDatabase() {
        val connectionString = "mongodb+srv://defetron27:defetron27@temp.ng7utpw.mongodb.net/"
        val client: CoroutineClient = KMongo.createClient(connectionString).coroutine
        database = client.getDatabase("temp")
        userCollection = database.getCollection<User>("users")
    }

    private fun startServer() {
        server = embeddedServer(Netty, port = 8080) {
            install(WebSockets)
            routing {
                get("/") {
                    call.respondText("Hello, Ktor on Android!", ContentType.Text.Plain)
                }
                get("/users") {
                    val users = runBlocking { userCollection.find().toList() }
                    call.respond(users)
                }
                webSocket("/ws") { // this is the path for the WebSocket
                    send(Frame.Text("You are connected to the WebSocket server!"))

                    for (frame in incoming) {
                        when (frame) {
                            is Frame.Text -> {
                                val receivedText = frame.readText()
                                send(Frame.Text("Server received: $receivedText"))
                            }
                            is Frame.Binary -> {
                                val receivedData = frame.readBytes()
                                send(Frame.Text("Server received binary data of size: ${receivedData.size}"))
                            }
                            else -> {
                                send(Frame.Text("Unsupported frame type received"))
                            }
                        }
                    }
                }
            }
        }.start(wait = false)

        Log.d("ServerApplication", "Server started on port 8080")
    }

    override fun onTerminate() {
        super.onTerminate()
        server?.stop(1000, 10000)
        Log.d("ServerApplication", "Server stopped")
    }
}
