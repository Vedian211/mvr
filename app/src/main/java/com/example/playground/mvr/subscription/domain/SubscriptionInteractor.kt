package com.example.playground.mvr.subscription.domain

import com.example.playground.mvr.main.UserPremiumCache

interface SubscriptionInteractor {

    fun subscribe(callback: () -> Unit)

    class Base(private val userPremiumCache: UserPremiumCache.Save): SubscriptionInteractor {

        override fun subscribe(callback: () -> Unit) {
            Thread {
                Thread.sleep(10_000)
                userPremiumCache.saveUserPremium()
                callback.invoke()
            }.start()
        }
    }
}