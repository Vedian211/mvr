package com.example.playground.mvr.subscription.progress.presentation

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.widget.ProgressBar
import androidx.core.view.isVisible
import com.example.playground.mvr.core.HideAndShow
import com.example.playground.mvr.core.ProvideRepresentative
import com.example.playground.mvr.core.UiObserver
import com.example.playground.mvr.core.VisibilityState

class SubscriptionProgressBar: ProgressBar, SubscriptionProgressActions {

    private val representative: SubscriptionProgressRepresentative by lazy {
        (context.applicationContext as ProvideRepresentative).provideRepresentative(
            SubscriptionProgressRepresentative::class.java
        )
    }
    private lateinit var observer: SubscriptionProgressObserver

    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet?): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    override fun show() {
        isVisible = true
    }

    override fun hide() {
        isVisible = false
    }

    override fun init(firstRun: Boolean) {
        representative.init(firstRun)
    }

    override fun resume() {
        representative.startGettingUpdates(observer)
    }

    override fun pause() {
        representative.stopGettingUpdates()
    }

    override fun subscribe() {
        representative.subscribe()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        observer = object : SubscriptionProgressObserver {
            override fun update(data: SubscriptionProgressUiState) {
                data.show(this@SubscriptionProgressBar)
                data.observed(representative)
            }
        }

        Log.d("CustomProgressBar", "onAttachedToWindow: ")
    }



    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Log.d("CustomProgressBar", "onDetachedFromWindow: ")
    }

    override fun onSaveInstanceState(): Parcelable? = super.onSaveInstanceState()?.let {
        val state = SubscriptionProgressSavedState(it)
        state.save(this)
        representative.save(state)
        return state
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val restoredState = state as SubscriptionProgressSavedState
        super.onRestoreInstanceState(restoredState.superState)
        restoredState.restore(this)
        representative.restore(restoredState)
    }

    override fun comeback(data: ComeBack<Boolean>) = representative.comeback(data)

}

interface SubscriptionProgressActions: HideAndShow, Init, Subscribe, ComeBack<ComeBack<Boolean>> {
    fun pause()
    fun resume()
}

interface ComeBack<T> {
    fun comeback(data: T)
}

interface Init {
    fun init(firstRun: Boolean)
}

interface Subscribe {
    fun subscribe()
}

interface SubscriptionProgressObserver: UiObserver<SubscriptionProgressUiState>