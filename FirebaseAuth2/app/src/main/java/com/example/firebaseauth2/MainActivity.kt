package com.example.firebaseauth2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerHereTextView.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
        }
        signInBtn.setOnClickListener {
            val eMail = eMailEditText.text.toString()
            val pass = passwordEditText.text.toString()
            signIn(eMail, pass)
        }
    }

    val auth by lazy {
        FirebaseAuth.getInstance()
    }

    fun signIn(eMail: String, pass: String) {
        if (eMail.isEmpty() || pass.isEmpty()) {
            if (eMail.isEmpty())
                showToast("Provide Email ID")
            else
                showToast("Provide Passcode")
        } else {
            auth.signInWithEmailAndPassword(eMail, pass)
                .addOnCompleteListener {
                    val name = getUserName()
                    showToast("${name} is logged in")
                }.addOnFailureListener {
                    if (it.localizedMessage.contains("network error"))
                        showToast("Please connect to the Internet")
                    else
                        showToast(it.localizedMessage)
                }
        }
    }

    fun getUserName(): String {
        val user = auth.currentUser
        return user?.let {
            if (user.displayName.toString().isEmpty())
                "Anonymous"
            else
                user.displayName
        } as String
    }

    fun showToast(content: String?) {
        Toast.makeText(this, content, Toast.LENGTH_LONG).show()
    }
}
