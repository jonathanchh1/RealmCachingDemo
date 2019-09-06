package com.emi.realmdemo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmModel
import io.realm.annotations.RealmClass

@RealmClass
open class ResponseAlcohols(@Expose @SerializedName("drinks") var result : RealmList<Alcohols>?=null) : RealmModel