package com.example.loginfirecompleto.activities.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.loginfirecompleto.R
import com.example.loginfirecompleto.activities.goToActivity
import com.example.loginfirecompleto.activities.isValidEmail
import com.example.loginfirecompleto.activities.login.LoginActivity
import com.example.loginfirecompleto.activities.toast
import com.example.loginfirecompleto.activities.validate
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_forgot_password.buttonGoLoginForgot
import kotlinx.android.synthetic.main.activity_forgot_password.editTextEmail

class ForgotPasswordActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        editTextEmail.validate {
            editTextEmail.error = if(isValidEmail(it)) null else "The email is not valid"
        }

        buttonGoLoginForgot.setOnClickListener {
            goToActivity<LoginActivity>{
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK;
            }
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        buttonForgotPassword.setOnClickListener {
            val email = editTextEmail.text.toString()
            if(isValidEmail(email)){
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(this) {
                    toast("An email has been send to reset the password")
                    goToActivity<LoginActivity> {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK;
                    }
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
            } else {
                toast("An error occurred, please try again")
            }
        }
    }
}




