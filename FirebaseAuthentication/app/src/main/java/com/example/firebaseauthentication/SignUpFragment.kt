package com.example.firebaseauthentication


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.fragment_sign_up.*

/**
 * A simple [Fragment] subclass.
 */
class SignUpFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
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
                showToast("Provide Email ID")
            else if (pass.isEmpty())
                showToast("Provide Passcode")
            else if (eMail.isEmpty() && pass.isEmpty())
                showToast("Provide Email ID & Passcode")
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
                        showToast("Please connect to the Internet")
                    else
                        showToast(it.localizedMessage)
                }
        }
    }

    fun showToast(content: String) {
        Toast.makeText(requireContext(), content, Toast.LENGTH_LONG).show()
    }
}
