
package com.example.loginfirecompleto.activities.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.loginfirecompleto.R
import com.example.loginfirecompleto.activities.*
import com.example.loginfirecompleto.activities.signup.ForgotPasswordActivity
import com.example.loginfirecompleto.activities.signup.SignUpActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.buttonForgotPassword
import kotlinx.android.synthetic.main.activity_login.editTextEmail
import kotlinx.android.synthetic.main.activity_login.editTextPassword


class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val mGoogleApiClient: GoogleApiClient by lazy { getGoogleApiClient() }
    private val RC_GOOGLE_SING_IN = 99

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        buttonForgotPassword.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            if(isValidEmail(email) && isValidPassword(password) ){
                logInByEmail(email, password)
            }
        }

        textViewForgotPassword.setOnClickListener {
            goToActivity<ForgotPasswordActivity>()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        buttonCreateAccount.setOnClickListener {
            goToActivity<SignUpActivity>()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        buttonLogInGoogle.setOnClickListener {
            val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
            startActivityForResult(signInIntent, RC_GOOGLE_SING_IN)
        }

        editTextEmail.validate {
            editTextEmail.error = if(isValidEmail(it)) null else "The email is not valid"
        }

        editTextPassword.validate {
            editTextPassword.error = if(isValidPassword(it)) null else "The password must be contains mayus, munis, numeros y simbolos especiales"
        }

    }

    private fun getGoogleApiClient(): GoogleApiClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        return GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }

    private fun logInByEmail( email:String, password: String ){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if( task.isSuccessful ){
                if( mAuth.currentUser!!.isEmailVerified ){
                    toast( "User is now logged in" )
                } else {
                    toast( "You must be confirm email first" )
                }
            } else {
                toast( "An error occurred, please try again" )
            }
        }
    }

    private fun logginByGoogleAccountIntoFirebase( googleAccount: GoogleSignInAccount ) {
        val credential = GoogleAuthProvider.getCredential( googleAccount.idToken, null )
        mAuth.signInWithCredential( credential ).addOnCompleteListener(this) {
            toast("Sign In by Google successfully")
            if( mGoogleApiClient.isConnected ){
                Auth.GoogleSignInApi.signOut( mGoogleApiClient )
            }
            goToActivity<MainActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK;
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if( requestCode == RC_GOOGLE_SING_IN ) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)

            if( result.isSuccess ){
                val account = result.signInAccount
                logginByGoogleAccountIntoFirebase( account!! )
            }
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        toast("Connection failed...")
    }
}






