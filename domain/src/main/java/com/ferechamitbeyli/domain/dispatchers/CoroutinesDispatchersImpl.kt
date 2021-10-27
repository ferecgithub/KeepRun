package com.ferechamitbeyli.domain.dispatchers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class CoroutineDispatchersImpl @Inject constructor() : CoroutineDispatchers {
    override fun ui(): CoroutineDispatcher = Dispatchers.Main.immediate
    override fun io(): CoroutineDispatcher = Dispatchers.IO
    override fun default(): CoroutineDispatcher = Dispatchers.Default
}