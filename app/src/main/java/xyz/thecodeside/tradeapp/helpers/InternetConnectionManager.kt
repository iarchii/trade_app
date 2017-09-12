package xyz.thecodeside.tradeapp.helpers

import android.app.Activity
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor
import javax.inject.Inject


class InternetConnectionManager @Inject internal constructor(
        private val service: ConnectivityManager){

    private val connectionState : BehaviorProcessor<Status> = BehaviorProcessor.create()

    private val onConnectionChanged : OnConnectionChanged = object : OnConnectionChanged{
        override fun onChange() {
            connectionState.onNext(isOnline())
        }

    }
    private val receiver = InternetConnectionReceiver(onConnectionChanged)

    fun isOnline(): Status = if(service.activeNetworkInfo?.state == NetworkInfo.State.CONNECTED) Status.ONLINE else Status.OFFLINE

    fun observe(activity: Activity?): Flowable<Status> {
        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        activity?.registerReceiver(receiver, intentFilter)
        return connectionState
    }

    fun stopObserve(activity: Activity?){
        activity?.unregisterReceiver(receiver)
    }

    interface OnConnectionChanged{
        fun onChange()
    }

    enum class Status{
        ONLINE,
        OFFLINE
    }

}