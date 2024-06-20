package com.bw.wshost

import android.util.Log
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.readBytes
import io.ktor.websocket.readText
import kotlinx.coroutines.*

class WebSocketClient {

    private val client = HttpClient(OkHttp) {
        install(WebSockets)
    }

    private var session: DefaultClientWebSocketSession? = null

    fun connectWebSocket(onMessageReceived: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            client.webSocket(method = HttpMethod.Get, host = "106.51.106.43", path = "/ws") {
                Log.d("WebSocket", "Connected to the WebSocket server.")

                send(Frame.Text("Hello from Android client!"))

                for (frame in incoming) {
                    when (frame) {
                        is Frame.Text -> {
                            val receivedText = frame.readText()
                            Log.d("WebSocket", "Received: $receivedText")
                            withContext(Dispatchers.Main) {
                                onMessageReceived(receivedText)
                            }
                        }
                        is Frame.Binary -> {
                            val receivedData = frame.readBytes()
                            Log.d("WebSocket", "Received binary data of size: ${receivedData.size}")
                        }
                        is Frame.Close -> {
                            Log.d("WebSocket", "Closed",)
                        }
                        is Frame.Ping -> {
                            Log.d("WebSocket", "Pinged",)
                        }
                        is Frame.Pong -> {
                            Log.d("WebSocket", "Ponged",)
                        }
                    }
                }
            }
        }
    }

    fun sendMessage(message: String) {
        CoroutineScope(Dispatchers.IO).launch {
            session?.send(Frame.Text(message))
        }
    }

    fun disconnect() {
        client.close()
    }
}
