package com.example.webrtc.util

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.webrtc.view.FillInfoStreaming
import com.example.webrtc.view.ViewerVideo
import com.example.webrtc.viewModel.LiveStreamViewModel

/**
 * @Author: Jupyter.
 * @Date: 1/16/25.
 * @Email: koemheang200@gmail.com.
 */

@Composable
fun AppNavigation(liveStreamViewModel: LiveStreamViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "screenA") {
        composable("screenA") {
            FillInfoStreaming(navController, liveStreamViewModel)
        }
        composable(
            "ViewerVideo/{liveStreamViewModel}/{name}/{email}/{age}",
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("email") { type = NavType.StringType },
                navArgument("age") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val age = backStackEntry.arguments?.getString("age") ?: ""
            ViewerVideo(liveStreamViewModel, name, email, age)
        }
    }
}