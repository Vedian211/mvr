package com.example.playground.mvr.subscription.progress.domain

interface SubscriptionRepository {

    fun subscribe()

    fun isPremiumUser(): Boolean

    suspend fun subscribeInternal()

}