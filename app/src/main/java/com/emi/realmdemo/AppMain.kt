package com.emi.realmdemo

import android.app.Application
import io.realm.Realm

class AppMain  : Application(){


    override fun onCreate() {
        super.onCreate()
        Realm.init(this)

    }
}