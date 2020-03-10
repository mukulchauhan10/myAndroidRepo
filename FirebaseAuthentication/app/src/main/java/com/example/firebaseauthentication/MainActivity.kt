package com.example.firebaseauthentication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewPageAdapter =  ViewPageAdapter(supportFragmentManager)
        this.viewPager.adapter = viewPageAdapter
        this.tabLayout1.setupWithViewPager(this.viewPager)
    }
}
