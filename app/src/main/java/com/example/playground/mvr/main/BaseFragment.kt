package com.example.playground.mvr.main

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.example.playground.mvr.core.ProvideRepresentative
import com.example.playground.mvr.core.Representative

abstract class BaseFragment<T: Representative<*>>(@LayoutRes private val layoutId: Int): Fragment(layoutId) {

    protected abstract val clazz: Class<T>
    protected lateinit var representative: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        representative = (requireActivity() as ProvideRepresentative).provideRepresentative(clazz)
    }
}