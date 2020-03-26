package com.example.broadcastingservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class MyBroadcastReceiver: BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("receivedBroadcast", " mode changed")
        Toast.makeText(context,intent?.getStringExtra("name"), Toast.LENGTH_SHORT).show()
    }

}