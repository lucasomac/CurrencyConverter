package br.edu.ifsp.scl.sdm.currencyconverter.model.domain

import com.google.gson.annotations.SerializedName

data class CurrencyList(
    @SerializedName("currencies") val currencies: Map<String, String>,
    @SerializedName("status") val status: String
)