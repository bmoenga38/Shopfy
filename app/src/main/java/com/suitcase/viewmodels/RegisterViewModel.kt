package com.suitcase.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suitcase.data.LoginState
import com.suitcase.data.RegisterLogin
import com.suitcase.repositories.RegisterRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerRepo: RegisterRepo
) : ViewModel() {

    private val _registerStatus = mutableStateOf(LoginState())
    val registerStatus = _registerStatus

    fun registerUser(email: String, password: String) =
        viewModelScope.launch {
            registerRepo.registerUser(email, password).collect {
                    result -> when(result){
                is RegisterLogin.Success ->{
                    _registerStatus.value = LoginState(isSuccess = true)
                }
                is RegisterLogin.Loading ->{
                    _registerStatus.value = LoginState(isLoading = true)
                }
                is RegisterLogin.Error ->{
                    _registerStatus.value = LoginState(isError = result.message.toString())
                }
            }


            }
        }


}