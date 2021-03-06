package com.example.androiddevchallenge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import java.util.Calendar
import java.util.Timer

import kotlin.concurrent.timerTask

class MainActivityViewModel : ViewModel() {

    private val timer: Timer = Timer()

    private val _timerRunning = MutableLiveData(false)
    val timerRunning: LiveData<Boolean> get() = _timerRunning

    private val _remainingTime = MutableLiveData(0)
    val remainingTime: LiveData<Int> get() = _remainingTime

    private val _timeExpired = MutableLiveData(false)
    val timeExpired: LiveData<Boolean> get() = _timeExpired

    fun countDown(initialRemainingTime: Int) {
        _timerRunning.value = true
        _remainingTime.value = initialRemainingTime
        var localRemainingTime = _remainingTime.value ?: 0

        timer.scheduleAtFixedRate(
            timerTask {
                if (localRemainingTime > 0) {
                    --localRemainingTime
                    _remainingTime.postValue(localRemainingTime)
                    _timeExpired.postValue(localRemainingTime <= 0)
                } else {
                    _timerRunning.postValue(false)
                    _timeExpired.postValue(true)
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