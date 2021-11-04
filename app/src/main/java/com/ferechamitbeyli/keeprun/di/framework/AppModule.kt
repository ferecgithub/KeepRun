package com.ferechamitbeyli.keeprun.di.framework

import android.content.Context
import androidx.room.Room
import com.ferechamitbeyli.data.local.cache.DataStoreObject
import com.ferechamitbeyli.data.local.db.DatabaseService
import com.ferechamitbeyli.data.local.entities.RunEntity
import com.ferechamitbeyli.data.local.entities.mappers.RunEntityMapper
import com.ferechamitbeyli.data.remote.entities.UserDto
import com.ferechamitbeyli.data.remote.entities.mappers.UserDtoMapper
import com.ferechamitbeyli.data.repositories.AuthRepositoryImpl
import com.ferechamitbeyli.data.repositories.SessionRepositoryImpl
import com.ferechamitbeyli.data.repositories.datasources.auth.AuthDataSourceImpl
import com.ferechamitbeyli.data.repositories.datasources.auth.AuthRemoteDBDataSourceImpl
import com.ferechamitbeyli.data.repositories.datasources.common.SessionCacheDataSourceImpl
import com.ferechamitbeyli.data.repositories.datasources.common.SessionRemoteDataSourceImpl
import com.ferechamitbeyli.data.utils.Constants
import com.ferechamitbeyli.domain.DomainMapper
import com.ferechamitbeyli.domain.dispatchers.CoroutineDispatchers
import com.ferechamitbeyli.domain.dispatchers.CoroutineDispatchersImpl
import com.ferechamitbeyli.domain.entity.Run
import com.ferechamitbeyli.domain.entity.User
import com.ferechamitbeyli.domain.repository.AuthRepository
import com.ferechamitbeyli.domain.repository.SessionRepository
import com.ferechamitbeyli.domain.repository.datasources.auth.AuthDataSource
import com.ferechamitbeyli.domain.repository.datasources.auth.AuthRemoteDBDataSource
import com.ferechamitbeyli.domain.repository.datasources.common.SessionCacheDataSource
import com.ferechamitbeyli.domain.repository.datasources.common.SessionRemoteDataSource
import com.ferechamitbeyli.presentation.uimodels.UserUIModel
import com.ferechamitbeyli.presentation.uimodels.mappers.UserToUIMapper
import com.ferechamitbeyli.presentation.utils.helpers.NetworkConnectionTracker
import com.google.firebase.auth.FirebaseAuth
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
class AppModule {

    /** Coroutine Dispatchers **/

    @Singleton
    @Provides
    fun provideCoroutineDispatchers(): CoroutineDispatchers = CoroutineDispatchersImpl()

    /** -------------------- Authentication provide functions -------------------- **/

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    /** Mappers **/

    @Singleton
    @Provides
    fun provideUserDtoMapper(): DomainMapper<UserDto, User> =
        UserDtoMapper

    /** Data Sources **/

    @Singleton
    @Provides
    fun provideAuthDataSource(@ApplicationContext appContext: Context): AuthDataSource = AuthDataSourceImpl(
        provideSessionCacheDataSource(appContext),
        provideAuthRemoteDBDataSource(),
        provideUserDtoMapper(),
        provideFirebaseAuth(),
        provideCoroutineDispatchers()
    )

    @Singleton
    @Provides
    fun provideAuthRemoteDBDataSource(): AuthRemoteDBDataSource =
        AuthRemoteDBDataSourceImpl(provideFirebaseAuth(), provideCoroutineDispatchers())

    @Singleton
    @Provides
    fun provideSessionCacheDataSource(@ApplicationContext appContext: Context): SessionCacheDataSource =
        SessionCacheDataSourceImpl(
            provideDataStoreObject(appContext),
            provideCoroutineDispatchers()
        )

    @Singleton
    @Provides
    fun provideSessionRemoteDataSource(): SessionRemoteDataSource =
        SessionRemoteDataSourceImpl(
            provideFirebaseAuth(),
            provideUserDtoMapper(),
            provideCoroutineDispatchers()
        )

    /** Repositories **/

    @Singleton
    @Provides
    fun provideSessionRepository(@ApplicationContext appContext: Context): SessionRepository =
        SessionRepositoryImpl(
            provideSessionCacheDataSource(appContext),
            provideSessionRemoteDataSource()
        )

    @Singleton
    @Provides
    fun provideAuthRepository(@ApplicationContext appContext: Context): AuthRepository =
        AuthRepositoryImpl(provideAuthDataSource(appContext), provideAuthRemoteDBDataSource())


    /** -------------------- End of Authentication provide functions -------------------- **/

    /** -------------------- Home provide functions -------------------- **/

    /** Mappers **/

    @Singleton
    @Provides
    fun provideRunEntityMapper(): DomainMapper<RunEntity, Run> =
        RunEntityMapper

    @Singleton
    @Provides
    fun provideUserToUIMapper(): DomainMapper<UserUIModel, User> =
        UserToUIMapper

    /** Data Sources **/

    /** Repositories **/


    /** -------------------- End of Home provide functions -------------------- **/

    /** Network Connection Tracker provide function **/


    @Provides
    @Singleton
    fun provideNetworkConnectionTracker(@ApplicationContext appContext: Context): NetworkConnectionTracker = NetworkConnectionTracker(appContext)



    /** Retrofit provide functions **/

    @Provides
    @Singleton
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
    fun provideDataStoreObject(@ApplicationContext appContext: Context): DataStoreObject =
        DataStoreObject(appContext)


}