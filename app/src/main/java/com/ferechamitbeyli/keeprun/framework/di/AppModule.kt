package com.ferechamitbeyli.keeprun.framework.di

import android.content.Context
import androidx.room.Room
import com.ferechamitbeyli.domain.repository.AuthDataSource
import com.ferechamitbeyli.keeprun.framework.model.local.cache.DataStoreObject
import com.ferechamitbeyli.keeprun.framework.model.local.db.DatabaseService
import com.ferechamitbeyli.keeprun.framework.model.remote.firebase.FirebaseDataSource
import com.ferechamitbeyli.keeprun.framework.common.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /** Authentication provide functions **/

    @Singleton
    @Provides
    fun provideAuthenticator() : AuthDataSource
        = FirebaseDataSource()

    /*
    @Singleton
    @Provides
    fun provideAuthRepository() : BaseAuthRepository
        = AuthRepository(provideAuthenticator())

     */

    /** Retrofit provide functions **/

    @Provides
    fun providesGson(): Gson = GsonBuilder().setLenient().create()

    /** Room Database provide functions **/

    @Provides
    @Singleton
    fun provideRunDatabase(
        @ApplicationContext appContext: Context
    ) = Room.databaseBuilder(
        appContext,
        DatabaseService::class.java,
        Constants.KEEPRUN_DB_NAME
    ).build()

    @Provides
    @Singleton
    fun provideRunDao(db: DatabaseService) = db.getRunDao()

    /** Jetpack Datastore provide functions **/

    @Singleton
    @Provides
    fun provideDataStoreObject(@ApplicationContext appContext: Context): DataStoreObject
            = DataStoreObject(appContext)




}
