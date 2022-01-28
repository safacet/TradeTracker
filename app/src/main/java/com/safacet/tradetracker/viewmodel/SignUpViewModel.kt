package com.safacet.tradetracker.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.safacet.tradetracker.R
import com.safacet.tradetracker.view.ui.LoginActivity

class SignUpViewModel : ViewModel() {

    var email = MutableLiveData<String>()
    var password1 = MutableLiveData<String>()
    var password2 = MutableLiveData<String>()
    var errorText = MutableLiveData<Int>()

    fun onSignUpClicked(view: View) {
        if(email.value.isNullOrEmpty() || password1.value.isNullOrEmpty() || password2.value.isNullOrEmpty()) {
            errorText.value = R.string.blank_sign_up
            return
        }
        if(password1.value != password2.value) {
            errorText.value = R.string.password_dont_match
            return
        }
        if(password1.value!!.length <6) {
            errorText.value = R.string.short_password
            return
        }
        val auth = Firebase.auth

        auth.createUserWithEmailAndPassword(email.value!!, password1.value!!)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    (view.context as LoginActivity).openHomePage()
                } else {
                    errorText.value = R.string.sign_up_error
                }
            }
    }

    fun onBackButtonClicked(view: View) {
        (view.context as LoginActivity).onBackButtonClicked()
    }
}