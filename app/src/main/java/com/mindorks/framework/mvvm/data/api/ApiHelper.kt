package com.mindorks.framework.mvvm.data.api

import retrofit2.Call

interface ApiHelper {
    suspend fun getPhotos(page: Int, limit: Int): Call<String>
}