package com.example.webrtc

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.webrtc.ui.theme.WebRTCTheme
import com.example.webrtc.viewModel.LiveStreamViewModel
import io.getstream.video.android.compose.permission.LaunchCallPermissions
import io.getstream.video.android.compose.theme.VideoTheme
import io.getstream.video.android.compose.ui.components.livestream.LivestreamPlayer
import io.getstream.video.android.core.Call
import io.getstream.video.android.core.StreamVideo
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
        val call = client.call("livestream", "livestream_6d99df83-2f74-494a-bde8-3b54e3166fc0")
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
    val networkQuality by liveStreamViewModel.networkQuality.collectAsState()

    LaunchedEffect(connectionState) {
        when(connectionState) {
            PeerConnection.PeerConnectionState.NEW -> {
                Toast.makeText(context, "Connection state: NEW", Toast.LENGTH_SHORT).show()
            }
            PeerConnection.PeerConnectionState.CONNECTING -> {
                Toast.makeText(context, "Connection state: CONNECTING", Toast.LENGTH_SHORT).show()
            }
            PeerConnection.PeerConnectionState.CONNECTED -> {
                Toast.makeText(context, "Connection state: CONNECTED", Toast.LENGTH_SHORT).show()
            }
            PeerConnection.PeerConnectionState.DISCONNECTED -> {
                Toast.makeText(context, "Connection state: DISCONNECTED", Toast.LENGTH_SHORT).show()
            }
            PeerConnection.PeerConnectionState.FAILED -> {
                Toast.makeText(context, "Connection state: FAILED", Toast.LENGTH_SHORT).show()
            }
            PeerConnection.PeerConnectionState.CLOSED -> {
                Toast.makeText(context, "Connection state: CLOSED", Toast.LENGTH_SHORT).show()
            }
        }
    }
    // Show toast for network quality changes
    LaunchedEffect(networkQuality) {
        Toast.makeText(context, "Network quality: $networkQuality", Toast.LENGTH_SHORT).show()
    }

    LaunchCallPermissions(call = call, onAllPermissionsGranted = {
        call.join(create = true)
    })
    LivestreamPlayer(
        call = call,
    )
}
