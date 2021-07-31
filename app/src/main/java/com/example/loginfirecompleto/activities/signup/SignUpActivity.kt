package com.example.loginfirecompleto.activities.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.loginfirecompleto.R
import com.example.loginfirecompleto.activities.*
import com.example.loginfirecompleto.activities.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up);

        buttonGoLoginForgot.setOnClickListener{
            goToActivity<LoginActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK;
            }
            //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

        buttonForgotPassword.setOnClickListener {

            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            val confirmPassword = editTextConfirmPassword.text.toString()

            if(isValidEmail(email) && isValidPassword(password) && isValidConfirmPassword(password, confirmPassword)) {
                signUpByEmail( email, password )
            } else {
                toast( "Please fil all fields and please try again" )
            }
        }
        editTextEmail.validate {
            editTextEmail.error = if(isValidEmail(it)) null else "The email is not valid"
        }

        editTextPassword.validate {
            editTextPassword.error = if(isValidPassword(it)) null else "The password must be contains mayus, munis, numeros y simbolos especiales"
        }

        editTextConfirmPassword.validate {
            editTextConfirmPassword.error = if(isValidConfirmPassword(editTextPassword.text.toString(),it)) null else "The passwords doesn't match"
        }
    }

    private fun signUpByEmail( email: String, password: String){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                    if( task.isSuccessful ){
                        mAuth.currentUser!!.sendEmailVerification().addOnCompleteListener(this){
                            toast( "An email has been sent to you, please confirm email" )
                            goToActivity<LoginActivity> {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK;
                            }
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        }
                    } else {
                        toast( "An error occurred, please try again" )
                    }
                }
    }
}