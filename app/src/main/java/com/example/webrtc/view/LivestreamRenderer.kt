package com.example.webrtc.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.getstream.video.android.compose.ui.components.video.VideoRenderer
import io.getstream.video.android.core.Call
import io.getstream.webrtc.android.ui.VideoTextureViewRenderer

/**
 * @Author: Jupyter.
 * @Date: 1/14/25.
 * @Email: koemheang200@gmail.com.
 */
@Composable
internal fun livestreamRenderer(
    call: Call,
    enablePausing: Boolean,
    onPausedPlayer: ((isPaused: Boolean) -> Unit)? = {},
) {
    val livestream by call.state.livestream.collectAsState()
   //  val testCallState by call.state.connection.collectAsStateWithLifecycle()
    var videoTextureView: VideoTextureViewRenderer? by remember { mutableStateOf(null) }
    var isPaused by rememberSaveable { mutableStateOf(false) }


    Box (
        modifier = Modifier.fillMaxSize()
    ){
       VideoRenderer(
           modifier = Modifier
               .fillMaxSize()
               .clickable(enabled = enablePausing) {
                   if (onPausedPlayer != null) {
                       isPaused = !isPaused
                       livestream?.track?.video?.setEnabled(!isPaused)
                       onPausedPlayer.invoke(isPaused)

                       if (isPaused) {
                           videoTextureView?.pauseVideo()
                       } else {
                           videoTextureView?.resumeVideo()
                       }
                   }
               },
           call = call,
           video = livestream,
           onRendered = { renderer ->
               videoTextureView = renderer
           },
       )
    }

}