package com.mindorks.framework.mvvm.data.api

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RequestHandler<T>(private val combine: (response: Response<T>?) -> Unit) : Callback<T> {
    override fun onFailure(call: Call<T>?, t: Throwable?) {
    }

    override fun onResponse(call: Call<T>?, response: Response<T>?) {
        combine(response)
    }
}