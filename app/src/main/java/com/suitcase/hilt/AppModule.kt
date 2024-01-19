package com.suitcase.hilt

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.suitcase.repositories.LoginRepo
import com.suitcase.repositories.LoginRepoImpl
import com.suitcase.repositories.RegisterRepo
import com.suitcase.repositories.RegisterRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth () = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesCurrentUser(): FirebaseUser?{
        return FirebaseAuth.getInstance().currentUser
    }

    @Provides
    @Singleton
    fun providesLoginRepository(firebaseAuth: FirebaseAuth): LoginRepo {
        return LoginRepoImpl(firebaseAuth)
    }
    @Provides
    @Singleton
    fun providesSignupRepository(firebaseAuth: FirebaseAuth) : RegisterRepo{
        return RegisterRepoImpl(firebaseAuth)
    }
    @Provides
    @Singleton
    fun providesFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance("gs://suitcase-b733b.appspot.com")
    }

    @Provides
    @Singleton
    fun provideFirebaseRealtimeDatabase():FirebaseDatabase {
        return FirebaseDatabase.getInstance("https://suitcase-b733b-default-rtdb.firebaseio.com/")
    }

}
