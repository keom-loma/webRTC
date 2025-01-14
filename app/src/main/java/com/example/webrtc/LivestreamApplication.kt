package com.example.webrtc

import android.app.Application
import androidx.compose.runtime.clearCompositionErrors
import io.getstream.log.Priority
import io.getstream.video.android.core.GEO
import io.getstream.video.android.core.StreamVideoBuilder
import io.getstream.video.android.core.logging.LoggingLevel
import io.getstream.video.android.model.User

class LivestreamApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initializeSDK()
    }

    private fun initializeSDK() {
        val userId = "c69eyawhnekh"
        val userName = "Broadcaster"
        val userToken ="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJAc3RyZWFtLWlvL2Rhc2hib2FyZCIsImlhdCI6MTczNjgzMjA5MSwiZXhwIjoxNzM2OTE4NDkxLCJ1c2VyX2lkIjoiIWFub24iLCJyb2xlIjoidmlld2VyIiwiY2FsbF9jaWRzIjpbImxpdmVzdHJlYW06bGl2ZXN0cmVhbV8wYzE4NDIxOS1jOThkLTRjMmEtYWQ4MC1hNzkwZDM1OTBjMDMiXX0.yLdVGncKELss3ybkNVbMzN_PGbAHWOahnvA9x5xlQHE"


        // Step 1 - Create a user.
        val user = User(
            id = userId, // predefined string
            name = userName, // Name and image are used in the UI
            role = "admin",
        )

        // Step 2 - Initialize StreamVideo. For a production app, we recommend adding the client to your Application class or DI module.
        val client = StreamVideoBuilder(
            context = this,
            user = User.anonymous(),
            apiKey = userId,
            token =  userToken,
            //geo = GEO.GlobalEdgeNetwork,
           // loggingLevel = LoggingLevel(priority = Priority.VERBOSE)

        ).build()
        println("Information of client: ${client.state.connection}")
    }
}