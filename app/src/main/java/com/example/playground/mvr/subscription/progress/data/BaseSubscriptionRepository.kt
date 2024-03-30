package com.example.playground.mvr.subscription.progress.data

import com.example.playground.mvr.main.UserPremiumCache
import com.example.playground.mvr.subscription.progress.domain.SubscriptionRepository

class BaseSubscriptionRepository(
    private val foregroundServiceWrapper: ForegroundServiceWrapper,
    private val userPremiumCache: UserPremiumCache.Mutable,
    private val subscriptionCloudDataSource: SubscriptionCloudDataSource
): SubscriptionRepository {
    override fun subscribe() = foregroundServiceWrapper.start()
    override fun isPremiumUser() = userPremiumCache.isUserPremium()
    override suspend fun subscribeInternal() {
        subscriptionCloudDataSource.subscribe()
        userPremiumCache.saveUserPremium()
    }
}