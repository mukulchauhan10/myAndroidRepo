package com.example.firebaseauth2.SignUpFragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.firebaseauth2.R
import com.example.firebaseauth2.SomeFunction.showSnackbar
import com.example.firebaseauth2.SomeFunction.showToast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_google.*

/**
 * A simple [Fragment] subclass.
 */
class GoogleFragment : Fragment() {

    private lateinit var googleSignInClient: GoogleSignInClient // to use GoogleSignInClient class import a dependency manaully of google play
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_google, container, false)
    }

    val auth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val googleSignInOptions = GoogleSignInOptions
            .Builder()
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignInOptions)
        googleRegisterBtn.setOnClickListener {
            signIn()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null)
                    firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                showSnackbar("Google sign in failed")
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {


        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    progressBar.visibility = View.VISIBLE
                    val user = auth.currentUser
                    showToast("${user?.displayName} is logged in")
                } else {
                    if (it.exception?.localizedMessage!!.contains("network error", true))
                        showSnackbar("Please connect to the Internet")
                    else
                        showSnackbar("Authentication failed")
                }
                progressBar.visibility = View.GONE
            }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}
