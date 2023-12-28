package com.example.playground.mvr.core

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.widget.ProgressBar
import androidx.core.view.isVisible

class CustomProgressBar: ProgressBar, HideAndShow {
    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet?): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    override fun show() {
        isVisible = true
    }

    override fun hide() {
        isVisible = false
    }

    override fun onSaveInstanceState(): Parcelable? = super.onSaveInstanceState()?.let {
        val visibilityState = VisibilityState(it)
        visibilityState.visibile = visibility
        return visibilityState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val visibilityState = state as VisibilityState?
        super.onRestoreInstanceState(visibilityState?.superState)
        visibilityState?.let { visibility = it.visibile }
    }
}