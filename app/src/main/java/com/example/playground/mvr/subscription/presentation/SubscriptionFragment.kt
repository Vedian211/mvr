package com.example.playground.mvr.subscription.presentation

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.example.playground.R
import com.example.playground.mvr.core.CustomButton
import com.example.playground.mvr.core.CustomProgressBar
import com.example.playground.mvr.core.UiObserver
import com.example.playground.mvr.main.BaseFragment
import com.example.playground.mvr.subscription.SaveAndRestoreSubscriptionUiState

class SubscriptionFragment: BaseFragment<SubscriptionRepresentative>(R.layout.fragment_subscription) {

    override val clazz = SubscriptionRepresentative::class.java
    private lateinit var observer: SubscriptionObserver

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnSubscribe = view.findViewById<CustomButton>(R.id.subscribeButton)
        val btnFinish = view.findViewById<CustomButton>(R.id.finishButton)
        val pb = view.findViewById<CustomProgressBar>(R.id.progressBar)

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    representative.comeback()
                }
            })

        observer = object: SubscriptionObserver {
            override fun update(data: SubscriptionUiState) {
                data.observed(representative)
                data.show(btnSubscribe, pb, btnFinish)
            }
        }

        btnSubscribe.setOnClickListener { representative.subscribe() }
        btnFinish.setOnClickListener { representative.finish() }

        representative.init(SaveAndRestoreSubscriptionUiState.Base(savedInstanceState))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        representative.saveState(SaveAndRestoreSubscriptionUiState.Base(outState))
    }

    override fun onResume() {
        super.onResume()
        representative.startGettingUpdates(observer)
    }

    override fun onPause() {
        super.onPause()
        representative.stopGettingUpdates()
    }
}

interface SubscriptionObserver: UiObserver<SubscriptionUiState>