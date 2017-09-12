package xyz.thecodeside.tradeapp.helpers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class InternetConnectionReceiver(private val onConnectionChanged: InternetConnectionManager.OnConnectionChanged) : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        onConnectionChanged.onChange()
    }

}