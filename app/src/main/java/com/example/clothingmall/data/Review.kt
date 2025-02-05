package com.example.clothingmall.data

import com.google.gson.annotations.SerializedName

data class Review(
    @SerializedName("userName") val userName: String = "",
    @SerializedName("rating") val rating: Int = 5,
    @SerializedName("comment") val comment: String = ""
) 