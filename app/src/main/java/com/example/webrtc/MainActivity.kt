package com.example.webrtc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.webrtc.ui.theme.WebRTCTheme
import io.getstream.video.android.compose.permission.LaunchCallPermissions
import io.getstream.video.android.compose.theme.VideoTheme
import io.getstream.video.android.compose.ui.components.livestream.LivestreamPlayer
import io.getstream.video.android.core.Call
import io.getstream.video.android.core.StreamVideo

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val client = StreamVideo.instance()
        val call = client.call("livestream", "livestream_58d1dea6-1b7c-4c55-9278-d82d92e453d7")
        setContent {
            WebRTCTheme {
                VideoTheme {
                    LiveAudience(call = call)
                }
            }
        }
    }
}


@Composable
fun LiveAudience(call: Call) {
    LaunchCallPermissions(call = call, onAllPermissionsGranted = {
        call.join(create = true)
    })
    LivestreamPlayer(
        call = call,
    )
}
