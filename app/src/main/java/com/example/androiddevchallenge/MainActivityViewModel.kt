package com.example.androiddevchallenge

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import java.util.Calendar
import java.util.Timer

import kotlin.concurrent.timerTask

class MainActivityViewModel : ViewModel() {

    private val _remainingTime = MutableLiveData<Int>()
    val remainingTime get() = _remainingTime

    private val _timeExpired = MutableLiveData(false)
    val timeExpired get() = _timeExpired

    fun setTime(initialRemainingTime: Int) {
        _remainingTime.value = initialRemainingTime
    }

    fun countDown() {
        var localRemainingTime = _remainingTime.value ?: 0

        val timer = Timer()
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
}