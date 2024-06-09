package br.edu.ifsp.scl.sdm.currencyconverter.ui

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.ArrayAdapter
import br.edu.ifsp.scl.sdm.currencyconverter.R
import br.edu.ifsp.scl.sdm.currencyconverter.databinding.ActivityMainBinding
import br.edu.ifsp.scl.sdm.currencyconverter.model.livedata.CurrencyConverterLiveData
import br.edu.ifsp.scl.sdm.currencyconverter.service.ConvertService
import br.edu.ifsp.scl.sdm.currencyconverter.service.CurrenciesServices

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val currenciesServicesIntent by lazy {
        Intent(this, CurrenciesServices::class.java)
    }
    private var convertService: ConvertService? = null
    private val convertServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            convertService = (service as ConvertService.ConvertServiceBinder).getConverterService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            //NSA
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)
        setSupportActionBar(amb.mainTb.apply { title = getString(R.string.app_name) })
        var fromQuote = ""
        var toQuote = ""
        var amount = ""
        val currenciesAdapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf<String>())
        with(amb) {
            fromQuoteMactv.apply {
                setAdapter(currenciesAdapter)
                setOnItemClickListener { _, _, position, _ ->
                    fromQuote = currenciesAdapter.getItem(position).toString()
                }
            }
            toQuoteMactv.apply {
                setAdapter(currenciesAdapter)
                setOnItemClickListener { _, _, position, _ ->
                    toQuote = currenciesAdapter.getItem(position).toString()
                }
            }
            convertBt.setOnClickListener {
                amount = amb.amountTiet.text.toString()
                convertService?.convert(fromQuote, toQuote, amount)
            }
        }
        CurrencyConverterLiveData.currenciesLivedata.observe(this) { currencyList ->
            currenciesAdapter.clear()
            currenciesAdapter.addAll(currencyList.currencies.keys.sorted())
            currenciesAdapter.getItem(0)?.also { quote ->
                amb.fromQuoteMactv.setText(quote, false)
                fromQuote = quote
            }
            currenciesAdapter.getItem(currenciesAdapter.count - 1)?.also { quote ->
                amb.toQuoteMactv.setText(quote, false)
                fromQuote = quote
            }
        }
        CurrencyConverterLiveData.conversionResultLivedata.observe(this) { conversionResult ->
            amb.resultTiet.setText(conversionResult.rates.values.first().rateForAmount)
        }
        startService(currenciesServicesIntent)
    }

    override fun onStart() {
        super.onStart()
        bindService(
            Intent(this, ConvertService::class.java),
            convertServiceConnection,
            BIND_AUTO_CREATE
        )
    }

    override fun onStop() {
        super.onStop()
        unbindService(convertServiceConnection)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(currenciesServicesIntent)
    }
}