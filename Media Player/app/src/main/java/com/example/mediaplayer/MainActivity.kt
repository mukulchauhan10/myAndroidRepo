package com.example.mediaplayer

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.jar.Manifest

const val WRITE_CODE = 9999
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            val permission = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

            if(permission == PackageManager.PERMISSION_GRANTED){
                letsCome()
            }else{
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), WRITE_CODE)
            }
        }

        mediaButton.setOnClickListener {
            startActivity(Intent(this,MediaPlayer::class.java))
        }

        restorebutton.setOnClickListener {

        }
    }



    private fun letsCome() {
        Toast.makeText(this
        ,"aaguya", Toast.LENGTH_LONG).show()
        val myData = editText.text.toString()

        GlobalScope.launch {
            val directory = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath
            Log.i("ask","1")
            val file = File(directory, "mFile.txt")
            val fileWritten = withContext(Dispatchers.IO){
                file.writeBytes(myData.toByteArray())
                true

            }
            Log.i("ask",fileWritten.toString())
        }



    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == WRITE_CODE){
            if(grantResults[0] ==  PackageManager.PERMISSION_GRANTED){
                letsCome()
            }else{
                Toast.makeText(this,"Permission denied", Toast.LENGTH_LONG).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }
}
