package com.example.webrtc.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.getstream.video.android.compose.theme.VideoTheme
import io.getstream.video.android.compose.ui.components.call.controls.actions.ToggleSpeakerphoneAction
import io.getstream.video.android.core.Call

/**
 * @Author: Jupyter.
 * @Date: 1/14/25.
 * @Email: koemheang200@gmail.com.
 */

@Composable
internal fun BoxScope.LivestreamPlayerOverlay(call:Call){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .padding(6.dp),
    ) {
        LiveBadge(call = call)

        LiveDuration(call = call)

        LiveControls(call = call)
    }
}

@Composable
private fun LiveControls(call: Call) {
    val speakerphoneEnabled by if (LocalInspectionMode.current) {
        remember { mutableStateOf(true) }
    } else {
        call.speaker.isEnabled.collectAsState()
    }
    ToggleSpeakerphoneAction(
        modifier = Modifier
            .size(45.dp),
        isSpeakerphoneEnabled = speakerphoneEnabled,
        onCallAction = { callAction -> call.speaker.setEnabled(callAction.isEnabled) },
    )
}

@Composable
private fun LiveDuration(call: Call) {
    val duration by call.state.duration.collectAsState()
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(VideoTheme.colors.alertWarning),
        )

        Text(
            modifier = Modifier.padding(horizontal = 6.dp),
            text = (duration ?: 0).toString(),
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
        )
    }

}

@Composable
private fun LiveBadge(call: Call){
    val totalParticipants by call.state.totalParticipants.collectAsState()
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .background(
                    color = VideoTheme.colors.brandPrimary,
                    shape = VideoTheme.shapes.container,
                )
                .padding(horizontal = 16.dp, vertical = 4.dp),
            text = stringResource(
                id = io.getstream.video.android.ui.common.R.string.stream_video_live,
            ),
            color = Color.White,
        )

        Spacer(modifier = Modifier.width(12.dp))

        Image(
            modifier = Modifier.size(22.dp),
            painter = painterResource(
                id = io.getstream.video.android.ui.common.R.drawable.stream_video_ic_live,
            ),
            contentDescription = stringResource(
                id = io.getstream.video.android.ui.common.R.string.stream_video_live,
            ),
        )

        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = totalParticipants.toString(),
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}
