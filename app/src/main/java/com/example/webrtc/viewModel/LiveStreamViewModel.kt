package com.example.webrtc.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.webrtc.DataChannel
import org.webrtc.IceCandidate
import org.webrtc.MediaConstraints
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import org.webrtc.PeerConnection.IceConnectionState
import org.webrtc.PeerConnection.IceGatheringState
import org.webrtc.PeerConnection.IceServer
import org.webrtc.PeerConnection.PeerConnectionState
import org.webrtc.PeerConnection.SignalingState
import org.webrtc.PeerConnectionFactory
import org.webrtc.PeerConnectionFactory.InitializationOptions
import org.webrtc.RTCStatsCollectorCallback
import org.webrtc.RTCStatsReport
import org.webrtc.SdpObserver
import org.webrtc.SessionDescription
import org.webrtc.VideoTrack

/**
 * @Author: Jupyter.
 * @Date: 1/13/25.
 * @Email: koemheang200@gmail.com.
 */


class LiveStreamViewModel(
    application: Application,
) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context: Context = application.applicationContext

    private val _connectionState = MutableStateFlow(PeerConnectionState.NEW)
    val connectionState: StateFlow<PeerConnectionState> = _connectionState


    private val _latencyState = MutableStateFlow(0.0)
    val latencyState: StateFlow<Double> get() = _latencyState

    private lateinit var peerConnectionFactory: PeerConnectionFactory
    private lateinit var peerConnection: PeerConnection
    private lateinit var vvideoTraTrack: VideoTrack

    init {
        initPeerConnectionFactory()
        startStateCollection1()
        startStatusCollection()
    }

    private fun initPeerConnectionFactory() {
        PeerConnectionFactory.initialize(
            InitializationOptions.builder(context)
                .setEnableInternalTracer(true)
                .createInitializationOptions()
        )
        val factory = PeerConnectionFactory.builder().createPeerConnectionFactory()

        // List of ICE servers (use actual server details in production)
        val iceServers = listOf(
            IceServer.builder("stun:stun.l.google.com:19302")
                .createIceServer() // Example STUN server
        ).apply {
            PeerConnection.SdpSemantics.UNIFIED_PLAN
        }
        peerConnection = factory.createPeerConnection(iceServers, object : PeerConnection.Observer {
            override fun onSignalingChange(p0: SignalingState?) {

            }

            override fun onIceConnectionChange(state: IceConnectionState?) {
                state?.let {
                    _connectionState.value = when (it) {
                        IceConnectionState.CONNECTED -> PeerConnectionState.CONNECTED
                        IceConnectionState.DISCONNECTED -> PeerConnectionState.DISCONNECTED
                        IceConnectionState.FAILED -> PeerConnectionState.FAILED
                        IceConnectionState.NEW -> PeerConnectionState.NEW
                        IceConnectionState.CHECKING -> PeerConnectionState.CONNECTING
                        IceConnectionState.COMPLETED -> PeerConnectionState.CONNECTED
                        IceConnectionState.CLOSED -> PeerConnectionState.CLOSED
                    }
                }
            }

            override fun onIceConnectionReceivingChange(p0: Boolean) {
                println("Information of onIceConnectionReceivingChange: $p0")
            }

            override fun onIceGatheringChange(p0: IceGatheringState?) {
                println("Information of onIceGatheringChange: $p0")
            }

            override fun onIceCandidate(p0: IceCandidate?) {
                println("Information of onIceCandidate: $p0")
            }

            override fun onIceCandidatesRemoved(p0: Array<out IceCandidate>?) {
                println("Information of onIceCandidatesRemoved: $p0")
            }

            override fun onAddStream(p0: MediaStream?) {
                println("Information of onAddStream: $p0")
            }

            override fun onRemoveStream(p0: MediaStream?) {
                println("Information of onRemoveStream: $p0")
            }

            override fun onDataChannel(p0: DataChannel?) {
                println("Information of onDataChannel: $p0")
            }

            override fun onRenegotiationNeeded() {
                println("Information of onRenegotiationNeeded:")
            }

        })!!

        createOffer()
    }

    private fun createOffer() {
        val constraints = MediaConstraints() // You can define any specific constraints here
        peerConnection.createOffer(object : SdpObserver {
            override fun onCreateSuccess(desc: SessionDescription?) {
                println("Offer created successfully: ${desc?.type}")
                val resPeerConnection = peerConnection.setLocalDescription(this, desc)
            }

            override fun onSetSuccess() {
                println("Local description set successfully")

            }

            override fun onCreateFailure(error: String?) {
                println("Offer creation failed: $error")
            }

            override fun onSetFailure(error: String?) {
                println("Set description failed: $error")
            }
        }, constraints)
    }

    private fun startStateCollection1() {
        viewModelScope.launch {
            while (true) {
                peerConnection.getStats(object : RTCStatsCollectorCallback {
                    override fun onStatsDelivered(statsReport: RTCStatsReport?) {
                        statsReport?.statsMap?.values?.forEach { report ->
                            println("Stats Type: ${report.type}")
                            println("Stats Values: ${report}")
                            /* if (report.type == "candidate-pair") {
                                 val rtt = report.values.find { it.name == "currentRoundTripTime" }
                                 if (rtt != null) {
                                     val latencyMs = (rtt.value.toDouble() * 1000).toInt()
                                     println("Current Latency (RTT): $latencyMs ms")
                                 }
                             }*/
                        }
                    }
                })
                delay(1000) // Delay for 1 second before fetching stats again
            }
        }
    }


    private fun startStatusCollection() {
        viewModelScope.launch {
            while (true) {
                peerConnection.getStats { value ->
                    value.statsMap.values.forEach { state ->
                        println("Information of state  ${state.type} and members: ${state.members}")
                        if (state.type == "candidate-pair") {
                            val rtt = state.members["currentRoundTripTime"]
                            if (rtt is String) {
                                val res = rtt.toDoubleOrNull() ?: 0.0
                                println("Inside of scope _latencyState: $rtt")
                                _latencyState.value = res
                            } else {
                                println("Out of scope _latencyState: ${rtt}")
                            }
                        } else if (state.type == "peer-connection") {
                            println("Out of scope _latencyState-=-=-==: ${state.members}")
                            println("Latency from peer-connection state timestamp: ${state.members["timestampUs"]}")
                            val latency =
                                state.members["timestampUs"].toString().toDoubleOrNull() ?: 0.0
                            val convertToMs = (latency * 1000)
                            println("Latency from peer-connection: $convertToMs ms")
                        } else {

                        }
                    }
                }
                delay(1000)
            }
        }
    }
}