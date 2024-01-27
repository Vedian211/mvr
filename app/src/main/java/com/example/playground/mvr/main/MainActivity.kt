package com.example.playground.mvr.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playground.R
import com.example.playground.mvr.core.App
import com.example.playground.mvr.core.ProvideRepresentative
import com.example.playground.mvr.core.Representative
import com.example.playground.mvr.core.UiObserver

class MainActivity: AppCompatActivity(), ProvideRepresentative {

    private lateinit var representative: MainRepresentative
    private lateinit var activityCallback: ActivityCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        representative = provideRepresentative(MainRepresentative::class.java)
        activityCallback = object : ActivityCallback {
            override fun update(data: Screen) = runOnUiThread {
                data.show(supportFragmentManager, R.id.container)
                data.observed(representative)
            }
        }

        // no need to show dashboard if we rotate a phone, etc
        representative.showDashboard(savedInstanceState == null)
    }

    override fun onResume() {
        super.onResume()
        representative.startGettingUpdates(activityCallback)
    }

    override fun onPause() {
        super.onPause()
        representative.stopGettingUpdates()
    }

    override fun <T : Representative<*>> provideRepresentative(clazz: Class<T>): T {
        return (application as ProvideRepresentative).provideRepresentative(clazz)
    }
}

interface ActivityCallback: UiObserver<Screen>