package com.emi.realmdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.emi.realmdemo.PreferencesUtil.Companion.getInstance
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmConfiguration
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var realmUi : Realm
    private val client = ClientProvider()
    private var disposable = Disposables.empty()
    private var compositeDisposable = CompositeDisposable()
    private var dataResult = MutableLiveData<DataResult<ResponseAlcohols>>()
    private lateinit var sharedPreferences : PreferencesUtil
    private lateinit var config : RealmConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        config = RealmConfiguration.Builder()
            .name("alcohols")
            .schemaVersion(2)
            .deleteRealmIfMigrationNeeded()
            .build()
        realmUi = Realm.getInstance(config)
        sharedPreferences = getInstance(this)
        fetchDataApi()
        displayData()
    }


    private fun fetchDataApi(){
           disposable = client.networkProvider().fetchCocktails()
               .delay(1L, TimeUnit.SECONDS)
               .subscribeOn(Schedulers.io())
               .observeOn(Schedulers.computation())
               .map(this::writeToDB)
               .observeOn(Schedulers.computation())
               .map(this::readAllDB)
               .mergeWith(Observable.fromArray(readAllDB(null)))
               .subscribeOn(Schedulers.computation())
               .observeOn(AndroidSchedulers.mainThread())
               .doOnError{
                   val error = DataResult.error<Throwable>(it)
                   Log.d(MainActivity::class.java.simpleName, "${error.error?.printStackTrace()}")
               }
               .subscribe()
               compositeDisposable.add(disposable)
    }

    private fun displayData(){
        dataResult.observe(this, Observer{
            when(it.status){
                DataResult.Status.SUCCESS ->{
                    it.data!!.result?.map {
                        Log.d(MainActivity::class.java.simpleName, "${it.instructiion}")
                    }
                }

                DataResult.Status.FAILED ->{
                    Log.d(MainActivity::class.java.simpleName, "${it.error} error from dataResult observable")
                }
            }
        })
    }

    private fun FindbyIdDB(realm : Realm) : ResponseAlcohols?{
       return realm.where(ResponseAlcohols::class.java).findFirst()
    }

    private fun fetchAllDB(realm : Realm) : List<ResponseAlcohols>?{
        return realm.where(ResponseAlcohols::class.java).findAll()
    }

    private fun readAllDB(response : List<ResponseAlcohols>?): List<ResponseAlcohols>?{
        Log.d(MainActivity::class.java.simpleName, "$response read from database")
        response?.map {
           val result = DataResult.success(it)
            dataResult.postValue(result)
        }
       return fetchAllDB(Realm.getInstance(config))
    }

    private fun writeToDB(cocktailList : ResponseAlcohols) : List<ResponseAlcohols>{
        val realm = Realm.getInstance(config)
        val cocktails = ResponseAlcohols()
        val listResponse = ArrayList<ResponseAlcohols>()
        listResponse.add(cocktailList)
        realm.executeTransaction { transactionRealm ->
            val data = FindbyIdDB(transactionRealm)
            if(data == null){
                transactionRealm.insertOrUpdate(listResponse)
            }
            Log.d(MainActivity::class.java.simpleName, "$cocktails all data")

        }
        realm.close()
        return listResponse
    }

    override fun onDestroy() {
        super.onDestroy()
        realmUi.close()
        compositeDisposable.clear()
        disposable.dispose()
    }
}
