package com.example.playground.mvr.core

import android.os.Parcel
import android.os.Parcelable
import android.view.View

class VisibilityState: View.BaseSavedState {

    private var visibile: Int = View.VISIBLE

    fun save(view: View) {
        visibile = view.visibility
    }

    fun restore(view: View) {
        view.visibility = visibile
    }

    constructor(superState: Parcelable): super(superState)

    private constructor(parcelIn: Parcel): super(parcelIn) {
        visibile = parcelIn.readInt()
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
        out.writeInt(visibile)
    }

    override fun describeContents() = 0

    companion object CREATOR: Parcelable.Creator<VisibilityState> {
        override fun createFromParcel(source: Parcel) = VisibilityState(source)
        override fun newArray(size: Int): Array<VisibilityState?> = arrayOfNulls(size)
    }
}