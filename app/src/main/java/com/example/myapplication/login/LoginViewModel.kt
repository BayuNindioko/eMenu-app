package com.example.myapplication.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> = _loginResult
    private val _showProgressBar = MutableLiveData<Boolean>()
    val showProgressBar: LiveData<Boolean> = _showProgressBar

    fun signInWithEmailAndPassword(email: String, password: String) {
        _showProgressBar.value = true

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _showProgressBar.value = false
                if (task.isSuccessful) {
                    _loginResult.value = true
                } else {
                    _loginResult.value = false
                    Log.e("LoginViewModel", "Login failed", task.exception)
                }
            }

    }

}