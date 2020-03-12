package com.example.firebaseauth2.SignUpFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.firebaseauth2.R
import com.example.firebaseauth2.SomeFunction.showSnackbar
import com.example.firebaseauth2.SomeFunction.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.fragment_email_and_pass.*

class EmailAndPassFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_email_and_pass, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        signUpBtn.setOnClickListener {
            val eMail = eMailEditText.text.toString()
            val pass = passwordEditText.text.toString()
            signUp(eMail, pass)
        }
    }

    val auth by lazy {
        FirebaseAuth.getInstance()
    }

    fun signUp(eMail: String, pass: String) {
        if (eMail.isEmpty() || pass.isEmpty()) {
            if (eMail.isEmpty())
                showSnackbar("Provide Email ID")
            else
                showSnackbar("Provide Passcode")
        } else {
            auth.createUserWithEmailAndPassword(eMail, pass)
                .addOnCompleteListener {
                    val user = auth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(userNameEditText.text.toString())
                        .build()
                    user?.updateProfile(profileUpdates)
                        ?.addOnSuccessListener {
                            val name = if (user?.displayName.toString().isEmpty())
                                "Anonymous"
                            else
                                user?.displayName
                            showToast("${name} is registered")
                        }
                }.addOnFailureListener {
                    if (it.localizedMessage.contains("network error"))
                        showSnackbar("Please connect to the Internet")
                    else
                        showSnackbar(it.localizedMessage)
                }
        }
    }
}
