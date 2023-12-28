package com.example.playground.mvr.dashboard

import android.os.Bundle
import android.view.View
import com.example.playground.R
import com.example.playground.mvr.core.CustomButton
import com.example.playground.mvr.core.CustomTextView
import com.example.playground.mvr.core.UiObserver
import com.example.playground.mvr.main.BaseFragment

class DashboardFragment: BaseFragment<DashboardRepresentative>(R.layout.fragment_dashboard) {

    private lateinit var callback: DashboardObserver
    override val clazz = DashboardRepresentative::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button = view.findViewById<CustomButton>(R.id.playButton)
        val textView = view.findViewById<CustomTextView>(R.id.showPlayingTextView)

        callback = object : DashboardObserver {
            override fun update(data: PremiumDashboardUiState) {
                data.show(button, textView)
            }
        }

        button.setOnClickListener { representative.play() }
    }

    override fun onResume() {
        super.onResume()
        representative.startGettingUpdates(callback)
        println("Dashboard, onResume")
    }

    override fun onPause() {
        super.onPause()
        representative.stopGettingUpdates()
        println("Dashboard, onPause")
    }
}

interface DashboardObserver: UiObserver<PremiumDashboardUiState> {
    override fun isEmpty() = false
}