package io.dotlearn.lrnplayer.utils

import android.content.Context
import android.net.ConnectivityManager

internal object WirelessUtils {

    /**
     * Checks if the device is currently connected to the internet. Please note this method is not
     * a guarantee that network calls will succeed, it only tells if the device data connection is on
     * or the device is connected to a wifi. The only way to truly confirm is to make a network call
     * @return true if the device is connected to the internet, false otherwise
     */
    internal fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

}
