package com.example.androiddevchallenge

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    val _remainingTime = MutableLiveData<Int>()
    val remainingTime get() = _remainingTime

    val _timeExpired = MutableLiveData(false)
    val timeExpired get() = _timeExpired

    fun setTime(initialRemainingTime: Int) {
        _remainingTime.value = initialRemainingTime
    }

    suspend fun countDown() {
        var localRemainingTime = _remainingTime.value ?: 0
        viewModelScope.launch {
            while (localRemainingTime > 0) {
                --localRemainingTime
                _remainingTime.value = localRemainingTime
                // wait for one second
                Thread.sleep(1000)
                _timeExpired.value = localRemainingTime <= 0
            }
        }
    }
}