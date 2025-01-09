package com.example.webrtc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.webrtc.view.CustomOverlayContent
import io.getstream.video.android.compose.theme.VideoTheme
import io.getstream.video.android.compose.ui.components.livestream.LivestreamPlayer
import io.getstream.video.android.core.Call

@Composable
fun CustomLivestreamPlayer(call: Call) {
    println("CustomLivestreamPlayer: ${call}")
    val totalParticipants by call.state.totalParticipants.collectAsState()
    val duration by call.state.duration.collectAsState()
    val livestream by call.state.livestream.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Green)
            .padding(6.dp),
        contentColor = VideoTheme.colors.baseSheetPrimary,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.Red
                    ),
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .background(
                            color = VideoTheme.colors.brandPrimary,
                            shape = RoundedCornerShape(6.dp),
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    text = "Live $totalParticipants",
                    color = Color.Black
                )

                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Live for $duration",
                    color = Color.Black,
                )
            }
        },
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()

            ) {
                LivestreamPlayer(
                    call = call,
                    enablePausing = true,
                    backstageContent = {
                        CustomOverlayContent(call = call)
                    },
                    /*rendererContent = {
                        CustomOverlayContent(call = call)
                    },*/
                   /* overlayContent = {
                        CustomOverlayContent(call = call)
                    }*/
                )
            }
            // androidx.compose.material3.Text(text = "Hello world")
        }
        /* VideoRenderer(
             modifier = Modifier
                 .fillMaxSize()
                 .padding(it)
                 .clip(RoundedCornerShape(6.dp)),
             call = call,
             video = livestream,
             videoFallbackContent = {
                 // Content for when the video is not available.
             },
         )*/
    }
}

@Composable
fun Text(modifier: Modifier, text: String, color: Any) {

}
