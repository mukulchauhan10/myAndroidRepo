package com.example.firebaseauthentication


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_sign_in.*

/**
 * A simple [Fragment] subclass.
 */
class SignInFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
            else if (pass.isEmpty())
                showToast("Provide Passcode")
            else if (eMail.isEmpty() && pass.isEmpty())
                showToast("Provide Email ID & Passcode")
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
        Toast.makeText(requireContext(), content, Toast.LENGTH_LONG).show()
    }

}
