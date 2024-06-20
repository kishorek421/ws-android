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
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.lang.reflect.InvocationTargetException


class MainActivity : AppCompatActivity() {
    private val PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        checkPermissions();
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION)
        } else {
            // Permissions are already granted
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                setupWifiHotspot()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission granted
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    setupWifiHotspot()
                }
            } else {
                // Permission denied
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun setupWifiHotspot() {
        val wifiNetworkSpecifier = WifiNetworkSpecifier.Builder()
            .setSsid("MyHotspot")
            .setWpa2Passphrase("password123")
            .build()
        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .setNetworkSpecifier(wifiNetworkSpecifier)
            .build()
        val connectivityManager =
            this.applicationContext.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, NetworkCallback())

//        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
//
//        val wifiConfig = WifiConfiguration().apply {
//            SSID = "MyHotspot"
//            preSharedKey = "password123"
//            allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK)
//        }

//        try {
//            val method = wifiManager.javaClass.getMethod(
//                "setWifiApEnabled",
//                WifiConfiguration::class.java, Boolean::class.javaPrimitiveType
//            )
//            val success = method.invoke(wifiManager, wifiConfig, true) as Boolean
//            if (success) {
//                Log.d("Hotspot", "Hotspot created successfully")
//            } else {
//                Log.d("Hotspot", "Failed to create hotspot")
//            }
//        } catch (e: NoSuchMethodException) {
//            e.printStackTrace()
//        } catch (e: IllegalAccessException) {
//            e.printStackTrace()
//        } catch (e: InvocationTargetException) {
//            e.printStackTrace()
//        }
    }


    private fun setupNetworkRouting() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.requestNetwork(request, object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                connectivityManager.bindProcessToNetwork(network)
                Log.d("Network", "Connected to network")
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                connectivityManager.bindProcessToNetwork(null)
                Log.d("Network", "Lost connection to network")
            }
        })
    }

}