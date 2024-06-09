package br.edu.ifsp.scl.sdm.currencyconverter.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import br.edu.ifsp.scl.sdm.currencyconverter.R
import br.edu.ifsp.scl.sdm.currencyconverter.databinding.ActivityMainBinding
import br.edu.ifsp.scl.sdm.currencyconverter.model.livedata.CurrencyConverterLiveData
import br.edu.ifsp.scl.sdm.currencyconverter.service.CurrenciesServices

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val currenciesServicesIntent by lazy {
        Intent(this, CurrenciesServices::class.java)
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
        startService(currenciesServicesIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(currenciesServicesIntent)
    }
}