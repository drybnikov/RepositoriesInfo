package com.test.denis.repositoriesinfo.network

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.annotation.RequiresPermission
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class ConnectionStatusProvider(connectivityManager: ConnectivityManager) {
    val status: BehaviorSubject<ConnectionStatus> = BehaviorSubject.createDefault(ConnectionStatus.DISCONNECTED)

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    @Inject
    constructor(application: Application) : this(
        application.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
    )

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network?) {
            status.onNext(ConnectionStatus.CONNECTED)
        }

        override fun onLost(network: Network?) {
            status.onNext(ConnectionStatus.DISCONNECTED)
        }
    }

    init {
        val builder = NetworkRequest.Builder()
        connectivityManager.registerNetworkCallback(builder.build(), networkCallback)
    }
}

enum class ConnectionStatus {
    CONNECTED,
    DISCONNECTED
}