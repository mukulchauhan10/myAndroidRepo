package com.example.firebaseauth2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.firebaseauth2.SignUpFragments.EmailAndPassFragment
import com.example.firebaseauth2.SignUpFragments.FacebookFragment
import com.example.firebaseauth2.SignUpFragments.GoogleFragment
import com.example.firebaseauth2.SignUpFragments.PhoneFragment
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity(), View.OnClickListener {
    val fragmentManager: FragmentManager = supportFragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        eMailAndPassBtn.setOnClickListener(this)
        phoneSignUpBtn.setOnClickListener(this)
        googleSignUpBtn.setOnClickListener(this)
        faceboolSignUpBtn.setOnClickListener(this)

    }

    /*fun eMailAndPassBtn(view: View) {
        Log.i("status1", "email")
        val fragmentManager: FragmentManager = supportFragmentManager
        Log.i("status1", "email1")
        val transaction: FragmentTransaction = fragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout, EmailAndPassFragment(), "fragment1")
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            //addToBackStack(null)
            commit()
        }
    }

    fun phoneBtn(view: View) {
        Log.i("status1", "phone")
        val fragmentManager: FragmentManager = supportFragmentManager
        Log.i("status1", "phone1")
        fragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout, PhoneFragment(), "fragment2")
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            commit()
        }
    }*/

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.eMailAndPassBtn -> {
                val transaction: FragmentTransaction = fragmentManager.beginTransaction().apply {
                    replace(R.id.frameLayout, EmailAndPassFragment(), "fragment1")
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    //addToBackStack(null)
                    commit()
                }
            }
            R.id.phoneSignUpBtn -> {
                fragmentManager.beginTransaction().apply {
                    replace(R.id.frameLayout, PhoneFragment(), "fragment2")
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    commit()
                }
            }
            R.id.googleSignUpBtn -> {
                fragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, GoogleFragment(), "fragment3")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
            R.id.faceboolSignUpBtn -> {
                fragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, FacebookFragment(), "fragment4")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
        }
    }
}
