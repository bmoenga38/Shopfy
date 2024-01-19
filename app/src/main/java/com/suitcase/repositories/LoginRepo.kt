package com.suitcase.repositories

import com.google.firebase.auth.AuthResult
import com.suitcase.data.RegisterLogin
import kotlinx.coroutines.flow.Flow

interface LoginRepo {

    suspend fun LoginUser(email : String, password : String) : Flow<RegisterLogin<AuthResult>>

}