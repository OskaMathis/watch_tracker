package com.oskamathis.watchtracker

import android.os.Bundle
import android.os.SystemClock
import android.support.wearable.activity.WearableActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : WearableActivity() {
    lateinit var startTime: Date
    private var elapsedTime: Long = 0
    private var isTracking: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initText()

        button.setOnClickListener {
            button.text = if (isTracking) "START" else "END"
            onButtonClicked()
            isTracking = !isTracking
        }

        setAmbientEnabled()
    }

    private fun initText() {
        button.text = "START"
        total_time.text = "00:00:00"
        timer.text = "00:00"
    }


    private fun onButtonClicked() {
        if (isTracking) {
            elapsedTime += Date().time - startTime.time
            total_time.text = milliSecFormatter(elapsedTime)
            timer.stop()
        } else {
            startTime = Date()
            timer.base = SystemClock.elapsedRealtime()
            timer.start()
        }

    }

    private fun milliSecFormatter(ms: Long): String {
        val timeDifference = ms / 1000
        val h = (timeDifference / (3600)).toInt()
        val m = ((timeDifference - (h * 3600)) / 60).toInt()
        val s = (timeDifference - (h * 3600) - m * 60).toInt()

        return String.format("%02d:%02d:%02d", h, m, s)
    }
}
