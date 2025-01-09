package com.example.webrtc.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.getstream.video.android.compose.theme.VideoTheme
import io.getstream.video.android.compose.ui.components.livestream.LivestreamPlayer
import io.getstream.video.android.compose.ui.components.video.VideoRenderer
import io.getstream.video.android.core.Call
import io.getstream.video.android.core.RealtimeConnection
import io.getstream.video.android.core.StreamVideo
import io.getstream.video.android.model.User
import kotlinx.coroutines.launch


@Composable
fun CustomOverlayContent(call: Call) {
    val count by call.state.participantCounts.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Custom overlay Live Stream",
            style = VideoTheme.typography.bodyL,
            color = Color.Red
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Viewers: $count", color = Color.White)
    }
}


@Preview
@Composable
fun PreviewCustomLivestreamPlayer() {
    CustomLivestreamPlayer(
        call = Call(
            client = StreamVideo.instance(),
            type = "Test Call",
            id = "jupyter10",
            user = User(
                id = "jupyter10",
                name = "Jupyter",
            )
        )
    )
}

@Composable
fun CustomLivestreamPlayer(call: Call) {
    val connection by call.state.connection.collectAsState()
    val totalParticipants by call.state.totalParticipants.collectAsState()
    val backstage by call.state.backstage.collectAsState()
    val localParticipant by call.state.localParticipant.collectAsState()
    val video = localParticipant?.video?.collectAsState()
    val duration by call.state.duration.collectAsState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {

        },
        bottomBar = {

        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (connection == RealtimeConnection.Connected) {
                if (!backstage) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(alignment = Alignment.Center)
                                .background(
                                    color = VideoTheme.colors.brandPrimary,
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            text = "Custom overlay Live Stream",
                            style = VideoTheme.typography.bodyL, color = Color.Red
                        )
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "Live for $duration",
                            color = VideoTheme.colors.basePrimary,
                        )
                    }

                   /* VideoRenderer(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                            .clip(RoundedCornerShape(6.dp)),
                        call = call,
                        video = video?.value,
                        videoFallbackContent = {
                            // Content for when the video is not available.
                        },
                    )*/
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            contentColor = VideoTheme.colors.brandPrimary,
                            containerColor = VideoTheme.colors.brandPrimary
                        ),
                        onClick = {
                            scope.launch {
                                if (backstage) call.goLive() else call.stopLive()
                            }
                        },
                    ) {
                        Text(
                            text = if (backstage) "Start Broadcast" else "Stop Broadcast",
                            color = Color.White,
                        )
                    }
                } else {
                    Text(
                        text = "The livestream is not started yet",
                        color = VideoTheme.colors.basePrimary,
                    )
                }
            } else if (connection is RealtimeConnection.Failed) {
                Text(
                    text = "Connection failed",
                    color = VideoTheme.colors.basePrimary,
                )
            }
        }

    }
    LivestreamPlayer(
        call = call,
        overlayContent = { CustomOverlayContent(call = call) }
    )
}
