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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
                TopAppBar (
                    title = {
                        Text(
                            text = "Final Countdown",
                            style = MaterialTheme.typography.subtitle1
                        )
                    }
                )
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
    val isRunning by vm.timerRunning.observeAsState(false)
    var textState by remember { mutableStateOf(TextFieldValue()) }

    if (isRunning) {
        TimerRunning(modifier = modifier, vm = vm)
    } else {
        TimerSetup(
            modifier = modifier,
            vm = vm,
            textFieldValue = textState
        ) { textState = it }
    }
}

@Composable
fun TimerSetup(modifier: Modifier,
               vm: MainActivityViewModel,
               textFieldValue: TextFieldValue,
               onTextChanged: (TextFieldValue) -> Unit,
) {
    Column(modifier = modifier
        .fillMaxWidth()
    ) {
        Box(modifier = modifier.fillMaxWidth()
        ) {
            BasicTextField(
                modifier = modifier.fillMaxWidth()
                    .align(Alignment.CenterStart),
                value = textFieldValue,
                onValueChange = { onTextChanged(it) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Send
                ),
                cursorBrush = SolidColor(LocalContentColor.current),
                maxLines = 1
            )
        }
        val disableContentColor =
            MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
        if (textFieldValue.text.isEmpty()) {
            Text(text = "Enter start time",
                style = MaterialTheme.typography.body1.copy(color = disableContentColor)
            )
        }
        Button(onClick = { vm.countDown(textFieldValue.text.toInt()) }) {
            Text(text = "Start Timer")
        }
    }
}

@Composable
fun TimerRunning(modifier: Modifier, vm: MainActivityViewModel) {
    val remainingTime by vm.remainingTime.observeAsState()
    Column(modifier = modifier
        .fillMaxWidth()
    ) {
        Text(text = "Time Remaining: $remainingTime")
        Button(onClick = { vm.stopTimer() }) {
            Text(text = "Stop Timer")
        }
    }
}