package com.bw.wshost

import android.app.Application
import android.util.Log
import io.ktor.http.*
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.*
import io.ktor.server.html.respondHtml
import io.ktor.server.http.content.staticFiles
import io.ktor.server.http.content.staticResources
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readBytes
import io.ktor.websocket.readText
import kotlinx.html.*
import java.io.File
import io.ktor.websocket.*
import java.util.Collections
import java.util.concurrent.atomic.*

//import org.litote.kmongo.coroutine.CoroutineCollection
//import org.litote.kmongo.coroutine.CoroutineDatabase
//import org.litote.kmongo.coroutine.CoroutineClient
//import org.litote.kmongo.coroutine.coroutine
//import org.litote.kmongo.reactivestreams.KMongo
//import kotlinx.html.*
data class User(val name: String, val age: Int)

class Connection(val session: DefaultWebSocketSession) {
    companion object {
        val lastId = AtomicInteger(0)
    }
    val name = "user${lastId.getAndIncrement()}"
}

class ServerApplication : Application() {

    private var server: NettyApplicationEngine? = null
//    private lateinit var database: CoroutineDatabase
//    private lateinit var userCollection: CoroutineCollection<User>

    private var msgs = mutableListOf<HashMap<String, String>>()

    override fun onCreate() {
        super.onCreate()
        setupDatabase()
        startServer()
    }

    private fun setupDatabase() {
//        val connectionString = "mongodb+srv://defetron27:defetron27@temp.ng7utpw.mongodb.net/"
//        val client: CoroutineClient = KMongo.createClient(connectionString).coroutine
//        database = client.getDatabase("temp")
//        userCollection = database.getCollection<User>("users")
    }

    private fun startServer() {
        server = embeddedServer(Netty, port = 8080) {
            install(WebSockets)
            install(ContentNegotiation)
            routing {
//                staticFiles("/assets", File("static"),"index.html")
                get("/") {
                    call.respondText("Mobile server is running", ContentType.Text.Plain)
                }
//                get("/message") {
//                    call.respondHtml {
//                        head {
//                            title { +"WebSocket Chat" }
//                            link(rel = "stylesheet", href = "/static/style.css", type = "text/css")
//                        }
//                        body {
//                            h1 { +"WebSocket Chat" }
//                            div {
//                                id = "chat"
//                                +"No messages yet."
//                            }
//                            input {
//                                id = "messageInput"
//                                placeholder = "Type a message"
//                            }
//                            button {
//                                id = "sendButton"
//                                +"Send"
//                            }
//                            script(src = "/static/client.js") {}
//                        }
//                    }
//                }
//                get("/users") {
//                    val users = runBlocking { userCollection.find().toList() }
//                    call.respond(users)
//                }
                val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())
                webSocket("/ws") { // this is the path for the WebSocket
//                    send(Frame.Text("You are connected to the WebSocket server!"))
                    val thisConnection = Connection(this)
                    connections += thisConnection
                    try {
                        send("You are connected! There are ${connections.count()} users here.")
                        for (frame in incoming) {
                            frame as? Frame.Text ?: continue
                            val receivedText = frame.readText()
                            val textWithUsername = "[${thisConnection.name}]: $receivedText"
                            connections.forEach {
                                it.session.send(textWithUsername)
                            }
                        }
                    } catch (e: Exception) {
                        println(e.localizedMessage)
                    } finally {
                        println("Removing $thisConnection!")
                        connections -= thisConnection
                    }
//                    for (frame in incoming) {
//                        when (frame) {
//                            is Frame.Text -> {
//                                val receivedText = frame.readText()
//                                outgoing(Frame.Text(receivedText))
//                            }
//                            is Frame.Binary -> {
//                                val receivedData = frame.readBytes()
//                                send(Frame.Text("Server received binary data of size: ${receivedData.size}"))
//                            }
//                            else -> {
//                                send(Frame.Text("Unsupported frame type received"))
//                            }
//                        }
//                    }
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
