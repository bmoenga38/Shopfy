package com.suitcase.repositories

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.suitcase.data.RegisterLogin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RegisterRepoImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : RegisterRepo {
    override suspend fun registerUser(
        email: String,
        password: String
    ): Flow<RegisterLogin<AuthResult>> {
        return flow {
            emit(RegisterLogin.Loading())
            try {
                val result = firebaseAuth.createUserWithEmailAndPassword(email,password)
                    .await()
                emit(RegisterLogin.Success(result))
            }
            catch (e: Exception){
                emit(RegisterLogin.Error(e.message.toString()))
            }
        }
    }
}