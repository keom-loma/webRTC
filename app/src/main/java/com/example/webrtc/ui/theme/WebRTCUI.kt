package com.example.webrtc.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun WebRTCUi(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        ElevatedButton(onClick = {}) {
            Text(text = "Open WebRTC")
        }
    }
}