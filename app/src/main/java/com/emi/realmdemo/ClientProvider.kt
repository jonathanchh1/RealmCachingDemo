package com.emi.realmdemo

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ClientProvider{


   private fun gsonBuilder() : Gson = GsonBuilder()
        .setLenient()
        .create()

   private fun httpClient() : OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS })
        .build()


    private fun getClient() : Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create(gsonBuilder()))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(httpClient())
        .build()


    fun networkProvider() : ClientServices{
        return getClient().create(ClientServices::class.java)
    }

    companion object{
        const val url = "https://www.thecocktaildb.com/api/json/v1/1/"
    }
}