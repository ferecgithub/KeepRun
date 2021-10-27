package com.ferechamitbeyli.presentation.utils.usecases

import com.ferechamitbeyli.domain.usecase.common.*
import javax.inject.Inject

data class SessionUseCases @Inject constructor(
    val getCurrentUserUseCase: GetCurrentUserUseCase,
    val getFirstUseStateUseCase: GetFirstUseStateUseCase,
    val getInitialSetupStateUseCase: GetInitialSetupStateUseCase,
    val getUserUidUseCase: GetUserUidUseCase,
    val getUserEmailUseCase: GetUserEmailUseCase,
    val getUsernameUseCase: GetUsernameUseCase,
    val getUserPhotoUrlUseCase: GetUserPhotoUrlUseCase,

    val cacheFirstUseStateUseCase: CacheFirstUseStateUseCase,
    val cacheInitialSetupStateUseCase: CacheInitialSetupStateUseCase,
    val cacheUserAccountUseCase: CacheUserAccountUseCase
)