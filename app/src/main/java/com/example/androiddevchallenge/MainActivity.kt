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
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
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
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.theme.MyTheme

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val vm by viewModels<MainActivityViewModel>()
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
                            text = "The Final Countdown",
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
    var textFieldFocusState by remember { mutableStateOf(false) }

    if (isRunning) {
        TimerRunning(modifier = modifier, vm = vm)
    } else {
        TimerSetup(
            modifier = modifier,
            vm = vm,
            textFieldValue = textState,
            onTextChanged = { textState = it },
            onTextFieldFocused = { textFieldFocusState = it },
            focusState = textFieldFocusState
        )
    }
}

@Composable
fun TimerSetup(modifier: Modifier,
               vm: MainActivityViewModel,
               textFieldValue: TextFieldValue,
               onTextChanged: (TextFieldValue) -> Unit,
               onTextFieldFocused: (Boolean) -> Unit,
               focusState: Boolean
) {
    Column(modifier = modifier
        .fillMaxWidth()
    ) {
        Box(modifier = Modifier.fillMaxWidth()
        ) {
            var lastFocusState by remember { mutableStateOf(FocusState.Inactive) }
            val disableContentColor =
                MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
            if (textFieldValue.text.isEmpty() && !focusState) {
                Text(text = "Enter start time",
                    style = MaterialTheme.typography.body1.copy(color = disableContentColor),
                    modifier = Modifier.align(Alignment.CenterStart)
                        .padding(16.dp)
                )
            }
            BasicTextField(
                modifier = Modifier.fillMaxWidth()
                    .align(Alignment.CenterStart)
                    .onFocusChanged { state ->
                        if (lastFocusState != state) {
                            onTextFieldFocused(state == FocusState.Active)
                        }
                        lastFocusState = state
                    }
                    .border(Dp.Hairline, Color.DarkGray, RoundedCornerShape(4.dp))
                    .padding(16.dp),
                value = textFieldValue,
                onValueChange = { onTextChanged(it) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Send
                ),
                cursorBrush = SolidColor(LocalContentColor.current),
                textStyle = LocalTextStyle.current.copy(color = LocalContentColor.current),
                maxLines = 1
            )
        }
        Spacer(
            Modifier.height(16.dp)
        )
        Button(onClick = { vm.countDown(textFieldValue.text.toInt()) },
            modifier = Modifier.fillMaxWidth()) {
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