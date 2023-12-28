package com.example.playground.mvr.subscription

import com.example.playground.mvr.core.HandleDeath
import org.junit.Before
import org.junit.Test

class SubscriptionRepresentativeTest {

    private lateinit var representative: SubscriptionRepresentative

    @Before
    fun setup() {
//        val fakeHandelDeath = FakeHandelDeath.Base()
//        representative = SubscriptionRepresentative.Base(
//            handleDeath = fakeHandelDeath
//        )
    }

    @Test
    fun test() {

    }
}

private interface FakeHandelDeath: HandleDeath {

    fun killProcess()

    class Base: FakeHandelDeath {

        private var  deathHappened = true

        override fun killProcess() {
            deathHappened = true
        }
        override fun firstOpening() {
            deathHappened = false
        }

        override fun didDeathHappen(): Boolean {
            return deathHappened
        }

        override fun deathHandled() {
            deathHappened = false
        }

    }

}