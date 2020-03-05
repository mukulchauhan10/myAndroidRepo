package com.example.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.audiofx.NoiseSuppressor
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // private lateinit var notiManager: NotificationManager (or)
    private val notificationManager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    val channelID: String by lazy { "1" }
    val channelDescription: String by lazy { "Promotional & Offers" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn1.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel = NotificationChannel(
                    channelID,
                    channelDescription,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    enableLights(true)
                    enableVibration(true)
                    lightColor = Color.GREEN
                }
                notificationManager.createNotificationChannel(notificationChannel)
            }

            builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Notification.Builder(this, "1")
            } else {
                Notification.Builder(this)
                    .setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_VIBRATE)
            }

            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.data = Uri.parse("http://www.google.com")
            val pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

            val notification = builder
                .setContentTitle("My Notification")
                .setContentText("I'm always cool")
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
                .addAction(R.drawable.ic_launcher_background, "click", pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .build()

            notificationManager.notify(System.currentTimeMillis().toInt(), notification)
        }
    }
}


/* Importance points

* Notification channel: We have to handle this in the above android oreo project
* Notification builder:
* Notification Manager:

*/