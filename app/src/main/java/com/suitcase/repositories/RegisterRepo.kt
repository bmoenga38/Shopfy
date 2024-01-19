package com.suitcase.repositories

import com.google.firebase.auth.AuthResult
import com.suitcase.data.RegisterLogin
import kotlinx.coroutines.flow.Flow

interface RegisterRepo {

    suspend fun registerUser(email : String,password : String):Flow<RegisterLogin<AuthResult>>
}