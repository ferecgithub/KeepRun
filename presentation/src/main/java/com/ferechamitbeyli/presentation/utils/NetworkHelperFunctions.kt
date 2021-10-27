package com.ferechamitbeyli.presentation.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

/**
 * Network Helper Functions
 */
/*
@Suppress("DEPRECATION")
class NetworkHelperFunctions {

    companion object {

        fun isOnline(context: Context): Boolean {

            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                val activeNetwork = connectivityManager.activeNetwork ?: return false
                val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

                return when {

                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> true
                    else -> false

                }
            } else {

                return connectivityManager.activeNetworkInfo?.isAvailable ?: false

            }
        }
    }

    sealed class NetworkStatus {
        object Available : NetworkStatus()
        object Unavailable : NetworkStatus()
    }

}

 */

@Suppress("DEPRECATION")
class NetworkHelperFunctions {

    companion object {

        fun isOnline(context: Context): Boolean {

            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                val activeNetwork = connectivityManager.activeNetwork ?: return false
                val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

                return when {

                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> true
                    else -> false

                }
            } else {

                return connectivityManager.activeNetworkInfo?.isAvailable ?: false

            }
        }
    }

}




