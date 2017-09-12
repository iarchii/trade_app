package xyz.thecodeside.tradeapp.repository.remote.rest

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import xyz.thecodeside.tradeapp.model.Product

interface RemoteDataSource {

    @GET("core/16/products/{productId}")
    fun getProductDetails(@Path("productId") productId: String): Single<Product>

}

