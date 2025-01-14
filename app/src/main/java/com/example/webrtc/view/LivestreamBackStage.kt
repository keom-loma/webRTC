package com.example.webrtc.view

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.webrtc.R
import io.getstream.video.android.compose.theme.VideoTheme

/**
 * @Author: Jupyter.
 * @Date: 1/14/25.
 * @Email: koemheang200@gmail.com.
 */
@Composable
internal fun BoxScope.LivestreamBackStage() {
    Text(
        style = TextStyle(
            color = Color.Red
        ),
    modifier = Modifier.align(Alignment.Center),
    text = stringResource(
        id =  R.string.livestream_backstage,
    ),
    fontSize = 14.sp,
    //color = VideoTheme.colors.brandPrimary,
)
}