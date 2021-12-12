package com.ferechamitbeyli.keeprun.di

import android.content.Context
import androidx.room.Room
import com.ferechamitbeyli.data.local.cache.DataStoreManager
import com.ferechamitbeyli.data.local.db.DatabaseService
import com.ferechamitbeyli.data.local.entities.RunEntity
import com.ferechamitbeyli.data.local.entities.mappers.RunEntityMapper
import com.ferechamitbeyli.data.remote.entities.RunDto
import com.ferechamitbeyli.data.remote.entities.UserDto
import com.ferechamitbeyli.data.remote.entities.mappers.RunDtoMapper
import com.ferechamitbeyli.data.remote.entities.mappers.RunDtoToEntityMapper
import com.ferechamitbeyli.data.remote.entities.mappers.UserDtoMapper
import com.ferechamitbeyli.data.repositories.AuthRepositoryImpl
import com.ferechamitbeyli.data.repositories.RunRepositoryImpl
import com.ferechamitbeyli.data.repositories.SessionRepositoryImpl
import com.ferechamitbeyli.data.repositories.datasources.auth.AuthDataSourceImpl
import com.ferechamitbeyli.data.repositories.datasources.auth.AuthRemoteDBDataSourceImpl
import com.ferechamitbeyli.data.repositories.datasources.common.SessionCacheDataSourceImpl
import com.ferechamitbeyli.data.repositories.datasources.common.SessionRemoteDataSourceImpl
import com.ferechamitbeyli.data.repositories.datasources.home.RunLocalDataSourceImpl
import com.ferechamitbeyli.data.repositories.datasources.home.RunRemoteDBDataSourceImpl
import com.ferechamitbeyli.data.utils.DataConstants
import com.ferechamitbeyli.data.utils.DataConstants.FIREBASE_DB_REF
import com.ferechamitbeyli.data.utils.DataConstants.FIREBASE_STORAGE_REF
import com.ferechamitbeyli.data.utils.EntityMapper
import com.ferechamitbeyli.domain.DomainMapper
import com.ferechamitbeyli.domain.dispatchers.CoroutineDispatchers
import com.ferechamitbeyli.domain.dispatchers.CoroutineDispatchersImpl
import com.ferechamitbeyli.domain.entity.Run
import com.ferechamitbeyli.domain.entity.User
import com.ferechamitbeyli.domain.repository.AuthRepository
import com.ferechamitbeyli.domain.repository.RunRepository
import com.ferechamitbeyli.domain.repository.SessionRepository
import com.ferechamitbeyli.domain.repository.datasources.auth.AuthDataSource
import com.ferechamitbeyli.domain.repository.datasources.auth.AuthRemoteDBDataSource
import com.ferechamitbeyli.domain.repository.datasources.common.SessionCacheDataSource
import com.ferechamitbeyli.domain.repository.datasources.common.SessionRemoteDataSource
import com.ferechamitbeyli.domain.repository.datasources.home.RunLocalDataSource
import com.ferechamitbeyli.domain.repository.datasources.home.RunRemoteDataSource
import com.ferechamitbeyli.presentation.uimodels.RunUIModel
import com.ferechamitbeyli.presentation.uimodels.UserUIModel
import com.ferechamitbeyli.presentation.uimodels.mappers.RunToUIMapper
import com.ferechamitbeyli.presentation.uimodels.mappers.UserToUIMapper
import com.ferechamitbeyli.presentation.utils.helpers.NetworkConnectionTracker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
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
    fun provideAuthDataSource(): AuthDataSource =
        AuthDataSourceImpl(
            provideUserDtoMapper(),
            provideFirebaseAuth(),
            provideCoroutineDispatchers()
        )

    @Singleton
    @Provides
    fun provideAuthRemoteDBDataSource(): AuthRemoteDBDataSource =
        AuthRemoteDBDataSourceImpl(
            provideFirebaseAuth(),
            provideFirebaseDatabaseReference(),
            provideUserDtoMapper(),
            provideCoroutineDispatchers()
        )

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
            provideFirebaseDatabaseReference(),
            provideUserDtoMapper(),
            provideCoroutineDispatchers()
        )

    /** Repositories **/

    @Singleton
    @Provides
    fun provideSessionRepository(@ApplicationContext appContext: Context): SessionRepository =
        SessionRepositoryImpl(
            provideFirebaseAuth(),
            provideSessionCacheDataSource(appContext),
            provideSessionRemoteDataSource(),
            provideCoroutineDispatchers()
        )

    @Singleton
    @Provides
    fun provideAuthRepository(@ApplicationContext appContext: Context): AuthRepository =
        AuthRepositoryImpl(
            provideFirebaseAuth(),
            provideAuthDataSource(),
            provideAuthRemoteDBDataSource(),
            provideSessionRemoteDataSource(),
            provideSessionCacheDataSource(appContext),
            provideCoroutineDispatchers()
        )


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

    @Singleton
    @Provides
    fun provideRunToUIMapper(): DomainMapper<RunUIModel, Run> =
        RunToUIMapper

    @Singleton
    @Provides
    fun provideRunDtoMapper(): DomainMapper<RunDto, Run> =
        RunDtoMapper

    @Singleton
    @Provides
    fun provideRunDtoToEntityMapper(): EntityMapper<RunDto, RunEntity> =
        RunDtoToEntityMapper

    /** Data Sources **/

    @Singleton
    @Provides
    fun provideRunLocalDataSource(@ApplicationContext appContext: Context): RunLocalDataSource =
        RunLocalDataSourceImpl(
            provideRunDao(provideRunDatabase(appContext)),
            provideRunEntityMapper(),
            provideCoroutineDispatchers()
        )

    @Singleton
    @Provides
    fun provideRunRemoteDataSource(@ApplicationContext appContext: Context): RunRemoteDataSource =
        RunRemoteDBDataSourceImpl(
            provideRunDao(provideRunDatabase(appContext)),
            provideFirebaseAuth(),
            provideFirebaseDatabaseReference(),
            provideFirebaseStorageReference(),
            provideRunDtoMapper(),
            provideRunDtoToEntityMapper(),
            provideCoroutineDispatchers()
        )

    /** Repositories **/

    @Singleton
    @Provides
    fun provideRunRepository(@ApplicationContext appContext: Context): RunRepository =
        RunRepositoryImpl(
            provideRunLocalDataSource(appContext),
            provideRunRemoteDataSource(appContext),
            provideCoroutineDispatchers()
        )

    /** FusedLocationProviderClient **/

    @Singleton
    @Provides
    fun provideFusedLocationProviderClient(
        @ApplicationContext appContext: Context
    ): FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(appContext)

    /** -------------------- End of Home provide functions -------------------- **/

    /** Network Connection Tracker provide function **/


    @Provides
    @Singleton
    fun provideNetworkConnectionTracker(@ApplicationContext appContext: Context): NetworkConnectionTracker =
        NetworkConnectionTracker(appContext)


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
        DataConstants.KEEPRUN_DB_NAME
    ).build()

    @Provides
    @Singleton
    fun provideRunDao(db: DatabaseService) = db.getRunDao()

    /** Jetpack Datastore provide functions **/

    @Singleton
    @Provides
    fun provideDataStoreObject(@ApplicationContext appContext: Context): DataStoreManager =
        DataStoreManager(appContext)

    /** Firebase Realtime DB provide functions **/

    @Singleton
    @Provides
    fun provideFirebaseDatabaseReference(): DatabaseReference =
        FirebaseDatabase.getInstance(FIREBASE_DB_REF).reference

    /** Firebase Storage provide functions **/

    @Singleton
    @Provides
    fun provideFirebaseStorageReference(): StorageReference =
        FirebaseStorage.getInstance(FIREBASE_STORAGE_REF).reference
}