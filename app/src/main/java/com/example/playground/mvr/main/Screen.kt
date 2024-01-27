package com.example.playground.mvr.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.playground.mvr.dashboard.DashboardFragment
import com.example.playground.mvr.subscription.SubscriptionFragment

interface Screen {

    fun show(fragmentManager: FragmentManager, containerId: Int)

    fun observed(representative: MainRepresentative) = representative.observed()

    abstract class Add(private val fragmentClass: Class<out Fragment>): Screen {
        override fun show(fragmentManager: FragmentManager, containerId: Int) {
            fragmentManager.beginTransaction()
                .replace(containerId, fragmentClass.getDeclaredConstructor().newInstance())
                .addToBackStack(fragmentClass.name)
                .commit()
        }
    }

    // out Fragment - Fragment + all children(inheritors)
    abstract class Replace(private val fragmentClass: Class<out Fragment>): Screen {
        override fun show(fragmentManager: FragmentManager, containerId: Int) {
            fragmentManager.beginTransaction()
                .replace(containerId, fragmentClass.getDeclaredConstructor().newInstance())
                .commit()
        }
    }

    object Dashboard: Replace(DashboardFragment::class.java)
    object Subscription: Add(SubscriptionFragment::class.java)

    object Empty: Screen {
        override fun show(fragmentManager: FragmentManager, containerId: Int) = Unit
    }
}