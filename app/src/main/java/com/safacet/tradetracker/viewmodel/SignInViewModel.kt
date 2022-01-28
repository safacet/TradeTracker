package com.safacet.tradetracker.viewmodel


import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.safacet.tradetracker.R
import com.safacet.tradetracker.view.ui.LoginActivity
import java.util.*

class SignInViewModel : ViewModel() {

    companion object {
        const val TAG = "SignInViewModel"
    }

    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var errorText = MutableLiveData<Int>()
    private var auth: FirebaseAuth = Firebase.auth


    fun onLoginClicked(view: View){
        if(email.value.isNullOrEmpty() || password.value.isNullOrEmpty()) {
            errorText.value = R.string.blank_login
            return
        }
        auth.signInWithEmailAndPassword(email.value!!, password.value!!)
            .addOnCompleteListener { task ->
                if(!task.isSuccessful) {
                    errorText.value = R.string.wrong_login
                } else {
                    (view.context as LoginActivity).openHomePage()
                }
            }
    }

    fun onSignUpClicked(view: View) {
        (view.context as LoginActivity).signUp()
    }
}