package net.geeksempire.simpleandroiddemonstration.Network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities

class NetworkCheckpoint(val context: Context) {

    fun networkConnection() : Boolean {

        var networkAvailable = false

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        if (connectivityManager != null) {

            val activeNetwork: Network? = connectivityManager.activeNetwork
            val networkCapabilities: NetworkCapabilities? = connectivityManager.getNetworkCapabilities(activeNetwork)

            if (networkCapabilities != null) {

                when {
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {

                        networkAvailable = true

                    }
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {

                        networkAvailable = true

                    }
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> {

                        networkAvailable = true

                    }
                }

            }

        }

        return networkAvailable
    }

    fun networkConnectionVpn() : Boolean {

        var networkAvailable = false

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        if (connectivityManager != null) {

            val activeNetwork: Network? = connectivityManager.activeNetwork
            val networkCapabilities: NetworkCapabilities? = connectivityManager.getNetworkCapabilities(activeNetwork)

            if (networkCapabilities != null) {

                if (networkConnection()
                        && networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {

                    networkAvailable = true

                }

            }

        }

        return networkAvailable
    }

}