package com.example.toolbar2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
}
