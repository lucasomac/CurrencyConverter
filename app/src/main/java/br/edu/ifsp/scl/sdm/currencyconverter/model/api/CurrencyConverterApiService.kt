package br.edu.ifsp.scl.sdm.currencyconverter.model.api

import br.edu.ifsp.scl.sdm.currencyconverter.model.domain.ConversionResult
import br.edu.ifsp.scl.sdm.currencyconverter.model.domain.CurrencyList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyConverterApiService {
    @GET("list")
    fun getCurrencies(): Call<CurrencyList>

    @GET("convert")
    fun convert(
        @Query("from") from: String, @Query("to") to: String, @Query("amount") amount: String
    ): Call<ConversionResult>
}