package com.example.playground.mvr

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.playground.R

class MainActivity: AppCompatActivity() {

    private lateinit var representative: MainRepresentative
    private lateinit var activityCallback: ActivityCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        representative = (application as App).mainRepresentative
        val textView = findViewById<TextView>(R.id.counterTextView)
        activityCallback = object : ActivityCallback {
            override fun update(data: Int) {
                textView.setText(data)
            }
        }

        if (savedInstanceState == null) textView.text = "0"

        textView.setOnClickListener { representative.startAsync() }
    }

    override fun onResume() {
        super.onResume()
        representative.startGettingUpdates(activityCallback)
    }

    override fun onPause() {
        super.onPause()
        representative.stopGettingUpdates()
    }
}

interface ActivityCallback: UiObserver<Int>