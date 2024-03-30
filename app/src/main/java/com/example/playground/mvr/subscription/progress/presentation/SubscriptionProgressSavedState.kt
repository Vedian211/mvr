package com.example.playground.mvr.subscription.progress.presentation

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.view.View

class SubscriptionProgressSavedState : View.BaseSavedState, SaveAndRestoreSubscriptionUiState.Mutable {

    private var visible: Int = View.VISIBLE
    private var state: SubscriptionProgressUiState = SubscriptionProgressUiState.Empty

    constructor(superState: Parcelable) : super(superState)

    private constructor(parcelIn: Parcel) : super(parcelIn) {
        visible = parcelIn.readInt()
        state = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            parcelIn.readSerializable(null, SubscriptionProgressUiState::class.java)!!
        } else {
            parcelIn.readSerializable() as SubscriptionProgressUiState
        }
    }

    fun save(view: View) {
        visible = view.visibility
    }

    fun restore(view: View) {
        view.visibility = visible
    }

    override fun save(state: SubscriptionProgressUiState) {
        this.state = state
    }

    override fun restore(): SubscriptionProgressUiState {
        return state
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
        out.writeInt(visible)
        out.writeSerializable(state)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<SubscriptionProgressSavedState> {
        override fun createFromParcel(parcel: Parcel): SubscriptionProgressSavedState = SubscriptionProgressSavedState(parcel)
        override fun newArray(size: Int): Array<SubscriptionProgressSavedState?> = arrayOfNulls(size)
    }
}

interface SaveAndRestoreSubscriptionUiState {

    interface Save {
        fun save(state: SubscriptionProgressUiState)
    }

    interface Restore {
        fun restore(): SubscriptionProgressUiState
    }

    interface Mutable : Save, Restore
}