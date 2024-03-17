package com.example.playground.mvr.subscription.data

import com.example.playground.mvr.main.UserPremiumCache
import com.example.playground.mvr.subscription.domain.SubsriptionRepository

class BaseSubscriptionRepository(
    private val foregroundServiceWrapper: ForegroundServiceWrapper,
    private val userPremiumCache: UserPremiumCache.Mutable,
    private val subscriptionCloudDataSource: SubscriptionCloudDataSource
): SubsriptionRepository {
    override fun subscribe() = foregroundServiceWrapper.start()
    override fun isPremiumUser() = userPremiumCache.isUserPremium()
    override suspend fun subscribeInternal() {
        subscriptionCloudDataSource.subscribe()
        userPremiumCache.saveUserPremium()
    }
}