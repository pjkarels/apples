/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.theme.MyTheme

class MainActivity : AppCompatActivity() {

    private val vm by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp(vm)
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp(vm: MainActivityViewModel) {
    Surface(color = MaterialTheme.colors.background) {
        Scaffold(
            topBar = {
                TopAppBar {
                    Text(
                        text = "Final Countdown",
                        style = MaterialTheme.typography.subtitle1
                    )
                }
            },
            content = {
                MainContent(
                    modifier = Modifier.padding(16.dp),
                    vm
                )
            }
        )
    }
}

@Composable
fun MainContent(modifier: Modifier, vm: MainActivityViewModel) {
    Column(modifier = modifier
        .fillMaxWidth()
    ) {

    }
}

@Composable
fun TimerSetup(vm: MainActivityViewModel) {
    val textState by remember { mutableStateOf(TextFieldValue()) }

    BasicTextField(
        value = textState,
        onValueChange = { textFieldValue: TextFieldValue -> textFieldValue.text }
    )
    Button(onClick = { vm.countDown(1) }) {
        Text(text = "Start Timer")
    }
}

@Composable
fun TimerRunning(vm: MainActivityViewModel) {
    val remainingTime = vm.remainingTime.observeAsState()

    Text(text = "Time Remaining: $remainingTime")
    Button(onClick = { vm.stopTimer() }) {
        Text(text = "Stop Timer")
    }
}