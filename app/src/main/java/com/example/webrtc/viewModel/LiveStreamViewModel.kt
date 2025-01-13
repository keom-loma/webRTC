package com.example.webrtc.viewModel

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.webrtc.DataChannel
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import org.webrtc.PeerConnectionFactory
import org.webrtc.PeerConnectionFactory.InitializationOptions
import kotlinx.coroutines.launch
import org.webrtc.DefaultVideoDecoderFactory
import org.webrtc.DefaultVideoEncoderFactory
import org.webrtc.EglBase
import org.webrtc.IceCandidate
import org.webrtc.MediaConstraints
import org.webrtc.SdpObserver
import org.webrtc.SessionDescription

/**
 * @Author: Jupyter.
 * @Date: 1/13/25.
 * @Email: koemheang200@gmail.com.
 */


class LiveStreamViewModel(application: Application) : AndroidViewModel(application) {


    private val _networkQuality = MutableStateFlow("Good") // Example: "Good", "Slow", "Very Slow"
    val networkQuality: StateFlow<String> = _networkQuality
    private val context: Context = application.applicationContext
    private lateinit var peerConnectionFactory: PeerConnectionFactory
    private lateinit var peerConnection: PeerConnection

    private val _connectionState = MutableStateFlow(PeerConnection.PeerConnectionState.NEW)
    val connectionState: StateFlow<PeerConnection.PeerConnectionState> = _connectionState

    init {
        initializePeerConnectionFactory()
    }

    private fun initializePeerConnectionFactory() {
        viewModelScope.launch {
            val options = InitializationOptions.builder(context)
                .setFieldTrials("WebRTC-H264HighProfile/Enabled/")
                .createInitializationOptions()

            PeerConnectionFactory.initialize(options)
            peerConnectionFactory = PeerConnectionFactory.builder()
                .setVideoDecoderFactory(DefaultVideoDecoderFactory(EglBase.create().eglBaseContext))
                .setVideoEncoderFactory(DefaultVideoEncoderFactory(EglBase.create().eglBaseContext, true, true))
                .createPeerConnectionFactory()

            val peerConnectionParams = PeerConnection.RTCConfiguration(mutableListOf()).apply {
                iceTransportsType = PeerConnection.IceTransportsType.ALL
                tcpCandidatePolicy = PeerConnection.TcpCandidatePolicy.DISABLED
                iceCandidatePoolSize = 10
                sdpSemantics = PeerConnection.SdpSemantics.UNIFIED_PLAN
                continualGatheringPolicy = PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY
            }

            peerConnection = peerConnectionFactory.createPeerConnection(peerConnectionParams, object : PeerConnection.Observer {
                override fun onIceConnectionChange(state: PeerConnection.IceConnectionState?) {
                    state?.let {
                        when (it) {
                            PeerConnection.IceConnectionState.CONNECTED -> monitorNetworkQuality()
                            PeerConnection.IceConnectionState.DISCONNECTED ->{
                                showToast("Network disconnected or failed.")
                            }
                            PeerConnection.IceConnectionState.FAILED -> {
                                showToast("Network disconnected or failed.")
                            }
                            PeerConnection.IceConnectionState.NEW ->{
                                showToast("Network disconnected or failed.")
                            }

                            PeerConnection.IceConnectionState.CHECKING -> {
                                showToast("Network disconnected or failed.")
                            }
                            PeerConnection.IceConnectionState.COMPLETED -> {
                                showToast("Network disconnected or failed.")
                            }
                            PeerConnection.IceConnectionState.CLOSED -> {
                                showToast("Network disconnected or failed.")
                            }
                        }
                    }
                }

                override fun onSignalingChange(state: PeerConnection.SignalingState?) {}
                override fun onIceConnectionReceivingChange(receiving: Boolean) {}
                override fun onIceGatheringChange(state: PeerConnection.IceGatheringState?) {}
                override fun onIceCandidate(candidate: IceCandidate?) {}
                override fun onIceCandidatesRemoved(candidates: Array<out IceCandidate>?) {}
                override fun onAddStream(mediaStream: MediaStream?) {}
                override fun onRemoveStream(mediaStream: MediaStream?) {}
                override fun onDataChannel(dataChannel: DataChannel?) {}
                override fun onRenegotiationNeeded() {}
            })!!
        }
    }

    private fun monitorNetworkQuality() {
        println("monitorNetworkQuality ex")
        viewModelScope.launch {
            while (true) {
               peerConnection.getStats { report ->
                    report.statsMap.values.forEach { stats ->
                        println("Network latency: ${stats.type}")
                        if (stats.type == "candidate-pair" && stats.members["state"] == "succeeded") {
                            println("Network latency: ${stats.members["currentRoundTripTime"]}")
                            val currentRtt = stats.members["currentRoundTripTime"].toString().toDouble() ?: 0.0
                            when {
                                currentRtt < 100 -> _networkQuality.value = "Good"
                                currentRtt < 300 -> _networkQuality.value = "Slow"
                                else -> _networkQuality.value = "Very Slow"
                            }
                        }
                    }
                }
                delay(1000) // Check every 1 seconds*/
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}