package com.emi.realmdemo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class Alcohols(@Expose @SerializedName("idDrink") @PrimaryKey var id : String?=null,
               @Expose @SerializedName("strDrink") var drink : String?=null,
               @Expose @SerializedName("strTags") var typeTag : String?=null,
               @Expose @SerializedName("strGlass") var drinkType : String?=null,
               @Expose @SerializedName("strInstructions") var instructiion : String?=null) : RealmModel