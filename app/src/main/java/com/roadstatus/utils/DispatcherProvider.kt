package com.roadstatus.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

open class DispatcherProvider @Inject constructor() {
    open fun main(): CoroutineDispatcher = Dispatchers.Main
    open fun default(): CoroutineDispatcher = Dispatchers.Default
    open fun io(): CoroutineDispatcher = Dispatchers.IO
}