package xyz.thecodeside.tradeapp.mvpbase


interface MvpPresenter<in V : MvpView> {
    fun attachView(mvpView: V)
    fun detachView()
}

