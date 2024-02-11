package com.example.playground.mvr.core

import com.example.playground.mvr.dashboard.DashboardModule
import com.example.playground.mvr.dashboard.DashboardRepresentative
import com.example.playground.mvr.main.MainModule
import com.example.playground.mvr.main.MainRepresentative
import com.example.playground.mvr.subscription.SubscriptionModule
import com.example.playground.mvr.subscription.presentation.SubscriptionRepresentative

@Suppress("UNCHECKED_CAST")
interface ProvideRepresentative {

    fun <T: Representative<*>> provideRepresentative(clazz: Class<T>): T

    class MakeDependency(private val core: Core, private val clear: ClearRepresentative): ProvideRepresentative {
        override fun <T : Representative<*>> provideRepresentative(clazz: Class<T>): T {
            return when (clazz) {
                MainRepresentative::class.java -> MainModule(core).representative()
                DashboardRepresentative::class.java -> DashboardModule(core, clear).representative()
                SubscriptionRepresentative::class.java -> SubscriptionModule(core, clear).representative()
                else -> throw Exception("Unknown class $clazz")
            } as T
        }
    }

    class Factory(private val makeDependency: ProvideRepresentative): ProvideRepresentative, ClearRepresentative {

        private val representativeMap = mutableMapOf<Class<out Representative<*>>, Representative<*>>()

        override fun <T : Representative<*>> provideRepresentative(clazz: Class<T>): T {
            return if (representativeMap.containsKey(clazz)) representativeMap[clazz] as T
            else {
                val representative = makeDependency.provideRepresentative(clazz)

                representativeMap[clazz] = representative
                representative
            }
        }

        override fun clear(clazz: Class<out Representative<*>>) {
            representativeMap.remove(clazz)
        }
    }
}