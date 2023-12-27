package com.example.playground.mvr.core

import android.app.Application

class App: Application(), ProvideRepresentative, ClearRepresentative {

    private val representativeMap = mutableMapOf<Class<out Representative<*>>, Representative<*>>()
    private lateinit var factory: ProvideRepresentative.Factory

    override fun onCreate() {
        super.onCreate()
        val core = Core.Base(this)
        factory = ProvideRepresentative.Factory(core = core, clear = this)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Representative<*>> provideRepresentative(clazz: Class<T>): T {
        return if (representativeMap.containsKey(clazz)) representativeMap[clazz] as T
        else {
            val representative = factory.provideRepresentative(clazz)

            representativeMap[clazz] = representative
            representative
        }
    }

    override fun clear(clazz: Class<out Representative<*>>) {
        representativeMap.remove(clazz)
    }
}