package com.bw.wshost

import android.R.attr.password
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSpecifier
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.lang.reflect.InvocationTargetException


class MainActivity : AppCompatActivity() {
//    private lateinit var webSocketClient: WebSocketClient
//    private lateinit var messageInput: EditText
//    private lateinit var sendButton: Button
//    private lateinit var messageDisplay: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//
//        messageInput = findViewById(R.id.messageInput)
//        sendButton = findViewById(R.id.sendButton)
//        messageDisplay = findViewById(R.id.messageDisplay)
//
//        webSocketClient = WebSocketClient()
//        webSocketClient.connectWebSocket { message ->
//            messageDisplay.append("\nServer: $message")
//        }
//
//        sendButton.setOnClickListener {
//            val message = messageInput.text.toString()
//            if (message.isNotEmpty()) {
//                webSocketClient.sendMessage(message)
//                messageDisplay.append("\nYou: $message")
//                messageInput.text.clear()
//            }
//        }
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        webSocketClient.disconnect()
//    }
}