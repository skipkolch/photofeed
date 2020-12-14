package com.mindorks.framework.mvvm.data.repository

import com.mindorks.framework.mvvm.data.api.ApiHelper
import retrofit2.Call

class MainRepository(private val apiHelper: ApiHelper) {
    suspend fun getPhotos(page: Int, limit: Int): Call<String> = apiHelper.getPhotos(page, limit)
}