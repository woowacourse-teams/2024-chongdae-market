package com.zzang.chongdae.data.remote.util

import com.zzang.chongdae.domain.model.offeringwrite.ProductImage
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

fun ProductImage.toMultipartPart(fieldName: String = "image"): MultipartBody.Part {
    val body = byteArray.toRequestBody(mimeType.toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(fieldName, name, body)
}
