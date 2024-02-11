package com.example.playground.mvr.subscription.data

import kotlinx.coroutines.delay

interface SubscriptionCloudDataSource {

    suspend fun subscribe()

    class Base: SubscriptionCloudDataSource {
        override suspend fun subscribe() {
            delay(1000L)
        }
    }

    // we can setup result when will work with UI tests
    class MockForUiTest: SubscriptionCloudDataSource {
        override suspend fun subscribe() = Unit
    }
}