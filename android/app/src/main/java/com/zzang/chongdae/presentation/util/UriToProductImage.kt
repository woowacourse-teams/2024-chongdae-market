package com.zzang.chongdae.presentation.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.zzang.chongdae.domain.model.offeringwrite.ProductImage

fun Context.uriToProductImage(uri: Uri): ProductImage {
    val cr = contentResolver
    val mime = cr.getType(uri) ?: "image/jpeg"
    val name =
        cr.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (cursor.moveToFirst() && nameIndex >= 0) {
                cursor.getString(nameIndex)
            } else {
                "upload.jpg"
            }
        } ?: "upload.jpg"

    val bytes =
        cr.openInputStream(uri)?.use { it.readBytes() }
            ?: throw IllegalArgumentException("Cannot read uri $uri")

    return ProductImage(name, mime, bytes)
}
