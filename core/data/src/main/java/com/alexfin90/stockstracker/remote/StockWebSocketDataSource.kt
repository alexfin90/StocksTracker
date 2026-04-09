package com.alexfin90.stockstracker.remote

import com.alexfin90.stockstracker.exception.NonFatalException
import com.alexfin90.stockstracker.mappers.toStockPriceEvent
import com.alexfin90.stockstracker.model.StockSocketEvent
import com.alexfin90.stockstracker.remote.dto.StockPriceDto
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockWebSocketDataSource @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val moshi: Moshi,
) {

    companion object {
        private const val WEBSOCKET_URL = "wss://ws.postman-echo.com/raw"
        private const val NORMAL_CLOSURE_STATUS = 1000
    }

    private val adapter by lazy { moshi.adapter(StockPriceDto::class.java) }

    private val _events = MutableSharedFlow<StockSocketEvent>(extraBufferCapacity = 64)
    val events: SharedFlow<StockSocketEvent> = _events.asSharedFlow()

    private var webSocket: WebSocket? = null

    fun connect() {
        if (webSocket != null) return

        val request = Request.Builder()
            .url(WEBSOCKET_URL)
            .build()

        webSocket = okHttpClient.newWebSocket(request, listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Timber.d("WebSocket onOpen")
                _events.tryEmit(StockSocketEvent.Connected)
                Timber.d("StockSocketEvent.Connected")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                //Timber.v("WebSocket message received: %s", text)
                runCatching {
                    adapter.fromJson(text)
                }.onSuccess { dto ->
                    if (dto != null) {
                        val priceEvent = dto.toStockPriceEvent()
                        _events.tryEmit(StockSocketEvent.PriceUpdateReceived(priceEvent))
                        //  Timber.v("StockSocketEvent ${StockSocketEvent.PriceUpdateReceived
                        //      (priceEvent)}")
                    } else {
                        Timber.e("DTO is null")
                    }
                }.onFailure { throwable ->
                    _events.tryEmit(StockSocketEvent.Failure(throwable))
                    Timber.e(
                        t = NonFatalException(
                            code = "STOCK_WEBSOCKET_PARSE_ERROR",
                            message = "Failed to parse stock WebSocket payload",
                        ),
                        message = "Failed to parse WebSocket message"
                    )
                }
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Timber.w("WebSocket closing code=%s, reason=%s", code, reason)
                _events.tryEmit(StockSocketEvent.Disconnected)
                this@StockWebSocketDataSource.webSocket = null
            }


            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Timber.w("WebSocket closed code=%s, reason=%s", code, reason)
                _events.tryEmit(StockSocketEvent.Disconnected)
                Timber.w("WebSocket ${StockSocketEvent.Disconnected}")
                this@StockWebSocketDataSource.webSocket = null
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                val nonFatal = NonFatalException(
                    code = "STOCK_WEBSOCKET_FAILURE",
                    message = "Stock WebSocket connection failed",
                )
                t.printStackTrace()
                Timber.e(
                    nonFatal,
                    "WebSocket onFailure with response: %s",
                    response?.body?.string()
                )
                _events.tryEmit(StockSocketEvent.Failure(t))
                this@StockWebSocketDataSource.webSocket = null
            }
        })
    }

    fun send(update: StockPriceDto) {
        // Timber.v("Sending WebSocket message: %s", update)
        val payload = adapter.toJson(update)
        webSocket?.send(text = payload)
        // Timber.v("WebSocket message sent: %s", payload)
    }

    fun disconnect() {
        Timber.w("Disconnecting WebSocket")
        webSocket?.close(NORMAL_CLOSURE_STATUS, "Feed stopped by user").also {
            Timber.d(
                "WebSocket close initiated with status %s and reason: %s",
                NORMAL_CLOSURE_STATUS,
                "Feed stopped by user"
            )
        }
        webSocket = null
    }
}