package com.example.webrtc

import android.app.Application
import io.getstream.video.android.core.StreamVideoBuilder
import io.getstream.video.android.model.User

class LivestreamApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeSDK()
    }

    private fun initializeSDK() {
        val userId = "c69eyawhnekh"
        val userName = "Broadcaster"
        val userToken =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJAc3RyZWFtLWlvL2Rhc2hib2FyZCIsImlhdCI6MTczNzAyMDkxOSwiZXhwIjoxNzM3MTA3MzE5LCJ1c2VyX2lkIjoiIWFub24iLCJyb2xlIjoidmlld2VyIiwiY2FsbF9jaWRzIjpbImxpdmVzdHJlYW06bGl2ZXN0cmVhbV8yNWI4ZmFmOS0wYTE3LTRmN2EtYmE4OC1iYzcwZDI2M2QxNmEiXX0.Lv6UONlm19y5n6ZXHOD3s9c7mzVUTXf5mp0jCt9pD1U"


        // Step 1 - Create a user.
        val user = User(
            id = userId,
            name = userName,
            role = "admin",
        )

        // Step 2 - Initialize StreamVideo. For a production app, we recommend adding the client to your Application class or DI module.
        val client = StreamVideoBuilder(
            context = this,
            user = User.anonymous(),
            apiKey = userId,
            token = userToken,

            ).build()
        println("Information of client: ${client.state.connection}")
    }
}