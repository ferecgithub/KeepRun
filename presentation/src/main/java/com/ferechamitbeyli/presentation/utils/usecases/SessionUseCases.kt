package com.ferechamitbeyli.presentation.utils.usecases

import com.ferechamitbeyli.domain.usecase.common.session_cache.*
import com.ferechamitbeyli.domain.usecase.common.session_remote.*
import javax.inject.Inject

data class SessionUseCases @Inject constructor(
    /**
     * Use cases that fetches identifier (uid) of the current user from FirebaseAuth and sign out
     */
    val getCurrentUserIdentifierUseCase: GetCurrentUserIdentifierUseCase,
    val signOutUseCase: SignOutUseCase,

    /**
     * Use cases fetches and saves data from/to Firebase Realtime Database
     */

    val getCurrentUserFromRemoteDBUseCase: GetCurrentUserFromRemoteDBUseCase,
    val getUserUidFromRemoteDBUseCase: GetUserUidFromRemoteDBUseCase,
    val getUsernameFromRemoteDBUseCaseUseCase: GetUsernameFromRemoteDBUseCase,
    val getUserEmailFromRemoteDBUseCase: GetUserEmailFromRemoteDBUseCase,
    val getUserWeightFromRemoteDBUseCase: GetUserWeightFromRemoteDBUseCase,
    val getUserNotificationStateFromRemoteDBUseCase: GetUserNotificationStateFromRemoteDBUseCase,
    val getUserPhotoUrlFromRemoteDBUseCase: GetUserPhotoUrlFromRemoteDBUseCase,

    val updateUserChangesToRemoteDBUseCase: UpdateUserChangesToRemoteDBUseCase,
    val updateUsernameToRemoteDBUseCase: UpdateUsernameToRemoteDBUseCase,
    val updateUserNotificationStateUseCase: UpdateUserNotificationStateUseCase,
    val updateUserWeightToRemoteDBUseCase: UpdateUserWeightToRemoteDBUseCase,


    /**
     * Use cases that fetches and saves data from/to Jetpack Datastore
     */
    val getFirstUseStateUseCase: GetFirstUseStateUseCase,
    val getInitialSetupStateUseCase: GetInitialSetupStateUseCase,
    val getUserUidUseCase: GetUserUidUseCase,
    val getUserEmailUseCase: GetUserEmailUseCase,
    val getUserWeightUseCase: GetUserWeightUseCase,
    val getUsernameUseCase: GetUsernameUseCase,
    val getUserPhotoUrlUseCase: GetUserPhotoUrlUseCase,
    val getUserNotificationStateUseCase: GetUserNotificationStateUseCase,

    val cacheFirstUseStateUseCase: CacheFirstUseStateUseCase,
    val cacheInitialSetupStateUseCase: CacheInitialSetupStateUseCase,
    val cacheUserAccountUseCase: CacheUserAccountUseCase,
    val cacheUserWeightUseCase: CacheUserWeightUseCase,

    val resetCachedUserUseCase: ResetCachedUserUseCase,
    val resetCachedStatesUseCase: ResetCachedStatesUseCase
)