package xyz.thecodeside.tradeapp.mvpbase

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


abstract class RxBasePresenter<T : MvpView> : BasePresenter<T>() {
    private val subscriptions: CompositeDisposable = CompositeDisposable()
    override fun detachView() {
        super.detachView()
        subscriptions.clear()
    }

    fun Disposable.registerInPresenter()   {
        subscriptions.add(this)
    }
}
