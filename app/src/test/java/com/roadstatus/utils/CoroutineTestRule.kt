package com.roadstatus.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext

@ExperimentalCoroutinesApi
class CoroutineTestRule(
    val dispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
) : BeforeAllCallback, AfterAllCallback, TestCoroutineScope by TestCoroutineScope(dispatcher) {

    val testDispatcherProvider = object : DispatcherProvider() {
        override fun default(): CoroutineDispatcher = dispatcher
        override fun io(): CoroutineDispatcher = dispatcher
        override fun main(): CoroutineDispatcher = dispatcher
    }

    fun runBlockingTest(block: suspend TestCoroutineScope.() -> Unit) {
        dispatcher.runBlockingTest {
            block()
        }
    }

    override fun beforeAll(context: ExtensionContext?) {
        Dispatchers.setMain(dispatcher)
    }

    override fun afterAll(context: ExtensionContext?) {
        Dispatchers.resetMain()
        cleanupTestCoroutines()
    }
}
