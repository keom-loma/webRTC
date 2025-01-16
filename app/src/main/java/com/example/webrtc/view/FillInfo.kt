package com.example.webrtc.view

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.webrtc.LiveAudience
import com.example.webrtc.util.Constant
import com.example.webrtc.util.Constant.openAppSetting
import com.example.webrtc.viewModel.LiveStreamViewModel
import io.getstream.video.android.compose.theme.VideoTheme
import io.getstream.video.android.core.StreamVideo

/**
 * @Author: Jupyter.
 * @Date: 1/16/25.
 * @Email: koemheang200@gmail.com.
 */

@Composable
fun FillInfoStreaming(navController: NavController, liveStreamViewModel: LiveStreamViewModel) {
    var livestreamID by remember { mutableStateOf("livestream_25b8faf9-0a17-4f7a-ba88-bc70d263d16a") }
    var apiKey by remember { mutableStateOf("c69eyawhnekh") }
    var viewerToken by remember { mutableStateOf("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJAc3RyZWFtLWlvL2Rhc2hib2FyZCIsImlhdCI6MTczNzAyMDkxOSwiZXhwIjoxNzM3MTA3MzE5LCJ1c2VyX2lkIjoiIWFub24iLCJyb2xlIjoidmlld2VyIiwiY2FsbF9jaWRzIjpbImxpdmVzdHJlYW06bGl2ZXN0cmVhbV8yNWI4ZmFmOS0wYTE3LTRmN2EtYmE4OC1iYzcwZDI2M2QxNmEiXX0.Lv6UONlm19y5n6ZXHOD3s9c7mzVUTXf5mp0jCt9pD1U") }
    Box {
        Column (
            modifier = Modifier.fillMaxSize().padding(
                horizontal = 16.dp
            ),
            verticalArrangement = Arrangement.Center,
        ){
            OutlinedTextField(
                value = livestreamID,
                onValueChange = { livestreamID = it },
                label = { Text("Livestream ID:") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = apiKey,
                onValueChange = { apiKey = it },
                label = { Text("API Key:") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewerToken,
                onValueChange = { viewerToken = it },
                label = { Text("Viewers Token:") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    // Navigate and pass data
                     navController.navigate("ViewerVideo/$liveStreamViewModel/$livestreamID/$apiKey/$viewerToken")
                },
                modifier = Modifier.align(Alignment.End).fillMaxWidth().padding(
                    top = 16.dp
                )
            ) {
                Text("Go to live")
            }
        }
    }
}


@Composable
fun ViewerVideo(
    liveStreamViewModel: LiveStreamViewModel,
    livestreamId: String,
    apiKey: String,
    viewersToken: String
) {
    println("Information livestreamId: $livestreamId")
    println("Information apiKey: $apiKey")
    println("Information viewersToken: $viewersToken")
    val context = LocalContext.current
    var isDenied by remember { mutableStateOf(true) }
    PermissionAwareComponent(
        modifier = Modifier.fillMaxSize(),
        permissions = Constant.PERMISSIONS,
        shouldShowSetting = {
            context.openAppSetting()
        },
        onPermissionGranted = {
            isDenied = false
        }
    ) { event ->
        event.value = true
        LaunchedEffect(key1 = Unit) {
            if (context.checkPermissionsDenied(Constant.PERMISSIONS)) {
                isDenied = true
            } else {
                isDenied = false
            }
        }
        Log.e("TAG", "$isDenied")
        if (isDenied.not()) {
            val client = StreamVideo.instance()
            //val call = client.call("livestream", "livestream_25b8faf9-0a17-4f7a-ba88-bc70d263d16a")
            val call = client.call("livestream", livestreamId)
            VideoTheme {
                LiveAudience(call = call, liveStreamViewModel = liveStreamViewModel)
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
                        text = "Need Permission!",
                        modifier = Modifier.padding(
                            top = 10.dp
                        ),
                    )
                }
            }
        }
    }
}


@Composable
fun ScreenB(name: String, email: String, age: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Name: $name", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Email: $email", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Age: $age", style = MaterialTheme.typography.bodyLarge)
    }
}