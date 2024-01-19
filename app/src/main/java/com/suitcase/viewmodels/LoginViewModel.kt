package com.suitcase.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suitcase.data.LoginState
import com.suitcase.data.RegisterLogin
import com.suitcase.repositories.LoginRepo
import dagger.hilt.android.lifecycle.HiltViewModel


import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepo: LoginRepo
):ViewModel() {

    private val _loginStatus = mutableStateOf(LoginState())
    val loginStatus = _loginStatus


    fun loginUser(email : String, password : String) =
        viewModelScope.launch {
            loginRepo.LoginUser(email, password).collect{
                result -> when(result){
                    is RegisterLogin.Success ->{
                        _loginStatus.value = LoginState(isSuccess = true)
                    }
                is RegisterLogin.Error ->{
                    _loginStatus.value = LoginState(isError = "There is an error while Login In")
                }
                is RegisterLogin.Loading ->{
                    _loginStatus.value = LoginState(isLoading = true)
                }
                }
            }

        }


}