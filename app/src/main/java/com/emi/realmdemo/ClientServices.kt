package com.emi.realmdemo

import io.reactivex.Observable
import retrofit2.http.GET

interface ClientServices{

    @GET("search.php?f=a")
    fun fetchCocktails() : Observable<ResponseAlcohols>
}