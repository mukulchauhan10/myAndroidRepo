package com.example.firebaseauth2.SignUpFragments


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.firebaseauth2.R
import com.example.firebaseauth2.SomeFunction.showSnackbar
import com.example.firebaseauth2.SomeFunction.showToast
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_facebook.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class FacebookFragment : Fragment(), View.OnClickListener {

    private lateinit var callbackManager: CallbackManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_facebook, container, false)
    }

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    private val loginManager by lazy{
        LoginManager.getInstance()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        callbackManager = CallbackManager.Factory.create()
        facebookLogInBtn.setOnClickListener(this)
        facebookLogOutBtn.setOnClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.i("mytag", "onActivityResult")
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.i("mytag", "handleFacebookAccessToken:$token")
        progressBar.visibility = View.VISIBLE

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) {
                if (it.isSuccessful) {
                    Log.i("mytag", "signInWithCredential:success")
                    facebookLogInBtn.apply {
                        isEnabled =false
                        setStrokeColorResource(R.color.dullStroke)
                        setTextColor(Color.GRAY)
                    }

                    facebookLogOutBtn.apply {
                        isEnabled =true
                        setStrokeColorResource(R.color.facebookStroke)
                        setTextColor(Color.BLUE)
                    }
                    val user = auth.currentUser
                    showToast("${user?.displayName} is logged in")
                } else {
                    Log.w("mytag", "signInWithCredential:failure", it.exception)
                    showSnackbar("Authentication failed")
                }
                progressBar.visibility = View.INVISIBLE
            }
    }

    fun signIn() {
        Log.i("mytag", "signIn")
        loginManager.apply {
            logInWithReadPermissions(
                this@FacebookFragment,
                Arrays.asList("email", "public_profile")
            )
            Log.i("mytag", "loginManager")

            registerCallback(callbackManager, object :
                FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    showToast("facebook:onSuccess:$loginResult")
                    Log.i("mytag", "onSuccess")
                    handleFacebookAccessToken(loginResult.accessToken)
                }

                override fun onCancel()  {
                    Log.i("mytag", "onCancle")
                    //showSnackbar("facebook:onCancel")
                }
                override fun onError(error: FacebookException) {
                    Log.i("mytag", "onError")
                    showSnackbar("facebook:onError")
                }

            })
        }

    }

    fun signOut() {
        auth.signOut()
        LoginManager.getInstance().logOut()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.facebookLogInBtn -> signIn()
            R.id.facebookLogOutBtn -> signOut()
        }
    }
}

