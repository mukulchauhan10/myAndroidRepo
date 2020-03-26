package com.example.broadcastingservice

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class MainActivity : AppCompatActivity() {

    val receiver = MyBroadcastReceiver()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        val localIntent = Intent("com.example.broadcastingservice").putExtra("name", "Mukul")
        localBroadcastManager.registerReceiver(receiver,
            IntentFilter("com.example.broadcastingservice")
        )
        localBroadcastManager.sendBroadcast(localIntent)
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(receiver, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(receiver)
    }
}
