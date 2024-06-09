package br.edu.ifsp.scl.sdm.currencyconverter.model.domain


import com.google.gson.annotations.SerializedName

data class ConversionResult(
    @SerializedName("amount") val amount: String,
    @SerializedName("base_currency_code") val baseCurrencyCode: String,
    @SerializedName("base_currency_name") val baseCurrencyName: String,
    @SerializedName("rates") val rates: Map<String, Rate>,
    @SerializedName("status") val status: String,
    @SerializedName("updated_date") val updatedDate: String
) {
    data class Rate(
        @SerializedName("currency_name") val currencyName: String,
        @SerializedName("rate") val rate: String,
        @SerializedName("rate_for_amount") val rateForAmount: String
    )
}