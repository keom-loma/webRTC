package com.example.webrtc

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.webrtc.ui.theme.WebRTCTheme
import com.example.webrtc.view.LivestreamBackStage
import com.example.webrtc.view.livestreamRenderer
import com.example.webrtc.viewModel.LiveStreamViewModel
import io.getstream.video.android.compose.permission.LaunchCallPermissions
import io.getstream.video.android.compose.theme.VideoTheme
import io.getstream.video.android.compose.ui.components.livestream.LivestreamPlayer
import io.getstream.video.android.core.Call
import io.getstream.video.android.core.RealtimeConnection
import io.getstream.video.android.core.StreamVideo
import org.webrtc.PeerConnection

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val client = StreamVideo.instance()
        val liveStreamViewModel: LiveStreamViewModel by viewModels()
        val call = client.call("livestream", "livestream_319584b0-c1be-4c75-809d-03d82da2e5e7")
        setContent {
            WebRTCTheme {
                VideoTheme {
                    LiveAudience(call = call, liveStreamViewModel = liveStreamViewModel)
                }
            }
        }
    }
}


@Composable
fun LiveAudience(call: Call, liveStreamViewModel: LiveStreamViewModel) {
    val context = LocalContext.current
    val connectionState by liveStreamViewModel.connectionState.collectAsState()
    val isRender = call.state.connection.collectAsState().value
    val isCheckingVideo = call.state.livestream.collectAsState().value
    println("Information of isCheckingVideo: $isCheckingVideo")
    println("Information of connectionState: $connectionState")
    println("Information of isRender: $isRender")
    when (isRender) {
        RealtimeConnection.PreJoin -> {
            Toast.makeText(context, isRender.toString(), Toast.LENGTH_SHORT).show()
        }

        RealtimeConnection.InProgress -> {
            Toast.makeText(context, isRender.toString(), Toast.LENGTH_SHORT).show()

        }

        RealtimeConnection.Connected -> {
            println("Connected condition")
            Toast.makeText(context, isRender.toString(), Toast.LENGTH_SHORT).show()

        }

        RealtimeConnection.Disconnected -> {
            println("Disconnected condition")
            Toast.makeText(context, isRender.toString(), Toast.LENGTH_SHORT).show()
        }

        RealtimeConnection.Reconnecting -> {
            println("Reconnecting condition")
            Toast.makeText(context, isRender.toString(), Toast.LENGTH_SHORT).show()

        }

        else -> {
            Toast.makeText(context, isRender.toString(), Toast.LENGTH_SHORT).show()
        }
    }
    LaunchedEffect(isRender) {
        if (isRender == RealtimeConnection.Disconnected || isRender == RealtimeConnection.Reconnecting) {
            Toast.makeText(context, "Attempting to reconnect to the stream...", Toast.LENGTH_SHORT)
                .show()
        }
    }
    LaunchedEffect(connectionState) {
        val message = when (connectionState) {
            PeerConnection.PeerConnectionState.NEW -> "Connection state: NEW"
            PeerConnection.PeerConnectionState.CONNECTING -> "Connection state: CONNECTING"
            PeerConnection.PeerConnectionState.CONNECTED -> {
                call.join(create = true)
                "Connection state: CONNECTED"
            }

            PeerConnection.PeerConnectionState.DISCONNECTED -> "Connection state: DISCONNECTED"
            PeerConnection.PeerConnectionState.FAILED -> "Connection state: FAILED"
            PeerConnection.PeerConnectionState.CLOSED -> "Connection state: CLOSED"
            else -> "Unknown connection state"
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    LaunchCallPermissions(call = call, onAllPermissionsGranted = {
        call.join(create = true)
    })

    if (isCheckingVideo?.track != null) {
        Column(
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp),
            ) {
                Column {
                    LivestreamPlayer(
                        call = call,
                        enablePausing = true,
                        onPausedPlayer = {
                            Toast.makeText(context, "Paused", Toast.LENGTH_SHORT).show()
                        },
                        backstageContent = { LivestreamBackStage() },
                        rendererContent = {
                            livestreamRenderer(
                                call = call,
                                enablePausing = true
                            )
                        },
                    )
                }
            }
            LazyColumn(
                contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
            ) {
                items(10) { index ->
                    Box(
                        modifier = Modifier.background(
                            color = if (index % 2 == 0) Color.White else Color.Gray.copy(
                                alpha = 0.2f
                            )
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .height(30.dp)
                        ) {

                        }
                    }
                }
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Text(
                    text = "$isRender",
                    modifier = Modifier.padding(
                        top = 10.dp
                    ),
                )
            }
        }
    }
}
