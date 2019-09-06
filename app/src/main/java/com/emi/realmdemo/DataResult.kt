package com.emi.realmdemo

class DataResult<T> constructor(var status : Status,  var data : T?, var error : Throwable?){

    enum class Status{
        SUCCESS,
        FAILED
    }

    companion object {
        fun <T> success(data: T?) : DataResult<T>{
            return DataResult(Status.SUCCESS, data, null)
        }

        fun<T> error(error: Throwable?) : DataResult<T>{
            return DataResult(Status.FAILED, null, error)
        }
    }
}