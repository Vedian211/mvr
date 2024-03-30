package com.example.playground.mvr.subscription.screen.presentation

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.example.playground.R
import com.example.playground.mvr.core.CustomButton
import com.example.playground.mvr.subscription.progress.presentation.SubscriptionProgressBar
import com.example.playground.mvr.core.UiObserver
import com.example.playground.mvr.main.BaseFragment

class SubscriptionFragment: BaseFragment<SubscriptionRepresentative>(R.layout.fragment_subscription) {

    override val clazz = SubscriptionRepresentative::class.java
    private lateinit var observer: SubscriptionObserver
    private var progressBar: SubscriptionProgressBar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnSubscribe = view.findViewById<CustomButton>(R.id.subscribeButton)
        val btnFinish = view.findViewById<CustomButton>(R.id.finishButton)
        val restoreState = SaveAndRestoreSubscriptionUiState.Base(savedInstanceState)

        progressBar = view.findViewById(R.id.progressBar)

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    progressBar?.comeback(representative)
                }
            })

        observer = object: SubscriptionObserver {
            override fun update(data: SubscriptionUiState) {
                data.observed(representative)
                data.show(btnSubscribe, progressBar!!, btnFinish)
            }
        }

        btnSubscribe.setOnClickListener { representative.subscribe() }
        btnFinish.setOnClickListener { representative.finish() }


        representative.init(restoreState)
        progressBar?.init(restoreState.isEmpty())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        representative.saveState(SaveAndRestoreSubscriptionUiState.Base(outState))
    }

    override fun onResume() {
        super.onResume()
        representative.startGettingUpdates(observer)
        progressBar?.resume()
    }

    override fun onPause() {
        representative.stopGettingUpdates()
        progressBar?.pause()
        super.onPause()
    }

    override fun onDestroyView() {
        progressBar = null
        super.onDestroyView()
    }
}

interface SubscriptionObserver: UiObserver<SubscriptionUiState>