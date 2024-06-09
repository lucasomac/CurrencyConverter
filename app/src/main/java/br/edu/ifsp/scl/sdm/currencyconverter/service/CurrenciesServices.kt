package br.edu.ifsp.scl.sdm.currencyconverter.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.util.Log
import br.edu.ifsp.scl.sdm.currencyconverter.model.api.CurrencyConverterApiClient
import br.edu.ifsp.scl.sdm.currencyconverter.model.livedata.CurrencyConverterLiveData

class CurrenciesServices : Service() {

    private lateinit var handler: CurrenciesServiceHandler
    private lateinit var serviceLogTag: String

    private inner class CurrenciesServiceHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            CurrencyConverterApiClient.currencyConverterApiService.getCurrencies().execute()
                .also { response ->
                    if (response.isSuccessful) {
                        response.body()?.let { currencyList ->
                            CurrencyConverterLiveData.currenciesLivedata.postValue(currencyList)
                        }
                    }
                }
            stopSelf(msg.arg1)
        }
    }

    override fun onCreate() {
        super.onCreate()
        HandlerThread(this.javaClass.name).apply {
            start()
            handler = CurrenciesServiceHandler(looper)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serviceLogTag = "${javaClass.simpleName}/${startId}"
        Log.v(serviceLogTag, "Service Started")
        handler.obtainMessage().also { msg ->
            msg.arg1 = startId
            handler.sendMessage(msg)
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(serviceLogTag, "Service done")
//        handler.looper.quit()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}