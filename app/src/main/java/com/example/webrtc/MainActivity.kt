package com.example.webrtc

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.webrtc.ui.theme.WebRTCTheme
import com.example.webrtc.view.CustomOverlayContent
import com.example.webrtc.view.LivestreamBackStage
import com.example.webrtc.view.livestreamRenderer
import com.example.webrtc.viewModel.LiveStreamViewModel
import io.getstream.video.android.compose.permission.LaunchCallPermissions
import io.getstream.video.android.compose.theme.VideoTheme
import io.getstream.video.android.compose.ui.components.livestream.LivestreamPlayer
import io.getstream.video.android.compose.ui.components.livestream.LivestreamPlayerOverlay
import io.getstream.video.android.core.Call
import io.getstream.video.android.core.RealtimeConnection
import io.getstream.video.android.core.StreamVideo
import kotlinx.coroutines.flow.collect
import org.webrtc.PeerConnection

class MainActivity : ComponentActivity() {
    companion object {
        const val REQUEST_CAMERA_PERMISSION = 1001
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val client = StreamVideo.instance()
        val liveStreamViewModel: LiveStreamViewModel by viewModels()
        val call = client.call("livestream", "livestream_0c184219-c98d-4c2a-ad80-a790d3590c03")
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
    println("Information of call message:${call.state.connection.collectAsState().value}")
    println("Information of call connectionState:${connectionState}")
    var isRender = call.state.connection.collectAsState().value
    when (isRender) {
        RealtimeConnection.PreJoin -> {
            println("isRender PreJoin:${isRender}")
        }
        RealtimeConnection.InProgress -> {
            println("isRender:${isRender}")
            Text(text = "Loading.....InProgress testing")
        }
        RealtimeConnection.Connected -> {
            println("isRender:${isRender}")
        }
        RealtimeConnection.Disconnected -> {
            println("isRender:${isRender}")
        }
        RealtimeConnection.Reconnecting -> {
            println("isRender:${isRender}")
        }
        else -> {
            println("isRender:${isRender}")
        }
    }

    LaunchedEffect(connectionState) {
        val message = when (connectionState) {
            PeerConnection.PeerConnectionState.NEW -> "Connection state: NEW"
            PeerConnection.PeerConnectionState.CONNECTING -> "Connection state: CONNECTING"
            PeerConnection.PeerConnectionState.CONNECTED -> "Connection state: CONNECTED"
            PeerConnection.PeerConnectionState.DISCONNECTED -> "Connection state: DISCONNECTED"
            PeerConnection.PeerConnectionState.FAILED -> "Connection state: FAILED"
            PeerConnection.PeerConnectionState.CLOSED -> "Connection state: CLOSED"
            else -> "Unknown connection state"
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    println("Information of connectionState: $connectionState")
    LaunchCallPermissions(call = call, onAllPermissionsGranted = {
        call.join(create = true)
    })
    LivestreamPlayer(
        call = call,
        enablePausing = true,
        onPausedPlayer = {
            Toast.makeText(context, "Paused", Toast.LENGTH_SHORT).show()
        },
        backstageContent = { LivestreamBackStage() },
        rendererContent = { livestreamRenderer(
            call = call,
            enablePausing = true
        )},
        overlayContent = {
            LivestreamPlayerOverlay(
                call = call
            )
        }
        )
}
