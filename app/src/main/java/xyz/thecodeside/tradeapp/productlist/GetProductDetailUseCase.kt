package xyz.thecodeside.tradeapp.productlist

import xyz.thecodeside.tradeapp.repository.remote.rest.RemoteDataSource
import javax.inject.Inject

class GetProductDetailUseCase @Inject internal constructor(
        private val api: RemoteDataSource){

    fun get(productId: String) = api.getProductDetails(productId)
}
