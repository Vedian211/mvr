package com.example.playground.mvr.subscription.domain

interface SubscriptionInteractor {

    suspend fun subscribe()

    class Base(private val repository: SubsriptionRepository): SubscriptionInteractor {

        override suspend fun subscribe() {
            repository.subscribe()
        }
    }
}