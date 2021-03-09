package com.example.androiddevchallenge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import java.util.Calendar
import java.util.Timer

import kotlin.concurrent.timerTask

class MainActivityViewModel : ViewModel() {

    private var _timer: Timer? = null

    private val _remainingTime = MutableLiveData(0)
    val remainingTime: LiveData<Int> get() = _remainingTime

    private val _timerState = MutableLiveData(TimerState.Stopped)
    val timerState: LiveData<TimerState> get() = _timerState

    private val _entryError = MutableLiveData(false)
    val entryError: LiveData<Boolean> get() = _entryError

    fun start(initialRemainingTime: String) {
        if (initialRemainingTime.isBlank()) {
            _entryError.value = true
            return
        } else {
            _entryError.value = false
        }

        this.countDownTimer(initialRemainingTime.toInt())
    }

    fun resume() {
        val remainingTime = _remainingTime.value ?: 0
        countDownTimer(remainingTime)
    }

    fun pause() {
        _timerState.value = TimerState.Paused
        stopTimer()
    }

    fun stop() {
        _timerState.value = TimerState.Stopped
        stopTimer()
    }

    private fun countDownTimer(initialRemainingTime: Int) {
        _timer = Timer()
        _timerState.value = TimerState.Running
        _remainingTime.value = initialRemainingTime
        var localRemainingTime = _remainingTime.value ?: 0

        _timer?.scheduleAtFixedRate(
            timerTask {
                if (localRemainingTime > 0) {
                    --localRemainingTime
                    _remainingTime.postValue(localRemainingTime)
                } else {
                    _timer?.cancel()
                    _timerState.postValue(TimerState.Stopped)
                }
            },
            Calendar.getInstance().time,
            1000
        )
    }

    private fun stopTimer() {
        _timer?.cancel()
        _timer = null
    }
}

enum class TimerState {
    Running, Paused, Stopped
}