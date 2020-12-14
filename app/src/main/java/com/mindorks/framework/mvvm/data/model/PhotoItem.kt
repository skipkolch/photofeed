package com.mindorks.framework.mvvm.data.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class PhotoItem(
    @JsonProperty("id") var id: Int,
    @JsonProperty("download_url") var image: String,
    @JsonProperty("author") var name: String
)