package com.mindorks.framework.mvvm.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.mindorks.framework.mvvm.BuildConfig
import com.mindorks.framework.mvvm.data.api.RequestHandler
import com.mindorks.framework.mvvm.data.model.PhotoItem
import com.mindorks.framework.mvvm.data.repository.MainRepository
import com.mindorks.framework.mvvm.utils.NetworkHelper
import com.mindorks.framework.mvvm.utils.Resource
import kotlinx.coroutines.launch

class MainViewModel(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {
    private val mapper = jacksonObjectMapper()
    private val _photos = MutableLiveData<Resource<ArrayList<PhotoItem>>>()
    val photos: LiveData<Resource<ArrayList<PhotoItem>>>
        get() = _photos

    init {
        fetchPhoto(1)
    }

    fun fetch(page: Int) {
        fetchPhoto(page)
    }

    private fun fetchPhoto(page: Int) {
        viewModelScope.launch {
            _photos.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getPhotos(page, BuildConfig.MAX_LIMIT).enqueue(RequestHandler {
                    val json: String? = it?.body()
                    if (json != null && json.isNotEmpty()) {
                        _photos.postValue(
                            Resource.success(mapper.readValue<ArrayList<PhotoItem>>(json))
                        )
                    } else _photos.postValue(Resource.error(it?.errorBody().toString(), null))
                })
            } else _photos.postValue(Resource.error("No internet connection", null))
        }
    }
}