package br.edu.ifsp.scl.sdm.currencyconverter.model.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CurrencyConverterApiClient {
    private const val BASE_URL = "https://currency-converter5.p.rapidapi.com/currency/"
    private val httpClient: OkHttpClient.Builder = OkHttpClient.Builder().apply {
        addInterceptor { chain ->
            val request = chain.request()
            val newRequest = request.newBuilder().addHeader(
                "x-rapidapi-key", "00954607f5msh2420266d0828d64p14fc06jsne07e164eba3e"
            ).addHeader("x-rapidapi-host", "currency-converter5.p.rapidapi.com")
                .method(request.method(), request.body()).build()
            chain.proceed(newRequest)
        }
    }
    private val client: OkHttpClient = httpClient.build()
    private val retrofit = Retrofit.Builder().baseUrl(BASE_URL).client(client).addConverterFactory(
        GsonConverterFactory.create()
    ).build()
    val currencyConverterApiService: CurrencyConverterApiService = retrofit.create(
        CurrencyConverterApiService::class.java
    )
}