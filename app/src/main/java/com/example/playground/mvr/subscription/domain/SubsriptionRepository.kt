package com.example.playground.mvr.subscription.domain

interface SubsriptionRepository {

    fun subscribe()

    fun isPremiumUser(): Boolean

    suspend fun subscribeInternal()

}