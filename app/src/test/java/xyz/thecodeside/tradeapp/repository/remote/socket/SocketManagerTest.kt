package xyz.thecodeside.tradeapp.repository.remote.socket

import com.nhaarman.mockito_kotlin.doAnswer
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Flowable
import io.reactivex.processors.ReplayProcessor
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import xyz.thecodeside.tradeapp.helpers.Logger
import xyz.thecodeside.tradeapp.helpers.RxTestRule
import xyz.thecodeside.tradeapp.model.BaseSocket
import xyz.thecodeside.tradeapp.model.PortfolioPerformance
import xyz.thecodeside.tradeapp.model.SocketType


@RunWith(MockitoJUnitRunner::class)
class SocketManagerTest {

    private val SOCKET_ADDRESS = "ws://something.com"

    @Rule
    @JvmField
    val rule: RxTestRule = RxTestRule()

    @Mock lateinit var rxSocketWrapper: RxSocketWrapper

    @Mock lateinit var logger: Logger
    @Mock lateinit var packer: SocketItemPacker

    private lateinit var socket: SocketManager


    @Before
    fun setUp() {
        socket = SocketManager(SOCKET_ADDRESS, rxSocketWrapper, packer, logger)
    }


    @Test
    fun `Socket is available - connect to it and inform the client when READY`() {

        val socketStateObservable = ReplayProcessor.create<RxSocketWrapper.Status>()

        whenever(rxSocketWrapper.connect(SOCKET_ADDRESS)).doAnswer {
            socketStateObservable.onNext(RxSocketWrapper.Status.CONNECTED)
            socketStateObservable.onNext(RxSocketWrapper.Status.READY)

            socketStateObservable
        }

        val testObservable = socket.connect().test()

        testObservable
                .assertNoErrors()


    }

    @Test
    fun `Disconnect socket`() {

        val socketStateObservable = ReplayProcessor.create<RxSocketWrapper.Status>()

        whenever(rxSocketWrapper.connect(SOCKET_ADDRESS)).doAnswer {
            socketStateObservable.onNext(RxSocketWrapper.Status.CONNECTED)
            socketStateObservable.onNext(RxSocketWrapper.Status.READY)
            socketStateObservable
        }

        whenever(rxSocketWrapper.disconnect()).doAnswer {
            socketStateObservable.onNext(RxSocketWrapper.Status.DISCONNECTED)
            socketStateObservable.onComplete()
        }

        val testObservable = socket.connect().test()
        socket.disconnect()


        testObservable
                .assertNoErrors()
                .assertComplete()

    }

    @Test
    fun `Socket wrapper encountered an error - pass it to the subscriber`() {

        val socketStateObservable = ReplayProcessor.create<RxSocketWrapper.Status>()
        val socketException = Exception("Some socket exception")

        whenever(rxSocketWrapper.connect(SOCKET_ADDRESS)).doAnswer {
            socketStateObservable.onNext(RxSocketWrapper.Status.CONNECTED)
            socketStateObservable.onError(socketException)
            socketStateObservable
        }

        val testObservable = socket.connect().test()

        testObservable
                .assertValueCount(1)
                .assertNotComplete()
                .assertError(socketException)

    }

    @Test
    fun `Socket item unpacker encounters an error - log it and handle silently`() {

        val socketStateObservable = ReplayProcessor.create<RxSocketWrapper.Status>()

        val item1 = BaseSocket(SocketType.CONNECT_CONNECTED, PortfolioPerformance("test"))
        val item2 = BaseSocket(SocketType.TRADING_QUOTE, PortfolioPerformance("test"))

        val unpackException = RuntimeException("Unpacking item went wrong")

        whenever(rxSocketWrapper.observeSocketMessages()).thenReturn(Flowable.just("socketItem1", "someBadOrUnknownItem", "socketItem2"))

        whenever(rxSocketWrapper.connect(SOCKET_ADDRESS)).doAnswer {
            socketStateObservable.onNext(RxSocketWrapper.Status.CONNECTED)
            socketStateObservable.onNext(RxSocketWrapper.Status.READY)
            socketStateObservable
        }

        whenever(packer.unpack("socketItem1")).thenReturn(item1)
        whenever(packer.unpack("socketItem2")).thenReturn(item2)
        whenever(packer.unpack("someBadOrUnknownItem")).thenThrow(unpackException)

        socket.connect().subscribe()

        val testObservable = socket.observe().test()

        testObservable
                .assertValues(item1, item2)
                .assertNoErrors()

        verify(logger).logException(unpackException)

    }

}