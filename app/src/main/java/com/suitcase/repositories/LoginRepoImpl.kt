package com.suitcase.repositories

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.suitcase.data.RegisterLogin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LoginRepoImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): LoginRepo {
    override suspend fun LoginUser(
        email: String,
        password: String
    ): Flow<RegisterLogin<AuthResult>> {
        return flow {

            try {
                emit(RegisterLogin.Loading())
                val result = firebaseAuth.signInWithEmailAndPassword(email,password)
                    .await()
                emit(RegisterLogin.Success(result))
            }
            catch (e : Exception){
                emit(RegisterLogin.Error(e.message.toString()))
            }


        }
    }
}