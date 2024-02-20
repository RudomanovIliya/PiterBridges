package com.example.piterbridges.data.remote.model

import com.example.piterbridges.presentation.bridges.model.Bridge
import com.google.gson.annotations.SerializedName

class ApiBridge(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("name_eng") val nameEng: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("description_eng") val descriptionEng: String?,
    @SerializedName("divorces") val divorces: List<ApiDivorces>?,
    @SerializedName("lat") val lat: Double?,
    @SerializedName("lng") val lng: Double?,
    @SerializedName("photo_close_url") val photoCloseUrl: String?,
    @SerializedName("photo_open_url") val photoOpenUrl: String?,
    @SerializedName("public") val public: Boolean?,
)

fun ApiBridge.toModel(): Bridge {
    return Bridge(
        id = id ?: 0,
        title = name.orEmpty(),
        titleEng = nameEng.orEmpty(),
        description = description.orEmpty(),
        descriptionEng = descriptionEng.orEmpty(),
        divorces = divorces?.map { it.toModel() } ?: listOf(),
        lat = lat ?: 0.0,
        lng = lng ?: 0.0,
        photoCloseUrl = photoCloseUrl.orEmpty(),
        photoOpenUrl = photoOpenUrl.orEmpty(),
        public = public ?: false
    )
}