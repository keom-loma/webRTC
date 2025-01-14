package com.example.webrtc.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.wifi.WifiManager
import android.telephony.CellInfoLte
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.webrtc.DataChannel
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import org.webrtc.PeerConnectionFactory
import org.webrtc.PeerConnectionFactory.InitializationOptions
import org.webrtc.DefaultVideoDecoderFactory
import org.webrtc.DefaultVideoEncoderFactory
import org.webrtc.EglBase
import org.webrtc.IceCandidate
import org.webrtc.PeerConnection.PeerConnectionState

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

    private lateinit var peerConnectionFactory: PeerConnectionFactory
    private lateinit var peerConnection: PeerConnection

    init {
        initializePeerConnectionFactory()
    }

    private fun initializePeerConnectionFactory() {
            val options = InitializationOptions.builder(context)
                .setFieldTrials("WebRTC-H264HighProfile/Enabled/")
                .createInitializationOptions()

            PeerConnectionFactory.initialize(options)
            val eglBase = EglBase.create()
            peerConnectionFactory = PeerConnectionFactory.builder()
                .setVideoDecoderFactory(DefaultVideoDecoderFactory(eglBase.eglBaseContext))
                .setVideoEncoderFactory(
                    DefaultVideoEncoderFactory(
                        eglBase.eglBaseContext,
                        true,
                        true
                    )
                )
                .createPeerConnectionFactory()
            createPeerConnection()


    }

    private fun createPeerConnection() {
        val rtcConfig = PeerConnection.RTCConfiguration(emptyList()).apply {
            iceTransportsType = PeerConnection.IceTransportsType.ALL
            sdpSemantics = PeerConnection.SdpSemantics.UNIFIED_PLAN
        }
        peerConnection = peerConnectionFactory.createPeerConnection(rtcConfig, object : PeerConnection.Observer {
            override fun onIceConnectionChange(state: PeerConnection.IceConnectionState?) {
                println("Processing of onIceConnectionChange: $state")
                state?.let {
                   _connectionState.value = when(it){
                       PeerConnection.IceConnectionState.CONNECTED -> PeerConnectionState.CONNECTED
                       PeerConnection.IceConnectionState.DISCONNECTED -> PeerConnectionState.DISCONNECTED
                       PeerConnection.IceConnectionState.FAILED -> PeerConnectionState.FAILED
                       PeerConnection.IceConnectionState.NEW -> PeerConnectionState.NEW
                       PeerConnection.IceConnectionState.CHECKING -> PeerConnectionState.CONNECTING
                       PeerConnection.IceConnectionState.COMPLETED -> PeerConnectionState.CONNECTED
                       PeerConnection.IceConnectionState.CLOSED -> PeerConnectionState.CLOSED
                   }
                }
            }

            override fun onSignalingChange(state: PeerConnection.SignalingState?) {
                println("Information of state ordinal : ${state?.ordinal}")
                println("Information of state name: ${state?.name}")
                println("Information of state: $state")
            }
            override fun onIceConnectionReceivingChange(receiving: Boolean) {
                println("Information of receiving: $receiving")
            }
            override fun onIceGatheringChange(state: PeerConnection.IceGatheringState?) {

            }
            override fun onIceCandidate(candidate: IceCandidate?) {}
            override fun onIceCandidatesRemoved(candidates: Array<out IceCandidate>?) {}
            override fun onAddStream(mediaStream: MediaStream?) {

            }
            override fun onRemoveStream(mediaStream: MediaStream?) {}
            override fun onDataChannel(dataChannel: DataChannel?) {}
            override fun onRenegotiationNeeded() {}
        }) ?: throw IllegalStateException("Failed to create PeerConnection")
    }

    @SuppressLint("MissingPermission")
    private fun getWifiSignalQuality(context: Context): String {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val rssi = wifiManager.connectionInfo.rssi

        return when {
            rssi > -50 -> "Excellent" // Strong signal
            rssi > -70 -> "Good"      // Moderate signal
            rssi > -80 -> "Fair"      // Weak signal
            else -> "Poor"            // Very weak signal
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCellularSignalQuality(context: Context): String {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val allCellInfo = telephonyManager.allCellInfo

        if (allCellInfo.isNotEmpty()) {
            val cellInfo = allCellInfo.firstOrNull() as? CellInfoLte
            val signalStrength = cellInfo?.cellSignalStrength?.dbm ?: -120

            return when {
                signalStrength > -90 -> "Excellent" // Strong signal
                signalStrength > -110 -> "Good"    // Moderate signal
                signalStrength > -130 -> "Fair"    // Weak signal
                else -> "Poor"                     // Very weak signal
            }
        }
        return "Unknown"
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}