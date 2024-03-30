package com.example.playground.mvr.core

import com.example.playground.mvr.dashboard.DashboardModule
import com.example.playground.mvr.dashboard.DashboardRepresentative
import com.example.playground.mvr.main.MainModule
import com.example.playground.mvr.main.MainRepresentative
import com.example.playground.mvr.subscription.common.SubscriptionDependency


@Suppress("UNCHECKED_CAST")
interface ProvideRepresentative {

    fun <T: Representative<*>> provideRepresentative(clazz: Class<T>): T

    class MakeDependency(
        private val core: Core,
        private val clear: ClearRepresentative,
        private val provideModule: ProvideModule
    ): ProvideModule {
        override fun <T : Representative<*>> module(clazz: Class<T>): Module<T> {
            return when (clazz) {
                MainRepresentative::class.java -> MainModule(core)
                DashboardRepresentative::class.java -> DashboardModule(core, clear)
                else -> provideModule.module(clazz)
            } as Module<T>
        }
    }

    class Factory(core: Core, clear: ClearRepresentative) : ProvideRepresentative, ClearRepresentative {
        private val representativeMap = mutableMapOf<Class<out Representative<*>>, Representative<*>>()
        private val makeDependency: ProvideModule = MakeDependency(
            core = core,
            clear = clear,
            provideModule = SubscriptionDependency(core, clear)
        )

        override fun <T : Representative<*>> provideRepresentative(clazz: Class<T>): T =
            if (representativeMap.containsKey(clazz)) representativeMap[clazz] as T
            else {
                val representative = makeDependency.module(clazz).representative()
                representativeMap[clazz] = representative
                representative
            }

        override fun clear(clazz: Class<out Representative<*>>) {
            representativeMap.remove(clazz)
        }
    }
}

interface ProvideModule {

    fun <T : Representative<*>> module(clazz: Class<T>): Module<T>
}