package com.mindorks.framework.mvvm.data.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v2/list")
    fun getPhotos(@Query("page") page: Int, @Query("limit") limit: Int): Call<String>
}