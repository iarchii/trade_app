package xyz.thecodeside.tradeapp.productlist

import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import xyz.thecodeside.tradeapp.helpers.RxTestRule
import xyz.thecodeside.tradeapp.helpers.mockProdDetails
import xyz.thecodeside.tradeapp.helpers.mockProdID
import xyz.thecodeside.tradeapp.repository.remote.rest.RemoteDataSource

@RunWith(MockitoJUnitRunner::class)
class GetProductDetailUseCaseTest{

    @Rule
    @JvmField
    val rule: RxTestRule = RxTestRule()

    @Mock
    lateinit var api : RemoteDataSource

    private lateinit var getDetails: GetProductDetailUseCase


    @Before
    fun setUp() {
        getDetails = GetProductDetailUseCase(api)
    }


    @Test
    fun `Load details from API`() {
        whenever(api.getProductDetails(mockProdID)).thenReturn(Single.just(mockProdDetails))

        val testSubscriber = getDetails.get(mockProdID).test()

        testSubscriber.assertValue(mockProdDetails)
    }


}