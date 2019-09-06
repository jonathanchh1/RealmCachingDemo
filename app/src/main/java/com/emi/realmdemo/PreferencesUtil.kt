package com.emi.realmdemo

import android.content.Context
import android.content.SharedPreferences

class PreferencesUtil {

    fun putString(key : String, value : String){
        val editor = sharedPreferences?.edit()
        editor?.putString(key, value)
        editor?.apply()
         editor?.commit()
    }

    fun putInt(key : String, value : Int){
        val editor = sharedPreferences?.edit()
        editor?.putInt(key, value)
        editor?.apply()
        editor?.commit()
    }

    fun putBoolean(key : String, value : Boolean){
        val editor = sharedPreferences?.edit()
        editor?.putBoolean(key, value)
        editor?.apply()
        editor?.commit()
    }

    fun getString(key : String, value : String) : String?{
        return sharedPreferences!!.getString(key, value)
    }

    fun getInt(key : String, value : Int) : Int{
        return sharedPreferences!!.getInt(key, value)
    }

    fun getBoolean(key : String, value : Boolean) : Boolean{
        return sharedPreferences!!.getBoolean(key, value)
    }


    companion object{
        var sharedPreferences : SharedPreferences?=null
        fun getInstance(context : Context) : PreferencesUtil{
            sharedPreferences = context.getSharedPreferences("caching", 0)
            return PreferencesUtil()
        }
    }
}