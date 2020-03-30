package com.example.broadcastingservice

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    val receiver = MyBroadcastReceiver()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        val localIntent = Intent("com.example.broadcastingservice").putExtra("name", "Mukul")
        localBroadcastManager.registerReceiver(
            receiver,
            IntentFilter("com.example.broadcastingservice")
        )
        localBroadcastManager.sendBroadcast(localIntent)
        setUpAlarm()
        setWorkerRequest()
    }

    private fun setWorkerRequest() {
        val inputData = workDataOf("title" to "Hello")

        val workRequest = OneTimeWorkRequestBuilder<Notification>()
            .setInitialDelay(20, TimeUnit.SECONDS)
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(this).enqueue(workRequest)
    }

    private fun setUpAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, MyBroadcastReceiver::class.java)
        intent.putExtra("name", "any name")
        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent, PendingIntent.FLAG_ONE_SHOT
        )
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 20000, pendingIntent
        )
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
