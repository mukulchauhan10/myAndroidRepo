package com.example.firebaseauth2

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            val info =
                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = String(Base64.encode(md.digest(), 0))
                Log.i("hashtag", "printHashKey() Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e("hashtag", "printHashKey()", e)
        } catch (e: Exception) {
            Log.e("hashtag", "printHashKey()", e)
        }

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
