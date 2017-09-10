package xyz.thecodeside.tradeapp.mvpbase


interface MvpPresenter<V : MvpView> {
    fun attachView(view: V)
    fun detachView()
}

