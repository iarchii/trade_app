package xyz.thecodeside.tradeapp.mvpbase


abstract class BasePresenter<T : MvpView> : MvpPresenter<T> {
    protected var view: T? = null
        private set

    override fun attachView(mvpView: T) {
        this.view = mvpView
    }

    override fun detachView() {
        this.view = null
    }
}