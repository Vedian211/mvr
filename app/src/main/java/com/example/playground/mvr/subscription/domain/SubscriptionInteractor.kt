package com.example.playground.mvr.subscription.domain

interface SubscriptionInteractor {

    suspend fun subscribe(): SubscriptionResult
    suspend fun subscribeInternal(): SubscriptionResult

    class Base(private val repository: SubsriptionRepository): SubscriptionInteractor {

        override suspend fun subscribe() = when {
            repository.isPremiumUser() -> SubscriptionResult.Success
            else -> {
                repository.subscribe()
                SubscriptionResult.NoDataYet
            }
        }

        override suspend fun subscribeInternal(): SubscriptionResult {
            repository.subscribeInternal()
            return SubscriptionResult.Success
        }
    }
}

interface SubscriptionResult {

    interface Mapper {
        fun mapSuccess(canGoBackCallback: (Boolean) -> Unit)
    }

    fun map(mapper: Mapper, canGoBackCallback: (Boolean) -> Unit)

    object Success: SubscriptionResult {
        override fun map(mapper: Mapper, canGoBackCallback: (Boolean) -> Unit) = mapper.mapSuccess(canGoBackCallback)
    }
    object NoDataYet: SubscriptionResult {
        override fun map(mapper: Mapper, canGoBackCallback: (Boolean) -> Unit) = Unit
    }
}