package com.example.playground.mvr.subscription.data

import com.example.playground.mvr.main.UserPremiumCache
import com.example.playground.mvr.subscription.domain.SubsriptionRepository

class BaseSubscriptionRepository(
    private val userPremiumCache: UserPremiumCache.Save,
    private val subscriptionCloudDataSource: SubscriptionCloudDataSource
): SubsriptionRepository {
    override suspend fun subscribe() {
        subscriptionCloudDataSource.subscribe()
        userPremiumCache.saveUserPremium()
    }
}