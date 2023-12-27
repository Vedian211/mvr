package com.example.playground.mvr.subscription

import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.playground.R
import com.example.playground.mvr.main.BaseFragment

class SubscriptionFragment: BaseFragment<SubscriptionRepresentative>(R.layout.fragment_subscription) {

    override val clazz = SubscriptionRepresentative::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.subscribeButton).setOnClickListener { representative.subscribe() }
    }
}