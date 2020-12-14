package com.mindorks.framework.mvvm.data.api

import retrofit2.Call

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {
    override suspend fun getPhotos(page: Int, limit: Int): Call<String> = apiService.getPhotos(page, limit)
}