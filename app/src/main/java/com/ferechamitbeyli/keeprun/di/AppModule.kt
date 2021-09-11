package com.ferechamitbeyli.keeprun.di

import android.content.Context
import com.ferechamitbeyli.keeprun.model.local.cache.DataStoreObject
import com.ferechamitbeyli.keeprun.model.remote.firebase.BaseAuthenticator
import com.ferechamitbeyli.keeprun.model.remote.firebase.FirebaseAuthenticator
import com.ferechamitbeyli.keeprun.model.repositories.AuthRepository
import com.ferechamitbeyli.keeprun.model.repositories.BaseAuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**All of our application dependencies shall be provided here*/

    //this means that anytime we need an authenticator Dagger will provide a Firebase authenticator.
    //in future if you want to swap out Firebase authentication for your own custom authenticator
    //you will simply come and swap here.
    @Singleton
    @Provides
    fun provideAuthenticator() : BaseAuthenticator {
        return  FirebaseAuthenticator()
    }

    //this just takes the same idea as the authenticator. If we create another repository class
    //we can simply just swap here
    @Singleton
    @Provides
    fun provideRepository() : BaseAuthRepository {
        return AuthRepository(provideAuthenticator())
    }

    @Provides
    @Singleton
    fun provideDataStoreObject(@ApplicationContext appContext: Context): DataStoreObject = DataStoreObject(appContext)
}
