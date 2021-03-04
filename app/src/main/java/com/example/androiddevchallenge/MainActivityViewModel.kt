package com.example.androiddevchallenge

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import java.util.Calendar
import java.util.Timer

import kotlin.concurrent.timerTask

class MainActivityViewModel : ViewModel() {

    private val timer: Timer = Timer()

    private val _timerRunning = MutableLiveData(false)
    val timerRunning get() = _timerRunning

    private val _remainingTime = MutableLiveData(0)
    val remainingTime get() = _remainingTime

    private val _timeExpired = MutableLiveData(false)
    val timeExpired get() = _timeExpired

    fun countDown(initialRemainingTime: Int) {
        _timerRunning.value = true
        _remainingTime.value = initialRemainingTime
        var localRemainingTime = _remainingTime.value ?: 0

        timer.scheduleAtFixedRate(
            timerTask {
                if (localRemainingTime > 0) {
                    --localRemainingTime
                    _remainingTime.value = localRemainingTime

                    _timeExpired.value = localRemainingTime <= 0
                }
            },
            Calendar.getInstance().time,
            1000
        )
    }

    fun stopTimer() {
        timer.cancel()
        _timerRunning.value = false
    }
}