package com.example.playground.mvr.core

import android.os.Build
import android.os.Bundle
import android.util.Log
import java.io.Serializable

interface SaveAndRestoreState {

    interface Save<T: Serializable> {
        fun save(data: T)
    }

    interface Restore<T: Serializable>: IsEmpty {
        fun restore(): T
    }

    interface Mutable<T: Serializable>: Save<T>, Restore<T>

    abstract class Abstract<T: Serializable>(
        private val bundle: Bundle?,
        private val key: String,
        private val clazz: Class<T>
    ): Mutable<T> {
        override fun save(data: T) {
            bundle!!.putSerializable(key, data)
        }

        @Suppress("UNCHECKED_CAST")
        override fun restore(): T {
            val isTiramisu = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
            Log.d("MVR", "restore: isTiramisu = $isTiramisu")
            val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle!!.getSerializable(key, clazz)!!
            } else {
                bundle!!.getSerializable(key) as T
            }

            Log.d("MVR", "restore: data = ${data::class.java}")

            return data
        }

        override fun isEmpty() = bundle == null
    }
}