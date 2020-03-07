package com.example.toolbar2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_toolbar.*

class ToolbarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toolbar)
        setSupportActionBar(toolbarID)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setTitle("Second Override")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        R.id.first -> {
            Toast.makeText(this, item.title, Toast.LENGTH_SHORT).show()
            true
        }
        R.id.second -> {
            startActivity(Intent(this,NavigationActivity::class.java))
            true
        }
        R.id.third -> {
            Toast.makeText(this, item.title, Toast.LENGTH_SHORT).show()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }


}
