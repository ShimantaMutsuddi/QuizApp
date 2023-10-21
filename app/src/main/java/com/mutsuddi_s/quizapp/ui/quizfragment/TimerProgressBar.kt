package com.mutsuddi_s.quizapp.ui.quizfragment


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.em
import com.mutsuddi_s.quizapp.ui.quizfragment.QuizViewModel

@Composable
fun ProgressBar(viewModel: QuizViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val progress = viewModel.progress.value

        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ProgressBar(progress)
            TimeTextView(progress)
        }


    }
}


@Composable
fun TimeTextView(progress: Int) {
    Text(
        text = "${(10 - progress)}/10 ",
        style = TextStyle(
            color = Color.Black,
            fontSize = 13.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.W800,
            fontStyle = FontStyle.Normal,
            background = Color.LightGray,

        )
        )




}

@Composable
fun ProgressBar(progress: Int) {


    LinearProgressIndicator(
        modifier = Modifier,
        progress = progress / 10.0f ,
        color = Color.Black,
        trackColor = Color.LightGray

    )




}
