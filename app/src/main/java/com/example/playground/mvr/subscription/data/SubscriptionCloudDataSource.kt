package com.example.playground.mvr.subscription.data

import android.util.Log
import kotlinx.coroutines.delay

interface SubscriptionCloudDataSource {

    suspend fun subscribe()

    class Base: SubscriptionCloudDataSource {
        override suspend fun subscribe() {
            Log.d("SubscriptionCloudDataSource", "subscribe, cloud started")
            delay(10_000L)
            Log.d("SubscriptionCloudDataSource", "subscribe, cloud finished")
        }
    }

    // we can setup result when will work with UI tests
    class MockForUiTest: SubscriptionCloudDataSource {
        override suspend fun subscribe() = Unit
    }
}